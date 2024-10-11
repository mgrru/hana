package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.Role;

@Mapper
public interface RoleMapper {

    @Select("select * from role")
    @Results(id = "role", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
    })
    List<Role> sel_all();

    @Insert("insert into role(name) values(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Role role);

    @Delete("delete from role where id=#{id}")
    void del(int id);

    @Update("UPDATE role SET name=#{name} WHERE id=#{id}")
    void upd(Role role);
}
