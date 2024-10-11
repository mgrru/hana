import request from '../utils/request';

export const fetchData = () => {
    return request({
        url: './mock/table.json',
        method: 'get'
    });
};

export const fetchUserData = () => {
    return request({
        url: 'http://localhost:9901/users',
        // ./mock/user.json
        method: 'get'
    });
};

export const fetchRoleData = () => {
    return request({
        url: 'http://localhost:9901/roles',
        // url: './mock/role.json',
        method: 'get'
    });
};

export const fetchAnimesData = () => {
    return request({
        url: 'http://localhost:9901/animes',
        method: 'get'
    });
};

export const fetchAnnouncementsData = () => {
    return request({
        url: 'http://localhost:9901/announcements',
        method: 'get'
    });
};
