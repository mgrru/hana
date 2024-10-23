// // src/mock/mockData.js
// import Mock from "mockjs";

// // 生成随机弹幕数据
// function generateRandomDanmaku(count = 10) {
//   const danmakuList = [];
//   for (let i = 0; i < count; i++) {
//     danmakuList.push(
//       Mock.mock({
//         "id|+1": 1,
//         name: "@cname", // 随机生成弹幕发送者名字
//         content: "@csentence(5, 15)", // 弹幕内容为一句话
//         timestamp: '@datetime("yyyy-MM-dd HH:mm:ss")', // 随机时间戳
//         color: Mock.Random.color(), // 随机颜色
//       })
//     );
//   }
//   return danmakuList;
// }

// // 单独导出 generateRandomDanmaku
// export { generateRandomDanmaku };

// // Mock获取点赞数和是否已点赞---正则表达式不要写错了
// Mock.mock(/\/likes\/\d+/, "get", (options) => {
//   console.log("Mock拦截到GET请求:", options.url); // 输出请求的URL
//   return {
//     likes: Mock.Random.natural(0, 5000),
//     isLiked: false,
//   };
// });

// //点赞接口
// Mock.mock("/likes", "post", (options) => {
//   console.log("Mock拦截到POST请求，参数:", options.body); // 输出请求体
//   return {
//     message: "点赞成功！",
//   };
// });

// //取消点赞接口
// Mock.mock(/\/likes\/\d+/, "delete", (options) => {
//   console.log("Mock拦截到DELETE请求:", options.url); // 输出请求的URL
//   return {
//     message: "取消点赞成功！",
//   };
// });

// // Mock 获取收藏数和是否已收藏
// Mock.mock(/\/favorites\/\d+/, "get", () => {
//   return {
//     favorites: Mock.Random.natural(0, 5000),
//     isFavorited: false,
//   };
// });

// // Mock 收藏接口
// Mock.mock("/favorites", "post", () => {
//   return {
//     message: "收藏成功！",
//   };
// });

// // Mock 取消收藏接口
// Mock.mock(/\/favorites\/\d+/, "delete", () => {
//   return {
//     message: "取消收藏成功！",
//   };
// });

// // Mock 获取收藏数和是否已收藏
// Mock.mock(/\/shares\/\d+/, "get", () => {
//   return {
//     shares: Mock.Random.natural(0, 50),
//     isShared: false,
//   };
// });

// // Mock 分享接口
// Mock.mock("/shares", "post", () => {
//   return {
//     message: "分享成功！",
//   };
// });

// // Mock 取消分享接口
// Mock.mock(/\/shares\/\d+/, "delete", () => {
//   return {
//     message: "取消分享成功！",
//   };
// });

// const Random = Mock.Random;
// //Mock 轮播图接口
// Mock.mock("/api/carouselVideos", "get", {
//   "videos|5-7": [
//     {
//       "id|+1": 1,
//       cover: () =>
//         Random.image(
//           "800x400",
//           Random.color(),
//           "#FFF",
//           "jpg",
//           Random.word(2, 5)
//         ),
//     },
//   ],
// });

// //视频列表接口
// Mock.mock("/videoList", "get", {
//   "list|48": [
//     {
//       "id|+1": 1,
//       thumbnail: '@dataImage("200x100", "#50B347", "Video")', // 确保生成缩略图
//       title: "@ctitle(5, 15)", // 随机生成标题
//     },
//   ],
// });

// //视频数据接口
// Mock.mock(/\/videos\/\d+/, "get", {
//   videoSrc: new URL("@/assets/videos/barca1.mp4", import.meta.url).href, // 使用静态资源路径//vjs.zencdn.net/v/oceans.mp4
//   title: "@ctitle(10, 20)",
//   description: "@cparagraph(1, 3)",
//   "views|1000-100000": 1,
//   "likes|100-10000": 1,
//   "stars|100-5000": 1,
//   "danmaku|100-5000": 1,
//   publishDate: '@date("yyyy-MM-dd")',
//   "shares|10-500": 1,
// });

// // 注册接口
// Mock.mock("/api/register", "post", (options) => {
//   const body = JSON.parse(options.body);

//   // 可以在这里添加注册逻辑，比如验证账号是否已存在
//   // 返回成功的注册响应
//   return {
//     code: 200,
//     message: "注册成功",
//     data: {
//       account: body.account,
//       name: body.name,
//       age: body.age,
//       phone: body.phone,
//       email: body.email,
//     },
//   };
// });

// // 登录接口
// Mock.mock("/api/login", "post", (options) => {
//   const { account, pass } = JSON.parse(options.body);

//   // 模拟用户登录信息
//   const mockUser = {
//     id: Mock.Random.guid(), // 新增用户ID
//     account: "tzt",
//     pass: "111111", // 这里可以设置一个模拟密码
//     token: Mock.Random.guid(), // 模拟token
//     userInfo: {
//       avatar:
//         "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
//       userName: "tang",
//       age: Mock.Random.natural(0, 100),
//       phone: Mock.Random.string("number", 11),
//       email: Mock.Random.email(),
//     },
//   };

//   if (account === mockUser.account && pass === mockUser.pass) {
//     return {
//       success: true,
//       code: 200,
//       message: "登录成功",
//       token: mockUser.token,
//       userInfo: mockUser.userInfo, // 将 avatar 放在 userInfo 中返回
//     };
//   } else {
//     return {
//       success: false,
//       message: "用户名或密码错误",
//     };
//   }
// });

// // 模拟评论数据
// let comments = Mock.mock({
//   "data|10-30": [
//     {
//       id: "@id",
//       content: "@csentence(5, 10)",
//       username: "@cname",
//       createdAt: "@datetime",
//       likes: "@integer(0, 1000)",
//       liked: false, // 是否点赞
//       isOwner: "@boolean", // 是否是评论人自己
//       isPublisher: "@boolean", // 是否是视频发布者
//       "replies|0-5": [
//         {
//           id: "@id",
//           content: "@csentence(3, 8)",
//           username: "@cname",
//           createdAt: "@datetime",
//           likes: "@integer(0, 5000)",
//           liked: false,
//           "replies|0-3": [], // 可以递归生成子评论
//         },
//       ],
//     },
//   ],
// }).data;

// // 获取评论接口
// Mock.mock(/\/comments\/\d+/, "get", (options) => {
//   const rid = options.url.split("/").pop(); // 从URL中获取 rid
//   console.log("Mock拦截到GET请求:", options.url, "rid:", rid);
//   // 根据 rid 返回不同的数据，这里简化为返回相同的数据
//   return {
//     success: true,
//     data: comments,
//   };
// });

// // 发送评论接口
// Mock.mock("/comments", "post", (options) => {
//   const { content, rid } = JSON.parse(options.body);
//   const newComment = {
//     id: Mock.Random.id(),
//     content,
//     username: "CurrentUser",
//     createdAt: new Date().toLocaleString(),
//     likes: 0,
//     liked: false,
//     isOwner: true,
//     isPublisher: false,
//     replies: [],
//     showReplies: false,
//     showReplyInput: false,
//   };
//   comments.push(newComment);
//   return { success: true };
// });

// // 删除评论接口
// Mock.mock(/\/comments\/\d+/, "delete", (options) => {
//   const id = options.url.split("/").pop();
//   comments = comments.filter((comment) => comment.id !== id);
//   return { success: true };
// });
