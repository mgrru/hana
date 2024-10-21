package com.hana.hana_spring.entity.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hana.hana_spring.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRep {
    private Integer id;
    private String user_name;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date time;

    public static CommentRep newRep(String user_name, Comment comment) {
        return new CommentRep(comment.getId(), user_name, comment.getContent(), comment.getTime());
    }
}
