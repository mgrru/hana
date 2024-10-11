package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.User;

@Mapper
public interface UserMapper {
        @Select("select user.*, role.id r_id, role.name r_name from user left join role on rid=role.id")
        @Results(id = "user", value = {
                        @Result(property = "id", column = "id", id = true),
                        @Result(property = "account", column = "account"),
                        @Result(property = "pass", column = "pass"),
                        @Result(property = "name", column = "name"),
                        @Result(property = "isBan", column = "is_ban"),
                        @Result(property = "age", column = "age"),
                        @Result(property = "phone", column = "phone"),
                        @Result(property = "email", column = "email"),
                        @Result(property = "role", one = @One(resultMap = "com.hana.hana_spring.dao.RoleMapper.role", columnPrefix = "r_")),
        })
        List<User> sel_all();

        @Insert("insert into user(account, pass, name, is_ban, age, phone, email, rid) values(#{account}, #{pass}, #{name}, #{isBan}, #{age}, #{phone}, #{email}, #{role.id})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void ins(User user);

        @Delete("delete from user where id=#{id}")
        void del(int id);

        @Update("update user set account=#{account},pass=#{pass},name=#{name},is_ban=#{isBan},age=#{age},phone=#{phone},email=#{email},rid=#{role.id} where id=#{id}")
        void upd(User user);

        @Update("update user set is_ban=#{isBan} where id=#{id}")
        void ban(User user);

        @Update("update user set rid=#{rid} where id=#{id}")
        void upd_role(int id, int rid);

        // 用户功能

        @Select("select user.*, role.id r_id, role.name r_name from user left join role on rid=role.id where id=#{id}")
        @ResultMap("user")
        User sel_by_id(int id);

        @Select("select user.*, role.id r_id, role.name r_name from user left join role on rid=role.id where account=#{account}")
        User sel_by_account(String account);

        @Update("update user set pass=#{pass} where id=#{id}")
        void upd_pass(User user);

        @Update("update user set name=#{name} where id=#{id}")
        void upd_name(User user);

        @Update("update user set age=#{age} where id=#{id}")
        void upd_age(User user);

        @Update("update user set phone=#{phone} where id=#{id}")
        void upd_phone(User user);

        @Update("update user set email=#{email} where id=#{id}")
        void upd_email(User user);
}
