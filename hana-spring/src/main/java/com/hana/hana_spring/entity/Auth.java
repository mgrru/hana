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
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Auth [id=").append(id).append(", name=").append(name).append("]");
    return builder.toString();
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
}
