package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.DanmakuMapper;
import com.hana.hana_spring.entity.Danmaku;

@Service
@Transactional(rollbackFor = Exception.class)
public class DanmakuService {
    @Autowired
    private DanmakuMapper danmaku_mapper;

    public List<Danmaku> get_danmaku(Integer rid) {
        return danmaku_mapper.sel_all(rid);
    }

    public void add_danmaku(Danmaku danmaku) {
        danmaku_mapper.ins(danmaku);
    }

    public void del_danmaku(Integer id) {
        danmaku_mapper.del(id);
    }
}
