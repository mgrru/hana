
import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path:'/',
      redirect:'/home'
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('../views/HomeView.vue'),
    }
    ,
    {
      path: '/comment',
      name: 'Comment',
      component: () => import('../../../vue3demo1/src/views/CommentView.vue'),
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../components/Login.vue'),
    },
    {
      path: '/videoplayer',
      name: 'VideoPlayer',
      component: () => import('../components/VideoPlayer.vue'),
    },
    {
      path: '/aside',
      name: 'Aside',
      component: () => import('../components/Aside.vue'),
    },
    {
      path: '/videolist',
      name: 'VideoList',
      component:() =>  import('../views/VideoList.vue')
    },
    {
      path: '/video/:id',
      name: 'VideoView',
      component: () => import('../views/VideoView.vue'),  // 在videoview挂载VideoContainerView
      props: true,
      children: [
          {
              path: '',  // 空路径表示默认展示 VideoContainer
              name: 'VideoContainer',  // 给子路由一个独立的名称
              component: () => import('../views/VideoContainerView.vue')
          }
      ]
  },
  {
    path: '/video',  // 处理没有 id 的情况
    redirect: '/home',  // 重定向到首页或者一个默认页面
  }
  ]
})

export default router
