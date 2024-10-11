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
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.service.RoleService;
import com.hana.hana_spring.utils.Result;

@RestController
@CrossOrigin("*")
@Validate(auth = true)
public class RoleCtr {
    @Autowired
    private RoleService role_service;

    /**
     * 查询所有角色的接口
     * 
     * @return {id, name}
     * @throws JsonProcessingException
     */
    @GetMapping("roles")
    public Result get_all_role() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = role_service.get_all_role();

        String data = mapper.writeValueAsString(roles);

        return Result.success(data);
    }

    /**
     * 创建角色
     * 
     * @param entity {name}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping("roles")
    public Result add_role(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Role role = mapper.readValue(entity, Role.class);
        role_service.add_role(role);
        return Result.success();
    }

    /**
     * 修改角色的接口
     * @param id 要修改的角色id
     * @param entity {name}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PutMapping("roles/{id}")
    public Result upd_role(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Role role = mapper.readValue(entity, Role.class);
        role.setId(id);
        role_service.upd_role(role);
        return Result.success();
    }

    /**
     * 删除角色
     * @param id 要删除的角色id
     * @return
     */
    @DeleteMapping("roles/{id}")
    public Result del_role(@PathVariable Integer id) {
        role_service.del_role(id);
        return Result.success();
    }

}
