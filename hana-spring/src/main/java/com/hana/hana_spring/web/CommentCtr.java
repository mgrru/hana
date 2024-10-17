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
import com.hana.hana_spring.entity.Comment;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.entity.dto.CommentRep;
import com.hana.hana_spring.entity.dto.CommentReq;
import com.hana.hana_spring.service.CommentService;
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
@RequestMapping("comments")
@CrossOrigin("*")
@Validate
public class CommentCtr {
    @Autowired
    private CommentService comment_service;

    @Autowired
    private UserService user_service;

    @Autowired
    private JwtUtil jwt_util;

    @Operation(summary = "获取动漫的评论")
    @Parameters({ @Parameter(name = "rid", description = "动漫id") })
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentRep.class))))
    @Validate(login = false)
    @GetMapping("{rid}")
    public ResponseEntity<String> get_comment(@PathVariable Integer rid) throws Exception {
        List<Comment> comments = comment_service.get_comment(rid);
        List<CommentRep> result = new ArrayList<>();
        for (Comment comment : comments) {
            User user = user_service.get_user_by_id(comment.getUid());
            result.add(CommentRep.newRep(user.getName(), comment));
        }
        String data = new ObjectMapper().writeValueAsString(result);
        return Result.success(data);
    }

    @Operation(summary = "发送评论")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CommentReq.class)))
    @PostMapping
    public ResponseEntity<String> add_comment(@RequestBody String entity, HttpServletRequest req)
            throws JsonMappingException, JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        Comment comment = new ObjectMapper().readValue(entity, CommentReq.class).toComment(uid);

        comment_service.add_comment(comment);
        return Result.success();
    }

    @Operation(summary = "删除评论")
    @Parameters({ @Parameter(name = "id", description = "要删除的评论id") })
    @DeleteMapping("{id}")
    public ResponseEntity<String> del_comment(@PathVariable Integer id) {
        comment_service.del_comment(id);
        return Result.success();
    }
}
