package com.hana.hana_spring.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment {
    private Integer id; // 评论id
    private int uid; // 评论用户id
    private int rid; // 动漫资源id
    private String content; // 评论内容
    private Date time; // 评论时间
}
