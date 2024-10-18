import request from "../utils/request";

export const fetchData = () => {
  return request({
    url: "./mock/table.json",
    method: "get",
  });
};

export const fetchUserData = () => {
  return request({
    url: "/users",
    method: "get",
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
