<template>
  <div>
    <div>
      <el-input
          size="small"
          class="addAreaInput"
          placeholder="请输入区域类别"
          suffix-icon="el-icon-plus"
          @keydown.enter.native="addArea"
          v-model="area.name">
      </el-input>
      <el-button size="small" icon="el-icon-plus" type="primary" @click="addArea">添加</el-button>
    </div>
    <div class="AreaMain">
      <el-table
          border
          stripe
          size="small"
          :data="areas"
          style="width: 70%"
          @selection-change="handleSelectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="id"
            label="编号"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="类别"
            width="120">
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="创建时间"
            width="200">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="showEditView(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- :disabled 不禁用条件为勾选中，没勾选中为禁用。 -->
    <el-button size="small" style="margin-top: 8px" type="danger"
               :disabled="this.multipleSelection.length===0" @click="deleteMany">批量删除
    </el-button>
    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <el-tag>区域类别名称</el-tag>
        <el-input v-model="updateArea.name" size="small" class="updateAreaInput"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "Area",
  data() {
    return {
      area: { // 查询添加数据
        name: ''
      },
      areas: [],
      dialogVisible: false,
      updateArea: { // 更新类别
        name: ''
      },
      multipleSelection: [] // 批量删除勾选的对象
    }
  },
  mounted() {
    // 调用获取后端接口所有区域类别
    this.initAreas()
  },
  methods: {
    // 批量删除请求
    deleteMany() {
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条类别, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?'
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&'
        })
        this.deleteRequest('/area/' + ids).then(resp => {
          if (resp) {
            this.initAreas()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 批量删除(取值）
    handleSelectionChange(val) {
      this.multipleSelection = val
      // console.log(val)
    },
    // 编辑
    doUpdate() {
      this.putRequest('/area/', this.updateArea).then(resp => {
        if (resp) {
          this.initAreas() // 刷新数据列表
          this.dialogVisible = false // 关闭对话框
        }
      })
    },
    // 编辑对话框
    showEditView(index, data) {
      Object.assign(this.updateArea, data) // 回显数据,数据拷贝
      this.updateArea.createTime = ''
      this.dialogVisible = true // 显示编辑框
    },
    // 添加类别
    addArea() {
      if (this.area.name) {
        this.postRequest('/area/', this.area).then(resp => {
          if (resp) {
            this.initAreas()
            this.area.name = ''
          }
        })

      } else {
        this.$message.error('类别不能为空！')
      }
    },
    // 删除
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除[' + data.name + ']类别, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest(' /area/' + data.id).then(resp => {
          if (resp) {
            this.initAreas()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 获取后端接口所有数据
    initAreas() {
      this.getRequest('/area/').then(resp => {
        if (resp) {
          this.areas = resp.data
        }
      })
    }
  }
}
</script>

<style scoped>
.addAreaInput {
  width: 300px;
  margin-right: 8px;
}

/*所有数据表格*/
.AreaMain {
  margin-top: 10px;
}

/*编号类别输入框*/
.updateAreaInput
{
  width: 200px;
  margin-left: 8px;
}
</style>
