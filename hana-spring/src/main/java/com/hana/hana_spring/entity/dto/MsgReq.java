package com.hana.hana_spring.entity.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.hana.hana_spring.entity.Msg;

import lombok.Data;

@Component
@Data
public class MsgReq {
    private Integer recipient;
    private String content;

    public Msg toMsg(Integer sender) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return new Msg(null, sender, recipient, content, now);
    }
}
