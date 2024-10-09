package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class History {
  private int id; // 观看历史id
  private int uid; // 用户id
  private int rid; // 观看动漫id
}
