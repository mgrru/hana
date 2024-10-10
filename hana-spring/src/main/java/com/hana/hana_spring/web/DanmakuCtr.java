package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.entity.Danmaku;
import com.hana.hana_spring.entity.dto.DanmakuReq;
import com.hana.hana_spring.service.DanmakuService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("danmus")
@CrossOrigin("*")
@LoginValidate
public class DanmakuCtr {
    @Autowired
    private DanmakuService danmaku_service;

    @Autowired
    private JwtUtil jwt_util;

    /**
     * 获取动漫的弹幕
     * 
     * @param rid 动漫id
     * @throws JsonProcessingException
     */
    @LoginValidate(value = false)
    @GetMapping("{rid}")
    public Result get_danmaku(@PathVariable Integer rid) throws JsonProcessingException {
        List<Danmaku> danmus = danmaku_service.get_danmaku(rid);
        String data = new ObjectMapper().writeValueAsString(danmus);
        return Result.success(data);
    }

    /**
     * 发送弹幕
     * @param entity {content, rid}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping
    public Result add_danmaku(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        Danmaku danmaku = new ObjectMapper().readValue(entity, DanmakuReq.class).toDanmaku(uid);

        danmaku_service.add_danmaku(danmaku);
        return Result.success();
    }

    /**
     * 删除弹幕
     * @param id 要删除的弹幕id
     * @return
     */
    @DeleteMapping("{id}")
    public Result del_danmaku(@PathVariable Integer id) {
        danmaku_service.del_danmaku(id);
        return Result.success();
    }
}
