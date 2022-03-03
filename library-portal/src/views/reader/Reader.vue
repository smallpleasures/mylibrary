<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;">
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;"
                    prefix-icon="el-icon-search"
                    v-model="name"
                    placeholder="请输入读者名称进行搜索..."
                    @keydown.enter.native="initReaders"
                    clearable
                    @clear="initReaders"
                    :disabled="showAdvanceSearchVisible"
          ></el-input>
          <el-button type="primary" icon="el-icon-search" @click="initReaders"
                     :disabled="showAdvanceSearchVisible">搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">
            <i :class="showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'"
               aria-hidden="true"></i>高级搜索
          </el-button>
        </div>
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddReaderView">添加读者</el-button>
        </div>
      </div>

      <transition name="slide-fade">
        <div v-show="showAdvanceSearchVisible"
             style="border: 1px solid #379ff5;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0;">
          <el-row>
            <el-col :span="5">
              用户名：
              <el-input v-model="searchValue.username" placeholder="请输入用户名" size="mini" style="width: 130px;">
              </el-input>
            </el-col>
            <el-col :span="5">
              性别：
              <el-radio-group v-model="searchValue.sex" size="mini">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :span="5">
              读者类别:
              <el-select v-model="searchValue.readerCategoryId" placeholder="请选择读者类别" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in readerCategories"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="5">
              地址：
              <el-input v-model="searchValue.address" placeholder="请输入地址" size="mini" style="width: 130px;">
              </el-input>
            </el-col>

            <el-col :span="5" :offset="3">
              <el-button size="mini">取消</el-button>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="initReaders('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="readers"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%" stripe border>
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="读者名称"
            fixed="left"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="username"
            label="用户名"
            align="left"
            width="80">
        </el-table-column>
        <el-table-column
            prop="sex"
            label="性别"
            align="left"
            width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.sex===0">男</el-tag>
            <el-tag v-if="scope.row.sex===1">女</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="生日"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="telephone"
            label="手机号"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="avatar"
            label="头像"
            align="left"
            width="85">
          <template slot-scope="scope">
            <img :src="scope.row.avatar" style="border-radius: 2px" width="30px" height="30px">
          </template>
        </el-table-column>
        <el-table-column
            prop="readerCategoryName"
            label="读者类别"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="创建时间"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            label="更新时间"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            label="操作"
            fixed="right"
            >
          <template slot-scope="scope">
            <el-button style="padding: 3px;" size="mini" @click="showReaderView(scope.row)">编辑</el-button>
            <el-button style="padding: 3px;" size="mini" type="danger" @click="deleteReader(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 10、分页 -->
      <div style="display: flex;justify-content: flex-end;margin-top: 10px;">
        <el-pagination
            prev-text="上一页"
            next-text="下一页"
            @current-change="currentChange"
            @size-change="sizeChange"
            :page-sizes="[10,20,30,50,100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total" background>
        </el-pagination>
      </div>
    </div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <el-form ref="readerRef" :model="reader" :rules="readerRules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="读者名称：" prop="name">
                <el-input v-model="reader.name" prefix-icon="el-icon-edit" placeholder="请输入读者名称" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="用户名：" prop="username">
                <el-input v-model="reader.username" prefix-icon="el-icon-edit" placeholder="请输入用户名" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" v-show="title==='添加读者'">
              <el-form-item label="密码：" prop="password">
                <el-input v-model="reader.password" prefix-icon="el-icon-edit" placeholder="请输入密码" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="性别：" prop="sex">
                <el-radio-group v-model="reader.sex">
                  <el-radio :label="0">男</el-radio>
                  <el-radio :label="1">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="生日：" prop="birthday">
                <el-date-picker
                    v-model="reader.birthday"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 150px;"
                    placeholder="生日">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="地址：" prop="address">
                <el-input v-model="reader.address" prefix-icon="el-icon-edit" placeholder="请输入地址" size="mini"
                          style="width: 180px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="手机号：" prop="telephone">
                <el-input v-model="reader.telephone" prefix-icon="el-icon-edit" placeholder="请输入手机号" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="头像：" prop="avatar">
                <el-input v-model="reader.avatar" prefix-icon="el-icon-edit" placeholder="请输入头像地址" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>

            <el-col :span="7">
              <el-form-item label="读者类别：" prop="readerCategoryId">
                <el-select v-model="reader.readerCategoryId" placeholder="请选择类别" size="mini" style="width: 200px;">
                  <el-option
                      v-for="item in readerCategories"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddReader">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Reader",
  data() {
    return {
      searchValue: {
        username: '',
        sex: '',
        address: '',
        readerCategoryId: null
      },
      showAdvanceSearchVisible: false,
      title: '',
      readers: [],
      readerCategories: [],
      loading: false,
      total: 0, // 11 分页总条数
      currentPage: 1, // 14、默认显示第1页(currentPage 后端字段）
      size: 10, // 15、默认每页显示 10 条
      name: '', // 18、搜索
      dialogVisible: false,
      reader: {
        id: null,
        name: '',
        username: '',
        password: '',
        sex: null,
        birthday: '',
        address: '',
        telephone: null,
        avatar: '',
        readerCategoryId: null,
      },
      visible: false, //弹出框

      readerRules: {
        name: [{required: true, message: '请输入读者名称', trigger: 'blur'}],
        username: [{required: true, message: '请输入读者用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入读者密码', trigger: 'blur'}],
        sex: [{required: true, message: '性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入生日', trigger: 'blur'}],
        address: [{required: true, message: '请输入地址', trigger: 'blur'}],
        telephone: [{required: true, message: '请输入手机号', trigger: 'blur'}],
        avatar: [{required: true, message: '请输入头像地址', trigger: 'blur'}],
        readerCategoryId: [{required: true, message: '请输入读者类别', trigger: 'blur'}],
      }

    }
  },
  mounted() {
    this.initReaders()
    this.initData()
  },
  methods: {
    showReaderView(data) {
      this.title = '编辑读者信息'
      Object.assign(this.reader, data)
      this.dialogVisible = true
    },
    // 删除读者
    deleteReader(data) {
      this.$confirm('此操作将永久删除该读者' + data.name + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/reader/' + data.id).then(resp => {
          if (resp) {
            this.initReaders()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    doAddReader() {
      if (this.reader.id) {
        // 有 id 编辑读者
        this.$refs['readerRef'].validate(valid => {
          if (valid) {
            this.putRequest('/reader/', this.reader).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initReaders()
              }
            })
          }
        })
      } else {
        // 没有id 添加读者
        this.$refs['readerRef'].validate(valid => {
          if (valid) {
            this.postRequest('/reader/', this.reader).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initReaders()
              }
            })
          }
        })
      }
    },

    initData() {
      this.getRequest('/readerCategory/').then(resp => {
        this.readerCategories = resp.data
      })
    },
    //添加读者点击事件
    showAddReaderView() {
      // 清空表单
      this.reader = {
        id: null,
        name: '',
        username: '',
        password: '',
        sex: null,
        birthday: '',
        address: '',
        telephone: null,
        avatar: '',
        readerCategoryId: null,
      }
      this.title = '添加读者'
      this.dialogVisible = true
    },
    sizeChange(size) {
      this.size = size
      this.initReaders()
    },
    currentChange(currentPage) {
      this.currentPage = currentPage
      this.initReaders()
    },
    initReaders(type) {
      this.loading = true // 8、添加 loading
      // 30-11 定义高级搜索 url
      let url = '/reader/?currentPage=' + this.currentPage + '&size=' + this.size
      if (type && type === 'advanced') { // 说明是高级搜索
        if (this.searchValue.username) {
          url += '&username=' + this.searchValue.username
        }
        if (this.searchValue.sex) {
          url += '&sex=' + this.searchValue.sex
        }
        if (this.searchValue.address) {
          url += '&address=' + this.searchValue.address
        }
        if (this.searchValue.readerCategoryId) {
          url += '&readerCategoryId=' + this.searchValue.readerCategoryId
        }
      } else {
        url += '&name=' + this.name
      }
      this.getRequest(url).then(resp => {
        this.loading = false
        if (resp) {
          this.readers = resp.data
          this.total = resp.total
          this.currentPage = 1
        }
      });
    }
  }
}
</script>

<style>
/*28-7 展开收起条件搜索框动画样式 */
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
