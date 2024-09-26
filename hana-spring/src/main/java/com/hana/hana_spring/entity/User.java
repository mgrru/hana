package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
  private int id;
  private String account;
  private String pass;
  private String name;
  private boolean isBan;
  private int age;
  private String phone;
  private String email;
  private Role role;
}
