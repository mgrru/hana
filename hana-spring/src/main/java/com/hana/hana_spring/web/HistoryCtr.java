package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.HistoryService;
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
@RequestMapping("history")
@CrossOrigin("*")
@Validate
public class HistoryCtr {
    @Autowired
    private HistoryService history_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "获取观看历史")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @GetMapping
    public ResponseEntity<String> get_history(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> histories = history_service.get_user_history(uid);
        String data = new ObjectMapper().writeValueAsString(histories);
        return Result.success(data);
    }

    @Operation(summary = "添加观看历史")
    @Parameters(@Parameter(name = "rid", description = "观看的动漫id"))
    @PostMapping("{rid}")
    public ResponseEntity<String> add_history(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        history_service.add_history(uid, rid);
        return Result.success();
    }

    @Operation(summary = "删除观看历史")
    @Parameters(@Parameter(name = "rid", description = "要删除的动漫id"))
    @DeleteMapping("{rid}")
    public ResponseEntity<String> del_history(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        history_service.del_history(uid, rid);
        return Result.success();
    }

}
