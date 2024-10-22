package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.HistoryMapper;
import com.hana.hana_spring.entity.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryService {
    @Autowired
    private HistoryMapper history_mapper;

    public List<Resource> get_user_history(Integer uid) {
        return history_mapper.sel_all(uid);
    }

    public void add_history(Integer uid, Integer rid) {
        if (history_mapper.sel_by_uid_and_rid(uid, rid) != null) {
            return;
        }
        history_mapper.ins(uid, rid);
    }

    public void del_history(Integer uid, Integer rid) {
        history_mapper.del(uid, rid);
    }
}
