package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import com.hana.hana_spring.entity.Danmaku;

import lombok.Data;

@Component
@Data
public class DanmakuReq {
    private String content; // 弹幕内容
    private Integer rid; // 动漫id

    public Danmaku toDanmaku(Integer uid) {
        return new Danmaku(null, uid, rid, content);
    }
}
