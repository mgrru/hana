package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.FavoriteMapper;
import com.hana.hana_spring.entity.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class FavoriteService {
    @Autowired
    private FavoriteMapper favorite_mapper;

    public List<Resource> get_user_favorite(Integer uid) {
        return favorite_mapper.sel_all(uid);
    }

    public void add_favorite(Integer uid, Integer rid) {
        if (favorite_mapper.sel_by_uid_and_rid(uid, rid) != null) {
            return;
        }
        favorite_mapper.ins(uid, rid);
    }

    public void del_favorite(Integer uid, Integer rid) {
        favorite_mapper.del(uid, rid);
    }
}
