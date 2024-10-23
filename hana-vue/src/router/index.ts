import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "../store/userStore";
import { useAuthStore } from "../store/auth";
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
      path: "/upload",
      name: "Upload",
      component: () => import("../views/UploudView.vue"),
    },
    {
      path: "/msg",
      name: "Msg",
      component: () => import("../components/Msg.vue"),
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
      path: "/commentList",
      name: "CommentList",
      component: () => import("../components/CommentList.vue"),
    },
    {
      path: "/searchList",
      name: "SearchList",
      component: () => import("../views/SearchView.vue"),
    },
    {
      path: "/animes/:name",
      name: "AnimeView",
      component: () => import("../views/AnimeView.vue"),
      props: true,
      children: [
        {
          path: "", // 空路径表示默认展示 AnimeContainer
          name: "AnimeContainer", // 给子路由一个独立的名称
          component: () => import("../views/AnimeContainerView.vue"),
          props: (route) => ({
            name: route.params.name,
          }),
        },
      ],
    },
    {
      path: "/animes", // 处理没有 name 的情况
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
    {
      path: "/section",
      name: "Section",
      component: () => import("../components/Section.vue"),
    },
    {
      path: "/episode",
      name: "Episode",
      component: () => import("../components/Episode.vue"),
    },
    {
      path: "/reset-pwd",
      name: "Reset",
      component: () => import("../views/ResetView.vue"),
    },
  ],
});

// 控制访问权限，可以使用 Vue Router 的导航守卫来检查用户是否已登录

// router.beforeEach((to, from, next) => {
//   const authStore = useAuthStore();

//   // 如果没有token并且不是访问登录页，重定向到登录页
//   if (!authStore.token && to.path !== '/login') {
//     next('/login');
//   } else {
//     next();
//   }
// });

export default router;
