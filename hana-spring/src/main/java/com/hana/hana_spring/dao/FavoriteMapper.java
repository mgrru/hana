package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Resource;

@Mapper
public interface FavoriteMapper {
    @Select("select resource.* from resource,favorite where favorite.uid=#{uid} and favorite.rid=resource.id")
    List<Resource> sel_all(int uid);

    @Insert("insert into favorite(uid, rid) values(#{uid}, #{rid})")
    void ins(int uid, int rid);

    @Delete("delete from favorite where uid=#{uid} and rid=#{rid}")
    void del(int uid, int rid);

    @Delete("delete from favorite where rid=#{rid}")
    void disable(int rid);
}
