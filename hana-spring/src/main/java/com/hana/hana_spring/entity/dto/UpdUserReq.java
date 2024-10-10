package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hana.hana_spring.entity.User;

import lombok.Data;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdUserReq {
    private String name;
    private Integer age;
    private String phone;
    private String email;

    public User toUser(Integer uid) {
        User user = new User();
        user.setId(uid);
        user.setName(name);
        user.setAge(age);
        user.setPhone(phone);
        user.setEmail(email);
        return user;
    }
}
