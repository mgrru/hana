package com.hana.hana_spring.web;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

@Validate
@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserCtr {
    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    /**
     * 管理员获取所有用户的接口
     * 
     * @return [{id, account, name, isBan, age, phone, email, role}]
     * @throws JsonProcessingException
     */
    @Validate(auth = true)
    @GetMapping("admin")
    public Result get_all_user() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<User> users = user_service.get_all_user();
        String data = mapper.writeValueAsString(users);

        return Result.success(data);
    }

    /**
     * 管理员封禁用户的接口
     * 
     * @param id 要封禁的用户id
     */
    @Validate(auth = true)
    @PutMapping("{id}/ban")
    public Result ban_user(@PathVariable Integer id) {
        user_service.ban_user(id);

        return Result.success();
    }

    /**
     * 管理员解封用户的接口
     * 
     * @param id 要解封的用户id
     */
    @Validate(auth = true)
    @PutMapping("{id}/unban")
    public Result unban_user(@PathVariable Integer id) {
        user_service.unban_user(id);

        return Result.success();
    }

    /**
     * 管理员修改用户角色的接口
     * 
     * @param id  要修改的用户id
     * @param rid 要修改的角色id
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Validate(auth = true)
    @PutMapping("{id}/role/{rid}")
    public Result upd_user_role(@PathVariable Integer id, @PathVariable Integer rid)
            throws JsonMappingException, JsonProcessingException {
        user_service.upd_role(id, rid);
        return Result.success();
    }

    /**
     * 用户获取账号信息的接口
     * 
     * @return {id, account, name, isBan, age, phone, email, role}
     * @throws JsonProcessingException
     */
    @GetMapping
    public Result get_user_by_id(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        User user = user_service.get_user_by_id(uid);
        String data = new ObjectMapper().writeValueAsString(user);
        return Result.success(data);
    }

    /**
     * 用户修改账号信息的接口
     * 
     * @param entity {name, age, phone, email} 只能修改这4个属性
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PutMapping
    public Result upd_user(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        UpdUserReq userReq = new ObjectMapper().readValue(entity, UpdUserReq.class);
        User user = userReq.toUser(uid);
        user_service.upd_user(user);
        return Result.success();
    }

    /**
     * 用户修改账号密码的接口
     * 
     * @param entity {pass, new_pass, code}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PutMapping("pass")
    public Result upd_user_pass(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        UpdPassReq passReq = new ObjectMapper().readValue(entity, UpdPassReq.class);

        String email = user_service.get_user_by_id(uid).getEmail();
        if (user_service.verify_code(email, passReq.getCode())) {
            user_service.upd_pass(uid, passReq.getPass(), passReq.getNewPass());
            return Result.success();
        } else {
            return Result.error();
        }
    }

    /**
     * 修改密码验证邮箱
     * @throws EmailException
     */
    @PostMapping("verify/email")
    public Result postMethodName(HttpServletRequest req) throws EmailException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        String email = user_service.get_user_by_id(uid).getEmail();

        if (email == null || email.isBlank() || email.isEmpty()) {
            return Result.noemail();
        }
        user_service.send_email(email);

        return Result.success();
    }

}
