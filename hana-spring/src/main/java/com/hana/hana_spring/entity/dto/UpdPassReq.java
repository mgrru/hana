package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Component
@Data
public class UpdPassReq {
    private String pass;
    @JsonAlias("new_pass")
    private String newPass;
    private String code;
}
