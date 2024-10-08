package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.service.RoleService;
import com.hana.hana_spring.utils.Result;

@RestController
@RequestMapping("roles")
@CrossOrigin("*")
public class RoleCtr {
    @Autowired
    private RoleService role_service;

    @GetMapping
    public Result get_roles() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = role_service.get_all_role();

        String data = mapper.writeValueAsString(roles);

        return Result.success(data);
    }

}
