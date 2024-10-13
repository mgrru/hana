package com.hana.hana_spring.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Danmaku;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.entity.dto.DanmakuRep;
import com.hana.hana_spring.entity.dto.DanmakuReq;
import com.hana.hana_spring.service.DanmakuService;
import com.hana.hana_spring.service.UserService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("danmus")
@CrossOrigin("*")
@Validate
public class DanmakuCtr {
    @Autowired
    private DanmakuService danmaku_service;

    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "获取动漫的弹幕")
    @Parameters(@Parameter(name = "rid", description = "动漫id"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = DanmakuRep.class))))
    @Validate(login = false)
    @GetMapping("{rid}")
    public ResponseEntity<String> get_danmaku(@PathVariable Integer rid) throws JsonProcessingException {
        List<Danmaku> danmus = danmaku_service.get_danmaku(rid);
        List<DanmakuRep> result = new ArrayList<>();
        for (Danmaku danmaku : danmus) {
            User user = user_service.get_user_by_id(danmaku.getUid());
            result.add(DanmakuRep.newRep(user.getName(), danmaku));
        }
        String data = new ObjectMapper().writeValueAsString(result);
        return Result.success(data);
    }

    @Operation(summary = "发送弹幕")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = DanmakuReq.class)))
    @PostMapping
    public ResponseEntity<String> add_danmaku(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        Danmaku danmaku = new ObjectMapper().readValue(entity, DanmakuReq.class).toDanmaku(uid);

        danmaku_service.add_danmaku(danmaku);
        return Result.success();
    }

    @Operation(summary = "删除弹幕")
    @Parameters(@Parameter(name = "id", description = "要删除的弹幕id"))
    @DeleteMapping("{id}")
    public ResponseEntity<String> del_danmaku(@PathVariable Integer id) {
        danmaku_service.del_danmaku(id);
        return Result.success();
    }
}
