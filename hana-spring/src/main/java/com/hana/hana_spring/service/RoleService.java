package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hana.hana_spring.dao.RoleDao;
import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

@Service
public class RoleService {
  @Autowired
  private RoleDao role_dao;

  public String get_all_auth() {
    List<Auth> auths = role_dao.sel_all_auth();
    return auths.toString();
  }

  public String get_all_role() {
    List<Role> roles = role_dao.sel_all_role();
    return roles.toString();
  }
}
