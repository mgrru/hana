package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Danmaku;

@Mapper
public interface DanmakuMapper {
    @Select("select * from danmaku where rid=#{rid}")
    List<Danmaku> sel_all(int rid);

    @Insert("insert into danmaku(uid, rid, content) values(#{uid}, #{rid}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Danmaku danmaku);

    @Delete("delete from danmaku where id=#{id}")
    void del(int id);

    @Delete("delete from danmaku where rid=#{rid}")
    void disable(int rid);
}
