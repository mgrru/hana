package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.UserMapper;
import com.hana.hana_spring.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private UserMapper user_mapper;

    public List<User> get_all_user() {
        return user_mapper.sel_all();
    }

    public void add_user(User user) {
        if (user != null) {
            user_mapper.ins(user);
        }
    }

    public void del_user(int id) {
        user_mapper.del(id);
    }

    public void upd_user(User user) {
        if (user != null) {
            user_mapper.upd(user);
        }
    }
}
