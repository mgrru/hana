package com.hana.hana_spring.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.UserMapper;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.utils.EmailSender;
import com.hana.hana_spring.utils.EncryUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private UserMapper user_mapper;

    @Autowired
    private RedisTemplate<String, String> redis;

    @Autowired
    private EmailSender email_sender;

    @Autowired
    private EncryUtil encry_util;

    public List<User> get_all_user() throws Exception {
        List<User> users = user_mapper.sel_all();
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                user.setPhone(encry_util.decrypt(user.getPhone()));
                user.setEmail(encry_util.decrypt(user.getEmail()));
            }
            return users;
        } else {
            throw new Exception();
        }
    }

    public void add_user(User user) throws Exception {
        if (user != null) {
            user.setPass(encry_util.hash(user.getPass()));
            user.setPhone(encry_util.encrypt(user.getPhone()));
            user.setEmail(encry_util.encrypt(user.getEmail()));
            user_mapper.ins(user);
        } else {
            throw new Exception();
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

    public User get_user_by_id(Integer id) throws Exception {
        if (id != null) {
            User user = user_mapper.sel_by_id(id);
            user.setPhone(encry_util.decrypt(user.getPhone()));
            user.setEmail(encry_util.decrypt(user.getEmail()));
            return user;
        } else {
            throw new Exception();
        }
    }

    public User get_user_by_account(String account) throws Exception {
        if (account != null) {
            User user = user_mapper.sel_by_account(account);
            if (user == null) {
                return null;
            }
            user.setPhone(encry_util.decrypt(user.getPhone()));
            user.setEmail(encry_util.decrypt(user.getEmail()));
            return user;
        } else {
            throw new Exception();
        }
    }

    public String send_email(String to) throws EmailException {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        redis.opsForValue().set(to, code.toString(), 5, TimeUnit.MINUTES);
        email_sender.send_email(to, "验证码有效期5分钟，您的验证码为：" + code.toString());
        return code.toString();
    }

    public boolean verify_code(String email, String input) {
        String code = redis.opsForValue().get(email);
        if (code.equals(input)) {
            redis.delete(email);
            return true;
        }
        return false;
    }

    public void upd_pass(Integer uid, String pass, String new_pass) throws Exception {
        User user = user_mapper.sel_by_id(uid);
        String hash_pass = encry_util.hash(pass);
        String hash_new_pass = encry_util.hash(new_pass);
        if (user.getPass().equals(hash_pass) && !hash_pass.equals(hash_new_pass)) {
            user.setPass(hash_new_pass);
            user_mapper.upd_pass(user);
        }
    }

    public boolean verify_pass(String pass, String login_pass) throws Exception {
        if (pass.equals(encry_util.hash(login_pass))) {
            return true;
        } else {
            return false;
        }
    }

    public void upd_user(User user) throws Exception {
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
            user.setPhone(encry_util.encrypt(user.getPhone()));
            user_mapper.upd_phone(user);
        }
        if (user.getEmail() != null) {
            user.setEmail(encry_util.decrypt(user.getEmail()));
            user_mapper.upd_email(user);
        }

    }
}
