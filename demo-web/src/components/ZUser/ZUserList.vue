<template> 
    <div style="padding:20px;" id="app">
    <div class="panel panel-primary">
      <div class="panel-heading">用户管理</div>
      <table class="table table-bordered table-striped text-center">
        <thead>
        <tr>
          <th>序号</th>
          <th>用户名</th>
          <th>手机号码</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for ="(user,index) in zUsers">
          <td>{{index+1}}</td>
          <td>{{user.username}}</td>
          <td>{{user.phoneNumber}}</td>
          <td><button v-on:click="remove(index)">remove</button></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="text"  id="username" v-model="user.username"/></td>
          <td><input type="text" id="phoneNumber"v-model="user.phoneNumber"/></td>
          <td><button @click="insert">insert</button></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ZUserList',

    data () {
      return {
        user: {'username': '', 'phoneNumber': ''},
        zUsers: [
          {'username': '张三', 'phoneNumber': '18292000000'},
          {'username': '李四', 'phoneNumber': '18292000000'},
          {'username': '王麻子', 'phoneNumber': '18292000000'}
        ]
      }
    },
    methods: {
      insert: function () {
        this.zUsers.push(this.user)
      },
      remove: function (index) {
        this.zUsers.splice(index, 1)
      }
    }
  }
</script>

<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
  tr,th{
    text-align:center;
  }
</style>
