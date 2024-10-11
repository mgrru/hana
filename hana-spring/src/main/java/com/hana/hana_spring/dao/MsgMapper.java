package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Msg;

@Mapper
public interface MsgMapper {
    @Select("select * from msg where sender=#{uid} or recipient=#{uid}")
    List<Msg> sel_all(int uid);

    @Insert("insert into msg(sender, recipient, content, time) values(#{sender}, #{recipient}, #{content}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Msg msg);
}
