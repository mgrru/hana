package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.History;

@Mapper
public interface HistoryMapper {
    @Select("select * from history where uid=#{uid}")
    List<History> sel_all(int uid);

    @Insert("insert into history(uid, rid, time) values(#{uid}, #{rid}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(History history);

    @Delete("delete from history where id=#{id}")
    void del(int id);

    @Delete("delete from history where rid=#{rid}")
    void disable(int rid);
}
