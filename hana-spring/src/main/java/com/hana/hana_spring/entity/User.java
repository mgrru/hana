package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
  private Integer id; // 用户id
  private String account; // 用户账号
  @JsonIgnore
  private String pass; // 账号密码
  private String name; // 用户名
  private boolean isBan; // 封禁状态
  private Integer age; // 年龄
  private String phone; // 电话
  private String email; // 邮箱
  private Role role; // 角色
}
