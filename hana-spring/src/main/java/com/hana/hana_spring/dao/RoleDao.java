package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

@Mapper
public interface RoleDao {
  @Select("select * from auth")
  @Results(id = "auth", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "name", column = "name")
  })
  List<Auth> sel_all_auth();

  @Insert("insert into role_auth(rid, aid) values(#{rid},#{aid})")
  void ins_role_auth(int rid, int aid);

  /**
   * 删除角色的某一项权限
   * @param rid 角色id
   * @param aid 删除的权限id
   */
  @Delete("delete from role_auth where rid=#{rid} and aid=#{aid}")
  void del_role_one_auth(int rid, int aid);

  /**
   * 直接删除角色及其拥有的权限
   * @param rid 角色id
   */
  @Delete("delete from role_auth where rid=#{rid}")
  void del_role_auth(int rid);

  @Select("select role.*, auth.id as a_id, auth.name as a_name from role left join role_auth rs on role.id=rs.rid left join auth on auth.id=rs.aid")
  @Results(id = "role", value = {
      @Result(property = "id", column = "id", id = true),
      @Result(property = "name", column = "name"),
      @Result(property = "auths", many = @Many(resultMap = "auth", columnPrefix = "a_"))
  })
  List<Role> sel_all_role();

  @Insert("insert into role values(#{id}, #{name})")
  void ins_role(Role role);

  @Delete("delete from role where id=#{id}")
  void del_role(int id);
}
