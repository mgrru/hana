package com.hana.hana_spring.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
  private Integer id; // 角色id
  private String name; // 角色名
  private List<Auth> auths; // 权限
}
