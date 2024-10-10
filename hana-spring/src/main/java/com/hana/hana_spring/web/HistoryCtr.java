package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.HistoryService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("history")
@CrossOrigin("*")
@LoginValidate
public class HistoryCtr {
    @Autowired
    private HistoryService history_service;

    @Autowired
    private JwtUtil jwt_util;

    @GetMapping
    public Result get_history(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> histories = history_service.get_user_history(uid);
        String data = new ObjectMapper().writeValueAsString(histories);
        return Result.success(data);
    }

    @PostMapping("{rid}")
    public Result add_history(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        history_service.add_history(uid, rid);
        return Result.success();
    }

    @DeleteMapping("{rid}")
    public Result del_history(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        history_service.del_history(uid, rid);
        return Result.success();
    }

}
