<template>
  <div id="login">
    <h3>注册</h3>
    <form>
      <input type="text" placeholder="请输入用户名" v-model.trim="registerData.username" name="username">
      <input type="phoneNumber" placeholder="请输入手机号码" v-model.trim="registerData.phoneNumber" name="phoneNumber">
      <input type="password" placeholder="请输入密码" v-model.trim="registerData.password" name="password">
      <!--<input type="button" value="注册" @click="register">-->
      <button v-on:click="register">注册</button>
      <p><router-link to="/login">已有账号？马上登录</router-link></p>
    </form>
    <br />
  </div>
</template>

<script>
  import utils from '../utils/utils';
  import router from "../router";

  export default {
    name: 'register',
    data () {
      return {
        registerData:{
          username:"zhang456",
          phoneNumber:"18292000000",
          password:"zhang456"
        }
      }
    },
    methods:{
      register(){
        let response=(response)=>{
          // debugger
          if(response.data.code===200){
            alert("注册成功");
            console.log(JSON.stringify(response.data));
            /*router.push({ // 注册成功跳转登录界面
              path: "/login"
            });*/
          } else {
            alert(response.data.message);
            console.log(JSON.stringify(response.data));
            router.push({
              path: "/register"
            });
          }
        }
        utils.axiosMethod({
          method: "post",
          url:"/api/user/register",
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'zhangpeng' // 设置token 仅用于测试
          },
          data:JSON.stringify(this.registerData),
          callback:response
        })
      }
    }

  }
</script>

<style scoped>
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
