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

    public void ban_user(Integer id) {
        if (id != null) {
            User user = new User();
            user.setId(id);
            user.setBan(true);
            user_mapper.ban(user);
        }
    }

    public void unban_user(Integer id) {
        if (id != null) {
            User user = new User();
            user.setId(id);
            user.setBan(false);
            user_mapper.ban(user);
        }
    }

    public void upd_role(Integer id, Integer rid) {
        if (id != null && rid != null) {
            user_mapper.upd_role(id, rid);
        }
    }

    public User get_user_by_id(Integer id) {
        if (id != null) {
            return user_mapper.sel_by_id(id);
        } else {
            return null;
        }
    }

    public User get_user_by_account(String account) {
        if (account != null) {
            return user_mapper.sel_by_account(account);
        } else {
            return null;
        }
    }

    public void upd_pass(Integer uid, String pass, String new_pass) {
        User user = user_mapper.sel_by_id(uid);
        if (user.getPass().equals(pass) && pass != new_pass) {
            user.setPass(new_pass);
            user_mapper.upd_pass(user);
        }
    }

    public void upd_user(User user) {
        if (user == null) {
            return;
        }

        if (user.getName() != null) {
            user_mapper.upd_name(user);
        }
        if (user.getAge() != null) {
            user_mapper.upd_age(user);
        }
        if (user.getPhone() != null) {
            user_mapper.upd_phone(user);
        }
        if (user.getEmail() != null) {
            user_mapper.upd_email(user);
        }

    }
}
