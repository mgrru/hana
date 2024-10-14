package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.AnimeMapper;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.utils.Oss;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnimeService {
    @Autowired
    private AnimeMapper anime_mapper;

    @Autowired
    private Oss oss;

    public List<Resource> get_all_anime() {
        return anime_mapper.sel_all();
    }

    public Resource get_by_name_episode(String name, String episode_name) {
        return anime_mapper.sel_by_name_episode(name, episode_name);
    }

    public List<Resource> get_by_name(String name) {
        return anime_mapper.sel_by_name(name);
    }

    public void add_anime(Resource resource) {
        if (resource != null) {
            anime_mapper.ins(resource);
        }
    }

    public void del_anime(Integer id) {
        if (id != null) {
            oss.delete(anime_mapper.sel_by_id(id).getCover());
            anime_mapper.del(id);
        }
    }

    public void del_user_anime(Integer uid, Integer rid) {
        if (uid != null && rid != null) {
            oss.delete(anime_mapper.sel_by_id(rid).getCover());
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

    public void add_likes(Integer rid) {
        Resource resource = anime_mapper.sel_by_id(rid);
        resource.setLikes(resource.getLikes() + 1);
        anime_mapper.upd_likes(resource);
    }

    public void add_views(Integer rid) {
        Resource resource = anime_mapper.sel_by_id(rid);
        resource.setViews(resource.getViews() + 1);
        anime_mapper.upd_views(resource);
    }

    public List<Resource> get_recommend() {
        return anime_mapper.sel_recommend();
    }

    public List<Resource> get_popular() {
        return anime_mapper.sel_popular();
    }

    public Resource get_by_id(Integer id) {
        return anime_mapper.sel_by_id(id);
    }

    public List<Resource> get_all_process() {
        return anime_mapper.sel_all_process();
    }
}
