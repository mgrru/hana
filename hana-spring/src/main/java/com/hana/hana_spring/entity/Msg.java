package com.hana.hana_spring.entity;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Msg {
  private int id;
  private User sender;
  private User recipient;
  private String content;
  private Timestamp time;
}
