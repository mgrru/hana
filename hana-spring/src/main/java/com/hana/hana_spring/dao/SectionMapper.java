package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.Section;

@Mapper
public interface SectionMapper {
    @Select("select * from section")
    List<Section> sel_all();

    @Insert("insert into section(name) values(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Section section);

    @Delete("delete from section where id=#{id}")
    void del(int id);

    @Update("update section set name=#{name} where id=#{id}")
    void upd(Section section);
}
