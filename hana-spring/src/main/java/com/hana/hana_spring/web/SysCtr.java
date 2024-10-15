package com.hana.hana_spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.hana.hana_spring.utils.HashUtil;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin("*")
public class SysCtr {
    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    @Autowired
    private HashUtil hash_util;

    @Operation(summary = "用户注册的接口")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = LoginReq.class)))
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        LoginReq register = new ObjectMapper().readValue(entity, LoginReq.class);
        if (user_service.get_user_by_account(register.getAccount()) != null) {
            return Result.error();
        }
        // 设置默认值
        User user = new User();
        user.setAccount(register.getAccount());
        user.setPass(hash_util.hash(register.getPass()));
        user.setName("新用户");
        user.setBan(false);
        user.setRole(new Role(2, null));

        user_service.add_user(user);
        return Result.success();
    }

    @Operation(summary = "用户和管理员登录的接口")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = LoginReq.class)))
    @ApiResponse(description = "直接返回token字符串")
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        LoginReq login = new ObjectMapper().readValue(entity, LoginReq.class);
        User user = user_service.get_user_by_account(login.getAccount());
        Role role = user.getRole();
        if (user != null && user.getPass().equals(hash_util.hash(login.getPass()))) {
            if (role.getName().equals("管理员")) {
                return Result.success(jwt_util.generateToken(user.getId().toString(), true));
            } else {
                return Result.success(jwt_util.generateToken(user.getId().toString(), false));
            }
        } else {
            return Result.no_auth();
        }
    }

}
