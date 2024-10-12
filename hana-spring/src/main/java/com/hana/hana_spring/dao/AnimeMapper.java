package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hana.hana_spring.entity.Resource;

@Mapper
public interface AnimeMapper {
    @Select("select * from resource")
    List<Resource> sel_all();

    @Select("select * from resource where name=#{name} and episode_name=#{episode_name}")
    Resource sel_by_name(String name, String episode_name);

    @Select("select * from resource where uid=#{uid}")
    List<Resource> sel_by_user(int uid);

    @Insert("insert into resource(type, cover, name, episode_name, path, url, process, uid, sid) values(#{type}, #{cover}, #{name}, #{episodeName}, #{path}, #{url}, #{process}, #{uid}, #{sid})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void ins(Resource resource);

    @Delete("delete from resource where id=#{id}")
    void del(int id);

    @Delete("delete from resource where id=#{rid} and uid=#{uid}")
    void del_user_ainme(int uid, int rid);

    @Update("update resource set process=#{process} where id=#{id}")
    void process(Resource resource);

    @Select("SELECT * FROM resource WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Resource> search(String name);

    @Update("update resource set likes=#{likes} where id=#{id}")
    void upd_likes(Resource resource);


    @Select("select * from resource where id=#{id}")
    Resource sel_by_id(int id);

    @Update("update resource set views=#{views} where id=#{id}")
    void upd_views(Resource resource);
}
