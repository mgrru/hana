package com.hana.hana_spring.entity.dto;

import org.springframework.stereotype.Component;

import com.hana.hana_spring.entity.Danmaku;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DanmakuRep {
    private Integer id;
    private String user_name;
    private String content;

    public static DanmakuRep newRep(String user_name, Danmaku danmaku) {
        return new DanmakuRep(danmaku.getId(), user_name, danmaku.getContent());
    }
}
