package com.hana.hana_spring.entity;

import java.io.File;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resource {
  private int id;
  private String type;
  private File cover;
  private String name;
  private int episodes;
  private String episodeName;
  private String path;
  private String url;
  private boolean process;
  private int uid;
  private Section section;
}
