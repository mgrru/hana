package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

@Mapper
public interface RoleDao {
  @Select("select role.*, auth.id as a_id, auth.name as a_name from role left join role_auth rs on role.id=rs.rid left join auth on auth.id=rs.aid")
  @Results(id = "role", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "name", column = "name"),
      @Result(property = "auths", many = @Many(resultMap = "auth", columnPrefix = "a_"))
  })
  List<Role> sel_all_role();

  @Select("select * from auth")
  @Results(id = "auth", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "name", column = "name")
  })
  List<Auth> sel_all_auth();
}
