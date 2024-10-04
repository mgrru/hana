package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.User;

@Mapper
public interface UserDao {
    @Select("SELECT\r\n" + //
                "    user.id,\r\n" + //
                "    user.account,\r\n" + //
                "    user.pass,\r\n" + //
                "    user.name,\r\n" + //
                "    user.is_ban,\r\n" + //
                "    user.age,\r\n" + //
                "    user.phone,\r\n" + //
                "    user.email,\r\n" + //
                "    role.id as r_id,\r\n" + //
                "    role.name as r_name,\r\n" + //
                "    auth.id as a_id,\r\n" + //
                "    auth.name as a_name\r\n" + //
                "FROM\r\n" + //
                "    user\r\n" + //
                "    LEFT JOIN role ON rid = role.id\r\n" + //
                "    LEFT JOIN role_auth rs ON role.id = rs.rid\r\n" + //
                "    LEFT JOIN auth ON auth.id = rs.aid")
    @Results(id = "user", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "account", column = "account"),
            @Result(property = "pass", column = "pass"),
            @Result(property = "name", column = "name"),
            @Result(property = "isBan", column = "is_ban"),
            @Result(property = "age", column = "age"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "email", column = "email"),
            @Result(property = "role", one = @One(resultMap = "com.hana.hana_spring.dao.RoleDao.role", columnPrefix = "r_")),
            @Result(property = "role.auths", many = @Many(resultMap = "com.hana.hana_spring.dao.AuthDao.auth", columnPrefix = "a_"))
    })
    List<User> sel_all();

    @Insert("insert into user values(#{id},#{account},#{pass},#{name},#{isBan},#{age},#{phone},#{email},#{role.id})")
    void ins(User user);

    @Delete("delete from user where id=#{id}")
    void del(int id);

    @Update("update user set account=#{account},pass=#{pass},name=#{name},is_ban=#{isBan},age=#{age},phone=#{phone},email=#{email},rid=#{role.id} where id=#{id}")
    void upd(User user);
}
