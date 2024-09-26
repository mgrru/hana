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
public class Comment {
  private int id;
  private int uid;
  private int rid;
  private String content;
  private Date time;
}
