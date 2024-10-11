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

    public Resource get_by_name(String name, String episode_name) {
        return anime_mapper.sel_by_name(name, episode_name);
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

    public void del_user_anime(Integer uid, Integer rid) {
        if (uid != null && rid != null) {
            anime_mapper.del_user_ainme(uid, rid);
        }
    }

    public void process_anime(Integer id) {
        if (id != null) {
            Resource resource = new Resource();
            resource.setId(id);
            resource.setProcess(true);
        }
    }

    public List<Resource> get_by_user(Integer uid) {
        if (uid != null) {
            return anime_mapper.sel_by_user(uid);
        } else {
            return null;
        }
    }

    public List<Resource> search(String name) {
        if (name != null) {
            return anime_mapper.search(name);
        } else {
            return null;
        }
    }
}
