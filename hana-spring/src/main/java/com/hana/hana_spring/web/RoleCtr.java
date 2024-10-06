package com.hana.hana_spring.web;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.service.RoleService;
import com.hana.hana_spring.utils.Result;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
public class RoleCtr {
    @Autowired
    private RoleService role_service;

    @GetMapping("hello")
    public Result hello() {
        List<Role> roles = role_service.get_all_role();
        log.info("roles:" + roles);
        return Result.success("hello");
    }

    @GetMapping("roles")
    public Result get_roles() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = role_service.get_all_role();
        log.info("roles:" + roles);

        String data = mapper.writeValueAsString(roles);

        log.info("data:" + data);
        return Result.success(data);
    }

}
