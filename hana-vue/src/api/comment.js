// src/api/comment.js
import mockData from '../mock/mockData.js';

export const fetchComments = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(mockData.getComments());
    }, 500);
  });
};

// 发送评论接口
export const sendComment = (content, rid) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(mockData.addComment(content, rid));
    }, 500);
  });
};

// 删除评论接口
export const deleteComment = (id) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(mockData.deleteComment(id));
    }, 500);
  });
};
