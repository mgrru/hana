package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Component
@Data
public class UpdPassReq {
    @Schema(description = "旧密码")
    private String pass;
    @JsonAlias("new_pass")
    @Schema(description = "新密码")
    private String newPass;
    @Schema(description = "验证码")
    private String code;
}
