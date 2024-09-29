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
  private int id; // 消息id
  private User sender; // 发送消息用户
  private User recipient; // 接收消息用户
  private String content; // 消息内容
  private Timestamp time; // 消息发送时间
}
