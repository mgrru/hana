package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Favorite;
import com.hana.hana_spring.entity.Resource;

@Mapper
public interface FavoriteMapper {
    @Select("select resource.* from resource,favorite where favorite.uid=#{uid} and favorite.rid=resource.id")
    @ResultMap("com.hana.hana_spring.dao.AnimeMapper.resource")
    List<Resource> sel_all(int uid);

    @Select("select * from favorite where uid=#{uid} and rid=#{rid}")
    Favorite sel_by_uid_and_rid(int uid, int rid);

    @Insert("insert into favorite(uid, rid) values(#{uid}, #{rid})")
    void ins(int uid, int rid);

    @Delete("delete from favorite where uid=#{uid} and rid=#{rid}")
    void del(int uid, int rid);

    @Delete("delete from favorite where rid=#{rid}")
    void disable(int rid);
}
