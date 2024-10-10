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
import com.hana.hana_spring.entity.Comment;
import com.hana.hana_spring.entity.dto.CommentReq;
import com.hana.hana_spring.service.CommentService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("comments")
@CrossOrigin("*")
@LoginValidate
public class CommentCtr {
    @Autowired
    private CommentService comment_service;

    @Autowired
    private JwtUtil jwt_util;

    /**
     * 获取动漫的评论
     * 
     * @param rid 动漫id
     * @throws JsonProcessingException
     */
    @GetMapping("{rid}")
    public Result get_comment(@PathVariable Integer rid) throws JsonProcessingException {
        List<Comment> comments = comment_service.get_comment(rid);
        String data = new ObjectMapper().writeValueAsString(comments);
        return Result.success(data);
    }

    @PostMapping
    public Result add_comment(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        Comment comment = new ObjectMapper().readValue(entity, CommentReq.class).toComment(uid);

        comment_service.add_comment(comment);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result del_comment(@PathVariable Integer id) {
        comment_service.del_comment(id);
        return Result.success();
    }
}
