package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Msg;
import com.hana.hana_spring.entity.dto.MsgReq;
import com.hana.hana_spring.service.MsgService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("msg")
@CrossOrigin("*")
@Validate
public class MsgCtr {
    @Autowired
    private MsgService msg_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "获取消息", description = "获取登录者的消息，包括管理员和用户")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Msg.class))))
    @GetMapping
    public ResponseEntity<String> get_msg(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Msg> msgs = msg_service.get_msg(uid);
        String data = new ObjectMapper().writeValueAsString(msgs);
        return Result.success(data);
    }

    @Operation(summary = "发送消息")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = MsgReq.class)))
    @PostMapping
    public ResponseEntity<String> send_msg(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        Msg msg = new ObjectMapper().readValue(entity, MsgReq.class).toMsg(uid);
        msg_service.send_msg(msg);
        return Result.success();
    }
}
