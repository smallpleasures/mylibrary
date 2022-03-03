<template>
  <div class="container">
    <el-form ref="loginForm" :model="loginForm" :rules="rules" class="loginContainer"
             v-loading="loading"
             element-loading-text="正在登录..."
             element-loading-spinner="el-icon-loading"
             element-loading-background="rgba(0, 0, 0, 0.8)">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input type="text" auto-complete="false" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" auto-complete="false" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="admin">
        <el-radio v-model="loginForm.admin" :label="true">管理员</el-radio>
        <el-radio v-model="loginForm.admin" :label="false">普通用户</el-radio>
      </el-form-item>
<!--        <el-checkbox class="remember" v-model="checked">记住我</el-checkbox>-->
        <el-button type="primary" style="width: 100%" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>

export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        admin: null
      },
      loading: false, //加载
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        admin: [
          { required: true, message: '请选择用户类型', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // 登录
    submitLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.success()
        } else {
          this.$message.error('请输入所有字段！')
          return false
        }
      })
    },
    // 验证成功
    async success() {
      this.loading = true
      await this.postRequest('/login', this.loginForm).then(async resp => {
        this.loading = false
        if (resp) {
          // 存储用户token到sessionStorage
          //console.log(resp.object)
          const tokenStr = resp.object.tokenHead + ' ' + resp.object.token
          sessionStorage.setItem('tokenStr', tokenStr)
          await this.getUserInfo()
        }
      })
    },

    async getUserInfo() {
      await this.getRequest('/userinfo').then(resp => {
        if (resp.object) {
          // 存入用户信息，转字符串，存入 sessionStorage
          sessionStorage.setItem('user', JSON.stringify(resp.object))
          // 同步用户信息 编辑用户
          // store.commit('INIT_ADMIN',resp)
          let path = this.$route.query.redirect
          this.$router.replace((path == '/' || path == undefined) ? '/home' : path)
        }
      })
    }

  }
}

</script>

<style scoped>
  .container{
    background: url("~@/assets/background.png") center center no-repeat fixed;
    background-size: cover;
    height: 100vh;
    width: 100vw;
    position: fixed;
  }
  .loginContainer{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .loginTitle {
    margin: 0 auto 40px auto;
    text-align: center;
  }

  .remember {
    margin: 0 0 15px 0;
  }

</style>