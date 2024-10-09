package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class LoginReq {
    private String account;
    private String pass;
}
