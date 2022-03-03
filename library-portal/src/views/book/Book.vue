<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between;">
        <div style="margin-top: 10px;">
          <el-input style="width: 300px;margin-right: 10px;"
                    prefix-icon="el-icon-search"
                    v-model="name"
                    placeholder="请输入书籍名称进行搜索..."
                    @keydown.enter.native="initBooks"
                    clearable
                    @clear="initBooks"
                    :disabled="showAdvanceSearchVisible"
          ></el-input>
          <el-button type="primary" icon="el-icon-search" @click="initBooks"
                     :disabled="showAdvanceSearchVisible">搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">
            <i :class="showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'"
               aria-hidden="true"></i>高级搜索
          </el-button>
        </div>
        <div v-if="!user.readerCategoryId">
          <el-button type="primary" icon="el-icon-plus" @click="showAddBookView">添加书籍</el-button>
        </div>
      </div>

      <transition name="slide-fade">
        <div v-show="showAdvanceSearchVisible"
             style="border: 1px solid #379ff5;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0;">
          <el-row>
            <el-col :span="5">
              图书类别：
              <el-select v-model="searchValue.classId" placeholder="请选择图书类别" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in classes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              区域类别：
              <el-select v-model="searchValue.areaId" placeholder="请选择区域类别" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in areas"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              在馆状态：
              <el-radio-group v-model="searchValue.inLibrary">
                <el-radio :label="true">在馆</el-radio>
                <el-radio :label="false">所有</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="5">
              作者：
              <el-input v-model="searchValue.author" placeholder="请输入作者" size="mini" style="width: 130px;">
              </el-input>
            </el-col>
            <el-col :span="5">
              出版社：
              <el-input v-model="searchValue.publish" placeholder="请输入出版社" size="mini" style="width: 130px;">
              </el-input>
            </el-col>
            <el-col :span="5">
              ISBN：
              <el-input v-model="searchValue.isbn" placeholder="请输入isbn号" size="mini" style="width: 130px;">
              </el-input>
            </el-col>
            <el-col :span="5">
              语言：
              <el-input v-model="searchValue.language" placeholder="请输入语言" size="mini" style="width: 130px;">
              </el-input>
            </el-col>
            <el-col :span="3" :offset="1">
              <el-button size="mini">取消</el-button>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="initBooks('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="books"
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
            label="书籍名称"
            fixed="left"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="author"
            label="作者"
            align="left"
            width="80">
        </el-table-column>
        <el-table-column
            prop="publish"
            label="出版社"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="isbn"
            label="ISBN"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="introduction"
            label="介绍"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="language"
            label="语言"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="price"
            label="价格"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="pubDate"
            label="出版日期"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="className"
            label="类别"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="areaName"
            label="区域"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="stock"
            label="库存"
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
            label="操作"
            fixed="right"
            >
          <template slot-scope="scope">
            <el-button style="padding: 3px;" size="mini" @click="showBookView(scope.row)" v-if="!user.readerCategoryId">编辑</el-button>
            <el-button style="padding: 3px;" size="mini" type="danger" @click="deleteBook(scope.row)" v-if="!user.readerCategoryId">删除</el-button>
            <el-button style="padding: 3px;" size="mini" type="success" @click="lendBook(scope.row)" v-if="user.readerCategoryId">借阅</el-button>
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
        <el-form ref="bookRef" :model="book" :rules="bookRules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="书籍名称：" prop="name">
                <el-input v-model="book.name" prefix-icon="el-icon-edit" placeholder="请输入书籍名称" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="作者：" prop="author">
                <el-input v-model="book.author" prefix-icon="el-icon-edit" placeholder="请输入作者" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出版社：" prop="publish">
                <el-input v-model="book.publish" prefix-icon="el-icon-edit" placeholder="请输入出版社" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="ISBN：" prop="isbn">
                <el-input v-model="book.isbn" prefix-icon="el-icon-edit" placeholder="请输入isbn" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="介绍：" prop="introduction">
                <el-input v-model="book.introduction" prefix-icon="el-icon-edit" placeholder="请输入书籍介绍" size="mini"
                          style="width: 180px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="书籍语言：" prop="name">
                <el-input v-model="book.language" prefix-icon="el-icon-edit" placeholder="请输入书籍语言" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="价格：" prop="price">
                <el-input v-model="book.price" prefix-icon="el-icon-edit" placeholder="请输入书籍价格" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出版日期：" prop="pubDate">
                <el-date-picker
                    v-model="book.pubDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    size="mini"
                    style="width: 150px;"
                    placeholder="出版日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="类别：" prop="classId">
                <el-select v-model="book.classId" placeholder="请选择类别" size="mini" style="width: 200px;">
                  <el-option
                      v-for="item in classes"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="区域：" prop="areaId">
                <el-select v-model="book.areaId" placeholder="区域" size="mini" style="width: 150px;">
                  <el-option
                      v-for="item in areas"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="数量：" prop="stock">
                <el-input v-model="book.stock" placeholder="数量" prefix-icon="el-icon-edit" size="small"
                          style="width: 120px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddBook">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Book",
  data() {
    return {
      searchValue: {
        author: '',
        publish: '',
        isbn: '',
        classId: null,
        areaId: null,
        inLibrary: false // 是否在馆
      },
      user: JSON.parse(sessionStorage.getItem('user')),
      showAdvanceSearchVisible: false,
      title: '',
      books: [],
      loading: false,
      total: 0, // 11 分页总条数
      currentPage: 1, // 14、默认显示第1页(currentPage 后端字段）
      size: 10, // 15、默认每页显示 10 条
      name: '', // 18、搜索
      dialogVisible: false,
      classes: [],
      areas: [],
      book: {
        id: null,
        name: '',
        author: '',
        publish: '',
        isbn: '',
        introduction: '',
        language: '',
        price: null,
        pubDate: '',
        classId: null,
        areaId: null,
        stock: null,
      },
      visible: false, //弹出框

      bookRules: {
        name: [{required: true, message: '请输入书籍名称', trigger: 'blur'}],
        author: [{required: true, message: '请输入作者', trigger: 'blur'}],
        publish: [{required: true, message: '请输入出版社', trigger: 'blur'}],
        isbn: [{required: true, message: '请输入isbn', trigger: 'blur'}],
        introduction: [{required: true, message: '请输入介绍', trigger: 'blur'}],
        language: [{required: true, message: '请输入语言', trigger: 'blur'}],
        price: [{required: true, message: '请输入价格', trigger: 'blur'}],
        pubDate: [{required: true, message: '请输入出版日期', trigger: 'blur'}],
        classId: [{required: true, message: '请输入类别', trigger: 'blur'}],
        areaId: [{required: true, message: '请输入区域', trigger: 'blur'}],
        stock: [{required: true, message: '请输入数量', trigger: 'blur'}],
      }

    }
  },
  mounted() {
    this.initBooks()
    this.initData()
  },
  methods: {
    showBookView(data) {
      this.title = '编辑书籍信息'
      Object.assign(this.book, data)
      this.dialogVisible = true
    },
    // 删除书籍
    deleteBook(data) {
      this.$confirm('此操作将永久删除该书籍' + data.name + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/book/' + data.id).then(resp => {
          if (resp) {
            this.initBooks()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 借阅书籍
    lendBook(data) {
      this.$confirm('将借阅书籍' + data.name + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest('/lendInfo/' + data.id).then(resp => {
          if (resp) {
            this.initBooks()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消借阅'
        });
      });
    },

    doAddBook() {
      if (this.book.id) {
        // 有 id 编辑书籍
        this.$refs['bookRef'].validate(valid => {
          if (valid) {
            this.putRequest('/book/', this.book).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initBooks()
              }
            })
          }
        })
      } else {
        // 没有id 添加书籍
        this.$refs['bookRef'].validate(valid => {
          if (valid) {
            this.postRequest('/book/', this.book).then(resp => {
              if (resp) {
                this.dialogVisible = false
                this.initBooks()
              }
            })
          }
        })
      }
    },


    initData() {
      this.getRequest('/class/').then(resp => {
        this.classes = resp.data
      })
      this.getRequest('/area/').then(resp => {
        this.areas = resp.data
      })
    },
    //添加书籍点击事件
    showAddBookView() {
      // 清空表单
      this.book = {
        id: null,
        name: '',
        author: '',
        publish: '',
        isbn: '',
        introduction: '',
        language: '',
        price: null,
        pubDate: '',
        classId: null,
        areaId: null,
        stock: null,
      }
      this.title = '添加书籍'
      this.dialogVisible = true
    },
    sizeChange(size) {
      this.size = size
      this.initBooks()
    },
    currentChange(currentPage) {
      this.currentPage = currentPage
      this.initBooks()
    },
    initBooks(type) {
      this.loading = true // 8、添加 loading
      // 30-11 定义高级搜索 url
      let url = '/book/?currentPage=' + this.currentPage + '&size=' + this.size
      if (type && type === 'advanced') { // 说明是高级搜索
        if (this.searchValue.author) {
          url += '&author=' + this.searchValue.author
        }
        if (this.searchValue.publish) {
          url += '&publish=' + this.searchValue.publish
        }
        if (this.searchValue.isbn) {
          url += '&isbn=' + this.searchValue.isbn
        }
        if (this.searchValue.classId) {
          url += '&classId=' + this.searchValue.classId
        }
        if (this.searchValue.areaId) {
          url += '&areaId=' + this.searchValue.areaId
        }
        if (this.searchValue.inLibrary) {
          url += '&inLibrary=' + this.searchValue.inLibrary
        }
      } else {
        url += '&name=' + this.name
      }
      this.getRequest(url).then(resp => {
        this.loading = false
        if (resp) {
          this.books = resp.data
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
