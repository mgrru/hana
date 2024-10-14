package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Favorite {
    private Integer id; // 收藏id
    private int uid; // 用户id
    private int rid; // 收藏动漫id
}
