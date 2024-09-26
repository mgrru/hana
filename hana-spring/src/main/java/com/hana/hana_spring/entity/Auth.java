package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

@Component
public class Auth {
  private Integer id;
  private String name;

  public Auth() {
  }

  public Auth(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return String.format("Auth [id=%s, name=%s]", id, name);
  }
}
