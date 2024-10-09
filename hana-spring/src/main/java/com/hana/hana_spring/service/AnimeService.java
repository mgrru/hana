package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.AnimeMapper;
import com.hana.hana_spring.entity.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnimeService {
    @Autowired
    private AnimeMapper anime_mapper;

    public List<Resource> get_all_anime() {
        return anime_mapper.sel_all();
    }

    public void add_anime(Resource resource) {
        if (resource != null) {
            anime_mapper.ins(resource);
        }
    }

    public void del_anime(Integer id) {
        if (id != null) {
            anime_mapper.del(id);
        }
    }

    public void process_anime(Integer id) {
        if (id != null) {
            Resource resource = new Resource();
            resource.setId(id);
            resource.setProcess(true);
        }
    }
}
