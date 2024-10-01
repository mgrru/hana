package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.hana.hana_spring.entity.Auth;

@Mapper
@Repository
public interface AuthDao {
    @Select("select * from auth")
    @Results(id = "auth", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name")
    })
    List<Auth> sel_all();

    @Select("SELECT auth.id, auth.name FROM auth, role_auth WHERE rid=#{rid} AND aid=auth.id")
    List<Auth> sel_by_role_id(int rid);

    @Insert("insert into role_auth(rid, aid) values(#{rid},#{aid})")
    void ins_role_auth(int rid, int aid);

    /**
     * 删除角色的某一项权限
     * 
     * @param rid 角色id
     * @param aid 删除的权限id
     */
    @Delete("delete from role_auth where rid=#{rid} and aid=#{aid}")
    void del_role_one_auth(int rid, int aid);

    /**
     * 直接删除角色及其拥有的权限
     * 
     * @param rid 角色id
     */
    @Delete("delete from role_auth where rid=#{rid}")
    void del_role_all_auth(int rid);

    @Update("UPDATE role_auth SET aid=#{new_aid} WHERE rid=#{rid} AND aid=#{aid}")
    void upd_role_auth(int rid, int aid, int new_aid);

}
