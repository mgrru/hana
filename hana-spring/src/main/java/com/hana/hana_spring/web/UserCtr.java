package com.hana.hana_spring.web;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.entity.dto.UpdPassReq;
import com.hana.hana_spring.entity.dto.UpdUserReq;
import com.hana.hana_spring.service.UserService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;
import com.hana.hana_spring.utils.exception.NewPassException;
import com.hana.hana_spring.utils.exception.PassException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validate
@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserCtr {
    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "管理员获取所有用户")
    @SecurityRequirement(name = "jwt")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))
    @Validate(auth = true)
    @GetMapping("admin")
    public ResponseEntity<String> get_all_user() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<User> users = user_service.get_all_user();
        String data = mapper.writeValueAsString(users);

        return Result.success(data);
    }

    @Operation(summary = "管理员封禁用户的接口")
    @SecurityRequirement(name = "jwt")
    @Parameters({ @Parameter(name = "id", description = "要封禁的用户id") })
    @Validate(auth = true)
    @PutMapping("{id}/ban")
    public ResponseEntity<String> ban_user(@PathVariable Integer id) {
        user_service.ban_user(id);

        return Result.success();
    }

    @Operation(summary = "管理员解封用户的接口")
    @SecurityRequirement(name = "jwt")
    @Parameters({ @Parameter(name = "id", description = "要解封的用户id") })
    @Validate(auth = true)
    @PutMapping("{id}/unban")
    public ResponseEntity<String> unban_user(@PathVariable Integer id) {
        user_service.unban_user(id);

        return Result.success();
    }

    @Operation(summary = "管理员修改用户角色的接口")
    @SecurityRequirement(name = "jwt")
    @Parameters({
            @Parameter(name = "id", description = "要修改的用户id"),
            @Parameter(name = "rid", description = "要修改的角色id")
    })
    @Validate(auth = true)
    @PutMapping("{id}/role/{rid}")
    public ResponseEntity<String> upd_user_role(@PathVariable Integer id, @PathVariable Integer rid)
            throws JsonMappingException, JsonProcessingException {
        user_service.upd_role(id, rid);
        return Result.success();
    }

    @Operation(summary = "用户获取账号信息的接口")
    @SecurityRequirement(name = "jwt")
    @ApiResponse(content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    public ResponseEntity<String> get_user_by_id(HttpServletRequest req)
            throws Exception {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        User user = user_service.get_user_by_id(uid);
        String data = new ObjectMapper().writeValueAsString(user);
        return Result.success(data);
    }

    @Operation(summary = "用户修改账号信息", description = "四个属性可任选一或多个")
    @SecurityRequirement(name = "jwt")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = UpdUserReq.class)))
    @PutMapping
    public ResponseEntity<String> upd_user(@RequestBody String entity, HttpServletRequest req)
            throws Exception {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        UpdUserReq user_req = new ObjectMapper().readValue(entity, UpdUserReq.class);
        User user = user_req.toUser(uid);
        user_service.upd_user(user);
        return Result.success();
    }

    @Operation(summary = "用户修改账号密码")
    @SecurityRequirement(name = "jwt")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = UpdPassReq.class)))
    @PutMapping("pass")
    public ResponseEntity<String> upd_user_pass(@RequestBody String entity, HttpServletRequest req)
            throws Exception {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        UpdPassReq pass_req = new ObjectMapper().readValue(entity, UpdPassReq.class);

        String email = user_service.get_user_by_id(uid).getEmail();

        try {
            user_service.verify_code(email, pass_req.getCode());
        } catch (Exception e) {
            return Result.code_err();
        }
        
        try {
            user_service.upd_pass(uid, pass_req.getPass(), pass_req.getNewPass());
        } catch (PassException e) {
            return Result.pass_err();
        } catch (NewPassException e) {
            return Result.new_pass_err();
        } catch (Exception e) {
            return Result.exception();
        }
        return Result.success();
    }

    @Operation(summary = "发送邮箱验证码")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schemaProperties = {
            @SchemaProperty(name = "email", schema = @Schema(type = "string", name = "email"))
    }))
    @Validate(login = false)
    @PostMapping("verify/email")
    public ResponseEntity<String> send_email(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String email = new ObjectMapper().readTree(entity).get("email").asText();
        if (email == null || email.isBlank()) {
            log.error("get noemail");
            return Result.no_email();
        }

        try {
            user_service.send_email(email);
        } catch (EmailException e) {
            log.error("email err!");
            return Result.no_email();
        }

        return Result.success();
    }

}
