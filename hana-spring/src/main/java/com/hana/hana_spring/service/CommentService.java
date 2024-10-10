package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.CommentMapper;
import com.hana.hana_spring.entity.Comment;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService {
    @Autowired
    private CommentMapper comment_mapper;

    public List<Comment> get_comment(Integer rid) {
        return comment_mapper.sel_all(rid);
    }

    public void add_comment(Comment comment) {
        comment_mapper.ins(comment);
    }

    public void del_comment(Integer id) {
        comment_mapper.del(id);
    }
}
