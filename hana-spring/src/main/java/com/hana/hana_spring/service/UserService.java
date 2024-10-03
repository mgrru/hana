package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.UserDao;
import com.hana.hana_spring.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private UserDao user_dao;

    public List<User> get_all_user() {
        return user_dao.sel_all();
    }

    public void add_user(User user) {
        user_dao.ins(user);
    }
}
