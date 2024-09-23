// src/mock/mockData.js
import Mock from 'mockjs';

// 模拟评论数据
let comments = Mock.mock({
  'data|10': [{
    id: '@id',
    content: '@sentence(5, 10)',
    username: '@name',
    createdAt: '@datetime',
    likes: '@integer(0, 100)',
    liked: false,  // 是否点赞
    isOwner: '@boolean',  // 是否是评论人自己
    isPublisher: '@boolean',  // 是否是视频发布者
    'replies|0-5': [{
      id: '@id',
      content: '@sentence(3, 8)',
      username: '@name',
      createdAt: '@datetime',
      likes: '@integer(0, 50)',
      liked: false,
      'replies|0-3': [] // 可以递归生成子评论
    }]
  }]
});

// 生成随机弹幕数据
function generateRandomDanmaku(count = 10) {
  const danmakuList = [];
  for (let i = 0; i < count; i++) {
    danmakuList.push(Mock.mock({
      'id|+1': 1,
      'name': '@name',  // 随机生成弹幕发送者名字
      'content': '@sentence(5, 15)',  // 弹幕内容为一句话
      'timestamp': '@datetime("yyyy-MM-dd HH:mm:ss")',  // 随机时间戳
      'color': Mock.Random.color()  // 随机颜色
    }));
  }
  return danmakuList;
}

export default {
  getComments() {
    return {
      data: comments.data
    };
  },
  // 发送评论接口
  addComment(content, rid) {
    const newComment = {
      id: Mock.Random.id(),
      content,
      username: 'CurrentUser',
      createdAt: new Date().toLocaleString(),
      likes: 0,
      liked: false,
      isOwner: true,
      isPublisher: false,
      replies: [],
      showReplies: false,
      showReplyInput: false
    };
    comments.data.push(newComment);
    return { success: true };
  },
  // 删除评论接口
  deleteComment(id) {
    comments.data = comments.data.filter(comment => comment.id !== id);
    return { success: true };
  },
};


// 单独导出 generateRandomDanmaku 
export { generateRandomDanmaku };

// Mock获取点赞数和是否已点赞
//正则表达式b不要写错了
Mock.mock(/\/likes\/\d+/, "get", (options) => {
  console.log('Mock拦截到GET请求:', options.url);  // 输出请求的URL
  return {
    likes: 100,
    isLiked: false,
  };
});

//点赞接口
Mock.mock("/likes", "post", (options) => {
  console.log('Mock拦截到POST请求，参数:', options.body);  // 输出请求体
  return {
    message: "点赞成功！",
  };
});

//取消点赞接口
Mock.mock(/\/likes\/\d+/, "delete", (options) => {
  console.log('Mock拦截到DELETE请求:', options.url);  // 输出请求的URL
  return {
    message: "取消点赞成功！",
  };
});

// Mock 获取收藏数和是否已收藏
Mock.mock(/\/favorites\/\d+/, "get", () => {
  return {
      favorites: 50,
      isFavorited: false,
  };
});

// Mock 收藏接口
Mock.mock("/favorites", "post", () => {
  return {
      message: "收藏成功！",
  };
});

// Mock 取消收藏接口
Mock.mock(/\/favorites\/\d+/, "delete", () => {
  return {
      message: "取消收藏成功！",
  };
});

// Mock 获取收藏数和是否已收藏
Mock.mock(/\/shares\/\d+/, "get", () => {
  return {
      shares: 20,
      isShared: false,
  };
});

// Mock 分享接口
Mock.mock("/shares", "post", () => {
  return {
      message: "分享成功！",
  };
});

// Mock 取消分享接口
Mock.mock(/\/shares\/\d+/, "delete", () => {
  return {
      message: "取消分享成功！",
  };
});


const Random = Mock.Random;
//Mock 轮播图接口
Mock.mock('/api/carouselVideos', 'get', {
  'videos|5-7': [
    {
      'id|+1': 1,
      'title': () => Random.ctitle(5, 10),
      'cover': () => Random.image('800x400', Random.color(), '#FFF', 'jpg', Random.word(2, 5)),
      'description': () => Random.csentence(10, 20),
      'views|1000-10000': 1
    }
  ]
});
