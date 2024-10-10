package com.hana.hana_spring.entity.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.hana.hana_spring.entity.Comment;

import lombok.Data;

@Component
@Data
public class CommentReq {
    private String content; // 评论内容
    private Integer rid; // 动漫id
    private Date time; // 评论日期

    public Comment toComment(Integer uid) {
        return new Comment(null, uid, rid, content, time);
    }

}
