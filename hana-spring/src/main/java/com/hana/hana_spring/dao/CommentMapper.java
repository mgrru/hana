package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Comment;

@Mapper
public interface CommentMapper {
    @Select("select * from comment where rid=#{rid}")
    List<Comment> sel_all(int rid);

    @Insert("insert into comment(uid, rid, content, time) values(#{uid}, #{rid}, #{content}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Comment comment);

    @Delete("delete from comment where id=#{id}")
    void del(int id);
}
