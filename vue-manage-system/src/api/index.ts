import request from "../utils/request";

export const fetchData = () => {
  return request({
    url: "./mock/table.json",
    method: "get",
  });
};

export const fetchUserData = () => {
  return request({
    url: "/users/admin",
    method: "get",
  });
};

export const fetchUser = () => {
  return request({
    url: "/users",
    method: "get",
  });
};
// 解封用户接口
export const unbanUser = (id: number) => {
  return request({
    url: `/users/${id}/unban`, ///
    method: "put", // 使用 PUT 方法
  });
};

// 禁用用户接口
export const banUser = (id: number) => {
  return request({
    url: `/users/${id}/ban`, //
    method: "put", // 使用 PUT 方法
  });
};

// 修改用户角色接口
export const updateUserRole = (id: number, rid: number) => {
  return request({
    url: `/users/${id}/role/${rid}`, //
    method: "put", // 使用 PUT 方法
  });
};

// 删除角色
export const deleteUser = (id: number) => {
  return request({
    url: `/roles/${id}`, // 删除公告的接口，带上公告ID
    method: "delete", // 使用 DELETE 方法
  });
};

export const fetchRoleData = () => {
  return request({
    url: "/roles",
    method: "get",
  });
};

// 添加角色
export const addRole = (RoleData: { name: string }) => {
  return request({
    url: "/roles",
    method: "post",
    data: RoleData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 修改角色
export const updateRole = (id: number, roleData: { name: string }) => {
  return request({
    url: `/roles/${id}`, // 使用公告ID更新
    method: "put", // 使用 PUT 方法
    data: roleData, // 发送公告数据
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 删除角色
export const deleteRole = (id: number) => {
  return request({
    url: `/roles/${id}`, // 删除公告的接口，带上公告ID
    method: "delete", // 使用 DELETE 方法
  });
};

export const fetchAnimeData = () => {
  return request({
    url: "/animes/all",
    method: "get",
  });
};

// 动漫通过审核接口
export const approveAnime = (id: number) => {
  return request({
    url: `/approve/${id}`, //
    method: "put", // 使用 PUT 方法
  });
};

// 动漫不通过审核接口
export const rejectAnime = (id: number) => {
  return request({
    url: `/reject/${id}`, //
    method: "put", // 使用 PUT 方法
  });
};

// 下架动漫
export const deactivateAnime = (id: number) => {
  return request({
    url: `/anime/${id}`, // 删除公告的接口，带上公告ID
    method: "delete", // 使用 DELETE 方法
  });
};

// 模糊搜索
export const fetchSearchData = (queryName: string) => {
  return request({
    url: "/search",
    method: "get",
    params: {
      name: queryName, // 传递 'name' 参数给后端
    },
  });
};

//获取公告信息
export const fetchAnnouncementsData = () => {
  return request({
    url: "/announcements",
    method: "get",
  });
};

// 添加公告
export const addAnnouncement = (announcementData: {
  title: string;
  content: string;
  time: string;
}) => {
  return request({
    url: "/announcements",
    method: "post",
    data: announcementData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 修改公告
export const updateAnnouncement = (
  id: number,
  announcementData: { title: string; content: string; time: string }
) => {
  return request({
    url: `/announcements/${id}`, // 使用公告ID更新
    method: "put", // 使用 PUT 方法
    data: announcementData, // 发送公告数据
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 删除公告
export const deleteAnnouncement = (id: number) => {
  return request({
    url: `/announcements/${id}`, // 删除公告的接口，带上公告ID
    method: "delete", // 使用 DELETE 方法
  });
};

//获取板块信息
export const fetchSectionsData = () => {
  return request({
    url: "/sections",
    method: "get",
  });
};

// 添加板块
export const addSection = (sectionData: { name: string }) => {
  return request({
    url: "/sections",
    method: "post",
    data: sectionData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 修改板块
export const updateSectiont = (id: number, sectionData: { name: string }) => {
  return request({
    url: `/sections/${id}`, // 使用公告ID更新
    method: "put", // 使用 PUT 方法
    data: sectionData, // 发送公告数据
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 删除板块
export const deleteSection = (id: number) => {
  return request({
    url: `/sections/${id}`, // 删除公告的接口，带上公告ID
    method: "delete", // 使用 DELETE 方法
  });
};

// 登录
export const loginRequest = (formData: { account: string; pass: string }) => {
  return request({
    url: "/login",
    method: "post",
    data: formData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

//修改密码验证码请求
export const verifyEmailRequest = (codeData: { email: string }) => {
  return request({
    url: "/users/verify/email",
    method: "post",
    data: codeData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 注册
export const registerRequest = (formData: {
  account: string;
  pass: string;
}) => {
  return request({
    url: "/register",
    method: "post",
    data: formData,
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};

// 修改密码
export const resetPass = (resetData: {
  pass: string;
  new_pass: string;
  code: string;
}) => {
  return request({
    url: `/users/pass`, // 使用公告ID更新
    method: "put", // 使用 PUT 方法
    data: resetData, // 发送公告数据
    headers: {
      "Content-Type": "application/json", // 设置为 JSON 格式
    },
  });
};
