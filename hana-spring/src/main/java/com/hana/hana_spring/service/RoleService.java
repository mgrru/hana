package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.RoleDao;
import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

  @Autowired
  private RoleDao role_dao;

  public List<Auth> get_all_auth() {
    List<Auth> auths = role_dao.sel_all_auth();
    return auths;
  }

  public List<Role> get_all_role() {
    List<Role> roles = role_dao.sel_all_role();
    return roles;
  }

  public void add_role(Role role) {
    if (role != null && role.getName() != null && !role.getName().isBlank()) {
      role_dao.insert_role(role);
    }

    int rid = role.getId();
    if (role.getAuths() == null) {
      return;
    }
    role.getAuths()
        .forEach((auth) -> {
          role_dao.insert_role_auth(rid, auth.getId());
        });
  }
}
