import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    }
    ,
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/components/Register')
    }
    ,
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/components/Login')
    }
    ,
    {
      path: '/home',
      name: 'Home',
      component: () => import('@/components/ZUser/Home')
    }
    ,
    {
      path: '/zUserList',
      name: 'ZUserList',
      component: () => import('@/components/ZUser/ZUserList')
    }
  ]
})
