package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Danmaku {
  private int id; // 弹幕id
  private int uid; // 发送弹幕用户id
  private int rid; // 动漫资源id
  private String content; // 弹幕内容
}
