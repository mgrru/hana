package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.entity.dto.RoleReq;
import com.hana.hana_spring.service.RoleService;
import com.hana.hana_spring.utils.Result;

@RestController
@CrossOrigin("*")
public class RoleCtr {
    @Autowired
    private RoleService role_service;

    @GetMapping("roles")
    public Result get_all_role() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = role_service.get_all_role();

        String data = mapper.writeValueAsString(roles);

        return Result.success(data);
    }

    @PostMapping("roles")
    public Result add_role(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RoleReq role = mapper.readValue(entity, RoleReq.class);
        role_service.add_role(role.toRole());
        return Result.success();
    }

    @PutMapping("roles/{id}")
    public Result upd_role(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RoleReq role = mapper.readValue(entity, RoleReq.class);
        role.setId(id);
        role_service.upd_role(role.toRole());
        return Result.success();
    }

    @DeleteMapping("roles/{id}")
    public Result del_role(@PathVariable Integer id) {
        role_service.del_role(id);
        return Result.success();
    }

    @GetMapping("permissions")
    public Result get_all_permissions() throws JsonProcessingException {
        List<Auth> auths = role_service.get_all_auth();
        String data = new ObjectMapper().writeValueAsString(auths);
        return Result.success(data);
    }

}
