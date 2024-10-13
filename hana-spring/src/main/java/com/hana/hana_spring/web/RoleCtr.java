package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin("*")
@Validate(auth = true)
public class RoleCtr {
    @Autowired
    private RoleService role_service;

    @Operation(summary = "查询所有角色")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))))
    @GetMapping("roles")
    public ResponseEntity<String> get_all_role() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Role> roles = role_service.get_all_role();

        String data = mapper.writeValueAsString(roles);

        return Result.success(data);
    }

    @Operation(summary = "创建角色")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Role.class)), description = "id属性不用")
    @PostMapping("roles")
    public ResponseEntity<String> add_role(@RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Role role = mapper.readValue(entity, Role.class);
        role_service.add_role(role);
        return Result.success();
    }

    @Operation(summary = "修改角色")
    @Parameters(@Parameter(name = "id", description = "要修改的角色id"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Role.class)), description = "id属性不用")
    @PutMapping("roles/{id}")
    public ResponseEntity<String> upd_role(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Role role = mapper.readValue(entity, Role.class);
        role.setId(id);
        role_service.upd_role(role);
        return Result.success();
    }

    @Operation(summary = "删除角色")
    @Parameters(@Parameter(name = "id", description = "要删除的角色id"))
    @DeleteMapping("roles/{id}")
    public ResponseEntity<String> del_role(@PathVariable Integer id) {
        role_service.del_role(id);
        return Result.success();
    }

}
