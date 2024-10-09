package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.Announcement;

@Mapper
public interface AnnouncementMapper {
    @Select("select * from announcement")
    List<Announcement> sel_all();

    @Insert("insert into announcement(title, content, time) values(#{title}, #{content}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Announcement announcement);

    @Delete("delete from announcement where id=#{id}")
    void del(int id);

    @Update("update announcement set title=#{title} where id=#{id}")
    void upd_title(Announcement announcement);

    @Update("update announcement set content=#{content} where id=#{id}")
    void upd_content(Announcement announcement);

    @Update("update announcement set time=#{time} where id=#{id}")
    void upd_time(Announcement announcement);
}
