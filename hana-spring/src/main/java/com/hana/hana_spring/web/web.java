package com.hana.hana_spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.hana.hana_spring.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class web {
  @Autowired
  private AuthService auth_service;

  @GetMapping("auth")
  public String get_all_auth() {
      return new String(auth_service.get_all_auth());
  }
  
}
