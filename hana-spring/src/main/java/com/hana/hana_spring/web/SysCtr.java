package com.hana.hana_spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.entity.dto.LoginReq;
import com.hana.hana_spring.service.UserService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

@RestController
@CrossOrigin("*")
public class SysCtr {
    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    /**
     * 用户注册的接口
     * 
     * @param entity {account,pass}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping("register")
    public Result register(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        LoginReq register = new ObjectMapper().readValue(entity, LoginReq.class);
        if (user_service.get_user_by_account(register.getAccount()) != null) {
            return Result.error();
        }
        // 设置默认值
        User user = new User();
        user.setAccount(register.getAccount());
        user.setPass(register.getPass());
        user.setName("新用户");
        user.setBan(false);
        user.setRole(new Role(2, null));

        user_service.add_user(user);
        return Result.success();
    }

    /**
     * 用户和管理员登录的接口
     * 
     * @param entity {account, pass}
     * @return 直接返回token字符串
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping("login")
    public Result login(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        LoginReq login = new ObjectMapper().readValue(entity, LoginReq.class);
        User user = user_service.get_user_by_account(login.getAccount());
        Role role = user.getRole();
        if (user != null && user.getPass().equals(login.getPass())) {
            if (role.getName().equals("管理员")) {
                return Result.success(jwt_util.generateToken(user.getId().toString(), true));
            } else {
                return Result.success(jwt_util.generateToken(user.getId().toString(), false));
            }
        } else {
            return Result.noauth();
        }
    }

}
