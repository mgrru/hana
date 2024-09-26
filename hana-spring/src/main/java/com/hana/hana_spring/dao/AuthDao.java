package com.hana.hana_spring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.hana.hana_spring.entity.Auth;


@Mapper
public interface AuthDao {
  @Select("select * from auth")
  List<Auth> get_all_auth();
}
