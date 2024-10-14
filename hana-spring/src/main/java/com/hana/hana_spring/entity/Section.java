package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Section {
    private Integer id; // 板块id
    private String name; // 板块名
}
