package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hana.hana_spring.dao.AuthDao;
import com.hana.hana_spring.entity.Auth;

@Service
public class AuthService {
  @Autowired
  private AuthDao auth_dao;

  public String get_all_auth() {
    List<Auth> auths = auth_dao.get_all_auth();
    return auths.toString();

  }
}
