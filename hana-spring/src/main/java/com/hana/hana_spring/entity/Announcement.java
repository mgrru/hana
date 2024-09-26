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
  private int id;
  private String title;
  private String content;
  private Date time;
}
