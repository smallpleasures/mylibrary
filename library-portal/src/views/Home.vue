<template>
  <el-container class="container">
    <el-header class="homeHeader" height="60px">
      <div class="title">
        图书管理系统
      </div>
      <div>
        <el-dropdown class="userInfo" @command="commandHandler">
        <span class="el-dropdown-link">
          {{user.name}}<i><img :src="user.avatar"></i>
        </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
            <el-dropdown-item command="setting">设置</el-dropdown-item>
            <el-dropdown-item command="logout">注销登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-container >
    <el-aside  width="200px" style="background-color: #545c64;">
              <el-menu router :unique-opened="true" style="height: calc(100vh - 60px); border-right-width: 0"
                  background-color="#545c64"
                  text-color="#fff"
                  active-text-color="#ffd04b">
                <el-submenu :index="index + ''" v-for="(item,index) in routes" :key="index">
                  <template slot="title">
                    <i :class="item.iconCls" style="margin-right: 5px; color: #379ff5"></i>
                    <span>{{item.name}}</span>
                  </template>
                  <el-menu-item :index="children.path" v-for="(children,index) in
                   item.children" :key="index">{{children.name}}</el-menu-item>
                </el-submenu>
              </el-menu>
    </el-aside>
    <el-main>
      <el-breadcrumb separator-class="el-icon-arrow-right" v-if="$router.currentRoute.path!='/home'">
        <el-breadcrumb-item :to="{path: '/home'}">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{$router.currentRoute.name}}</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="homeWelcome" v-if="$router.currentRoute.path=='/home'">
        欢迎来到图书管理系统！
      </div>
      <router-view class="content" />
    </el-main>
  </el-container>

  </el-container>
</template>

<script>

export default {
  name: "Home",
  data() {
    return {
      // user: JSON.parse(sessionStorage.getItem('user')),
      // routes: JSON.parse(sessionStorage.getItem('routes')),
      aside: {
        height: ''
      }
    }
  },
  computed: {
    routes() {
      return JSON.parse(sessionStorage.getItem('routes'))
    },
    user() {
      return JSON.parse(sessionStorage.getItem('user')) == null ? {} : JSON.parse(sessionStorage.getItem('user'))
    }
  },
  created() {
    this.aside.height = document.body.scrollHeight - 20 + 'px';
  },
  methods: {
    commandHandler(command) {
      if (command === 'logout') {
        this.$confirm('此操作将注销登录，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.postRequest('/logout')
          sessionStorage.removeItem('tokenStr')
          sessionStorage.removeItem('user')
          sessionStorage.removeItem('routes')
          this.$router.replace('/')
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消注销登录'
          })
        })
      }
      if (command === 'userinfo') {
        this.$message({
          type: 'info',
          message: '未开发'
        })
      }
    }

  }
}
</script>

<style scoped>

  /*.container {
    height: 100%;
    overflow: hidden;
  }*/

  .homeHeader {
    background: #4787f0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 15px;
    box-sizing: border-box;
  }
  .homeHeader .title {
    font-size: 30px;
    font-family: 华文楷体;
    color: white;
  }
  .homeHeader .userInfo {
    cursor: pointer;
  }
  /*.el-aside {

    !*height: calc(100vh - 60px);*!
    height: 100vh;
  }*/
  .el-dropdown-link img {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    margin-right: 8px;
  }

  .homeWelcome {
    text-align: center;
    font-size: 30px;
    font-family: 华文楷体;
    color: #409ef4;
    padding-top: 50px;
  }

  .content {
    margin-top: 10px;
  }

</style>