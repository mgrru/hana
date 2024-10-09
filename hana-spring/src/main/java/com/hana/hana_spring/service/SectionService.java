package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.SectionMapper;
import com.hana.hana_spring.entity.Section;

@Service
@Transactional(rollbackFor = Exception.class)
public class SectionService {
    @Autowired
    private SectionMapper section_mapper;

    public List<Section> get_all_section() {
        return section_mapper.sel_all();
    }

    public void add_section(Section section) {
        section_mapper.ins(section);
    }

    public void del_section(int id) {
        section_mapper.del(id);
    }

    public void upd_section(Section section) {
        section_mapper.upd(section);
    }
}
