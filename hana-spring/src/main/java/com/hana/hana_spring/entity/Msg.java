package com.hana.hana_spring.entity;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Msg {
  private Integer id; // 消息id
  private Integer sender; // 发送消息用户
  private Integer recipient; // 接收消息用户
  private String content; // 消息内容
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
  private Timestamp time; // 消息发送时间
}
