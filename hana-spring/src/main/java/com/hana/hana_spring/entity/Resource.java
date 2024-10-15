package com.hana.hana_spring.entity;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resource {
    private Integer id; // 动漫id
    private String type; // 动漫类型 动画 | 漫画
    private String cover; // 动漫封面url
    private String name; // 动漫名
    private String title; // 动漫标题
    private Integer episode; // 集数
    @Schema(name = "episode_name")
    @JsonAlias("episode_name")
    private String episodeName; // 集名
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String path; // 存储路径
    private String url; // 资源访问url
    private boolean process; // 审核状态
    private Integer uid; // 上传用户id
    private Integer sid; // 所属板块id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likes; // 点赞数量
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer views; // 播放数量
}
