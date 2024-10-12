import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "../store/userStore";
import { useAuthStore } from '../store/auth';
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/home",
    },
    {
      path: "/home",
      name: "Home",
      component: () => import("../views/HomeView.vue"),
    },
    {
      path: "/userinfo",
      name: "UserInfo",
      component: () => import("../views/UserInfoView.vue"),
    },
    {
      path: "/login",
      name: "Login",
      component: () => import("../views/LoginView.vue"),
    },
    {
      path: "/videoplayer",
      name: "VideoPlayer",
      component: () => import("../components/VideoPlayer.vue"),
    },
    {
      path: "/aside",
      name: "Aside",
      component: () => import("../components/Aside.vue"),
    },
    {
      path: "/animeList",
      name: "AnimeList",
      component: () => import("../views/AnimeList.vue"),
    },
    {
      path: "/video/:id",
      name: "VideoView",
      component: () => import("../views/VideoView.vue"), // 在videoview挂载VideoContainerView
      props: true,
      children: [
        {
          path: "", // 空路径表示默认展示 VideoContainer
          name: "VideoContainer", // 给子路由一个独立的名称
          component: () => import("../views/VideoContainerView.vue"),
          props: route => ({ videoId: route.params.id }), // 直接传递 videoId 作为 props
        },
      ],
    },
    {
      path: "/video", // 处理没有 id 的情况
      redirect: "/home", // 重定向到首页或者一个默认页面
    },
    {
      path: "/history",
      name: "History",
      component: () => import("../views/HistoryView.vue"),
    },
    {
      path: "/favorite",
      name: "Favorite",
      component: () => import("../views/FavoriteView.vue"),
    },
  ],
});

// 控制访问权限，可以使用 Vue Router 的导航守卫来检查用户是否已登录
// router.beforeEach((to, from, next) => {
//   const userStore = useUserStore();
//   userStore.initializeUser(); // 确保用户状态是最新的

//   // 需要登录的路由
//   if (to.meta.requiresAuth && !userStore.isLoggedIn) {
//       next({ name: 'Login' }); // 跳转到登录页面
//   } else {
//       next(); // 允许访问
//   }
// });
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  
  // 如果没有token并且不是访问登录页，重定向到登录页
  if (!authStore.token && to.path !== '/login') {
    next('/login');
  } else {
    next();
  }
});

export default router;
