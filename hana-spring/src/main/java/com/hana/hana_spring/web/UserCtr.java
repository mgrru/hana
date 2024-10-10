package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.service.UserService;
import com.hana.hana_spring.utils.Result;

@LoginValidate
@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserCtr {
    @Autowired
    private UserService user_service;

    @GetMapping
    public Result get_all_user() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<User> users = user_service.get_all_user();
        String data = mapper.writeValueAsString(users);

        return Result.success(data);
    }

    @PutMapping("{id}/ban")
    public Result ban_user(@PathVariable Integer id) {
        user_service.ban_user(id);

        return Result.success();
    }

    @PutMapping("{id}/unban")
    public Result unban_user(@PathVariable Integer id) {
        user_service.unban_user(id);

        return Result.success();
    }

    @PutMapping("{id}/role/{rid}")
    public Result unban_user(@PathVariable Integer id, @PathVariable Integer rid)
            throws JsonMappingException, JsonProcessingException {
        user_service.upd_role(id, rid);
        return Result.success();
    }

    @GetMapping("{id}")
    public Result get_user_by_id(@PathVariable Integer id) throws JsonProcessingException {
        User user = user_service.get_user_by_id(id);
        String data = new ObjectMapper().writeValueAsString(user);
        return Result.success(data);
    }

    @PutMapping("{id}")
    public Result upd_user(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        User user = new ObjectMapper().readValue(entity, User.class);
        user_service.upd_user(user);
        return Result.success();
    }

}
