<template>
    <div id="login">
      <h3>登录</h3>
      <form>
        <input type="text" placeholder="请输入用户名" v-model.trim="loginData.username" name="username">
        <input type="password" placeholder="请输入密码" v-model.trim="loginData.password" name="password">
        <!--<input type="button" value="登录" @click="doLogin">-->
        <button v-on:click="doLogin">登录</button>
        <p>
          <router-link to="/register">没有账号？马上注册</router-link>
        </p>
      </form>
    </div>
</template>

<script>
  import utils from '../utils/utils'
  import router from '../router'
  export default {
    name: 'Login',
    data () {
      return {
        loginData:{
          username:"11111",
          password:"test"
        }
      }
    },
    methods:{
      doLogin:function(){
        let response=(response)=>{
          debugger
          if(response.data.code===200){
            alert("登录成功");
            router.push({ // 注册成功跳转首页
              path: "/home"
            });
          } else {
            alert(response.data.message);
            console.log(JSON.stringify(response.data));

          }
          /*this.$options.methods.zUserList(response.data);*/  // 跳转到zUserList方法中
        }
        utils.axiosMethod({
          method: "post",
          url:"/api/user/login",
          headers: {
            'Content-Type': 'application/json'
          },
          data:JSON.stringify(this.loginData),
          callback:response
        })

      },
      zUserList(data){
        console.log(JSON.stringify(data));
      }

    }
  }
</script>

<style>
  .login-wrap {
    text-align: center;
  }

  h3 {
    text-align: center;
  }
  span {
    text-align: center;
  }
  input {
    display: block;
    width: 250px;
    height: 40px;
    line-height: 40px;
    margin: 0 auto;
    margin-bottom: 10px;
    outline: none;
    border: 1px solid #888;
    padding: 10px;
    box-sizing: border-box;
  }

  p {
    color: red;
    text-align: center;
  }

  button {
    display: block;
    width: 250px;
    height: 40px;
    line-height: 40px;
    margin: 0 auto;
    border: none;
    background-color: #41b883;
    color: #fff;
    font-size: 16px;
    margin-bottom: 5px;
  }

  span {
    cursor: pointer;
  }

  span:hover {
    color: #41b883;
  }
</style>
