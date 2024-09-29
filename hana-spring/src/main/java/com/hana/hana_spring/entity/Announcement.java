package com.hana.hana_spring.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Announcement {
  private int id; // 公告id
  private String title; // 公告标题
  private String content; // 公告内容
  private Date time; // 公告发布时间
}
