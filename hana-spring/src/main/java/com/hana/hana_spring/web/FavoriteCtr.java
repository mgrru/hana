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
import com.hana.hana_spring.service.FavoriteService;
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
@RequestMapping("favorites")
@CrossOrigin("*")
@Validate
public class FavoriteCtr {
    @Autowired
    private FavoriteService favorite_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "获取收藏")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @GetMapping
    public ResponseEntity<String> get_favorites(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> favorites = favorite_service.get_user_favorite(uid);
        String data = new ObjectMapper().writeValueAsString(favorites);
        return Result.success(data);
    }

    @Operation(summary = "添加收藏")
    @Parameters({ @Parameter(name = "rid", description = "收藏的动漫id") })
    @PostMapping("{rid}")
    public ResponseEntity<String> add_favorite(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        favorite_service.add_favorite(uid, rid);
        return Result.success();
    }

    @Operation(summary = "删除收藏")
    @Parameters({ @Parameter(name = "rid", description = "要删除的收藏的动漫id") })
    @DeleteMapping("{rid}")
    public ResponseEntity<String> del_favorite(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        favorite_service.del_favorite(uid, rid);
        return Result.success();
    }

}
