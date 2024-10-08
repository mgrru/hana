package com.hana.hana_spring;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.entity.Section;
import com.hana.hana_spring.service.SectionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class SectionTest {
    @Autowired
    private SectionService section_service;

    @Test
    void test_get_section() {
        List<Section> sections = section_service.get_all_section();
        log.info("sections: " + sections);
    }

    @Test
    void test_add_section() {
        Section section = new Section(null, "日漫");
        section_service.add_section(section);
        test_get_section();
    }

    @Test
    void test_del() {
        Section section = new Section(null, "日漫");
        section_service.add_section(section);
        test_get_section();
        section_service.del_section(section.getId());
        test_get_section();
    }

    @Test
    void test_upd() {
        Section section = new Section(null, "日漫");
        section_service.add_section(section);
        test_get_section();

        section.setName("国漫");
        section_service.upd_section(section);
        test_get_section();
    }
}
