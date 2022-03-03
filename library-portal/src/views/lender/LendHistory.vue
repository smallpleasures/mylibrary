<template>
  <div>
    <div>
      <div style="display: flex;justify-content: flex-start;">
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;"
                    prefix-icon="el-icon-search"
                    v-model="searchValue.bookName"
                    placeholder="请输入书籍名称进行搜索..."
                    @keydown.enter.native="initLendInfo"
                    clearable
                    @clear="initLendInfo"
          ></el-input>
          <el-select v-model="searchValue.state" placeholder="状态" size="mini" style="width: 200px;margin-right: 10px">
            <el-option
                label="未逾期"
                value="1">
            </el-option>
            <el-option
                label="逾期"
                value="0">
            </el-option>
            <el-option
                label="已归还"
                value="2">
            </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-search" @click="initLendInfo">搜索
          </el-button>
        </div>

      </div>

    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="lendInfo"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%" stripe border>
        <el-table-column
            prop="bookName"
            label="书籍名称"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="lendDate"
            label="借阅日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="backDate"
            label="归还日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="renewDate"
            label="续借日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            width="150">
          <template slot-scope="scope">
            <el-tag type="warning" style="padding: 3px;" size="mini" v-if="scope.row.state===0">已逾期</el-tag>
            <el-tag type="success" style="padding: 3px;" size="mini" v-if="scope.row.state===1">未逾期</el-tag>
            <el-tag type="success" style="padding: 3px;" size="mini" v-if="scope.row.state===2">已归还</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state!==2"
                size="mini" type="success"
                @click="backBook(scope.row)">归还
            </el-button>
            <el-button v-if="scope.row.state===2"
                size="mini" type="success"
                @click="renewBook(scope.row)">续借
            </el-button>
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
  </div>
</template>

<script>
export default {
  name: "LendHistory",
  data() {
    return {
      searchValue: {
        bookName: '',
        state: null
      },
      lendInfo: [],
      loading: false,
      total: 0, // 11 分页总条数
      currentPage: 1, // 14、默认显示第1页(currentPage 后端字段）
      size: 10, // 15、默认每页显示 10 条

    }
  },
  mounted() {
    this.initLendInfo()
  },
  methods: {

    // 归还
    backBook(data) {
      this.$confirm('将归还书籍' + data.bookName + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest('/lendInfo/' + data.id + '/' + data.bookId).then(resp => {
          if (resp) {
            this.initLendInfo()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消归还'
        });
      });
    },
    // 续借
    renewBook(data) {
      this.$confirm('将续借书籍' + data.bookName + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.putRequest('/lendInfo/renewBook/' + data.id + '/' + data.bookId).then(resp => {
          if (resp) {
            this.initLendInfo()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消续借'
        });
      });
    },
    sizeChange(size) {
      this.size = size
      this.initLendInfo()
    },
    currentChange(currentPage) {
      this.currentPage = currentPage
      this.initLendInfo()
    },
    initLendInfo() {
      this.loading = true // 8、添加 loading
      let url = '/lendInfo/currentReader?currentPage=' + this.currentPage + '&size=' + this.size

      if (this.searchValue.bookName) {
        url += '&bookName=' + this.searchValue.bookName
      }
      if (this.searchValue.state) {
        url += '&state=' + this.searchValue.state
      }

      this.getRequest(url).then(resp => {
        this.loading = false
        if (resp) {
          this.lendInfo = resp.data
          this.total = resp.total
          this.currentPage = 1
        }
      });
    }
  }
}
</script>

<style>

</style>
