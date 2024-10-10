package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.entity.Section;
import com.hana.hana_spring.service.SectionService;
import com.hana.hana_spring.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@LoginValidate
@RestController
@RequestMapping("sections")
@CrossOrigin("*")
public class SectionCtr {
    @Autowired
    private SectionService section_service;

    @GetMapping
    public Result get_all_section() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Section> sections = section_service.get_all_section();

        String data = mapper.writeValueAsString(sections);

        return Result.success(data);
    }

    @PostMapping
    public Result add_section(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Section section = mapper.readValue(entity, Section.class);
        section_service.add_section(section);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result upd_section(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Section section = mapper.readValue(entity, Section.class);
        section.setId(id);
        section_service.upd_section(section);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result del_section(@PathVariable Integer id) {
        section_service.del_section(id);
        return Result.success();
    }

}
