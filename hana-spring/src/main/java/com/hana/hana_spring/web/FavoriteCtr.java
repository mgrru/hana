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
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.FavoriteService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

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

    /**
     * 获取收藏
     * 
     * @return [{id, type, cover, name, episodeName, url, process, uid, sid}]
     * @throws JsonProcessingException
     */
    @GetMapping
    public Result get_favorites(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> favorites = favorite_service.get_user_favorite(uid);
        String data = new ObjectMapper().writeValueAsString(favorites);
        return Result.success(data);
    }

    /**
     * 添加收藏
     * 
     * @param rid 收藏的动漫id
     * @return
     */
    @PostMapping("{rid}")
    public Result add_favorite(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        favorite_service.add_favorite(uid, rid);
        return Result.success();
    }

    /**
     * 删除收藏
     * 
     * @param rid 收藏的动漫id
     * @return
     */
    @DeleteMapping("{rid}")
    public Result del_favorite(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        favorite_service.del_favorite(uid, rid);
        return Result.success();
    }

}
