package com.hana.hana_spring.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.AnimeMapper;
import com.hana.hana_spring.dao.HistoryMapper;
import com.hana.hana_spring.entity.History;
import com.hana.hana_spring.entity.dto.HistoryRep;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryService {
    @Autowired
    private HistoryMapper history_mapper;

    @Autowired
    private AnimeMapper anime_mapper;

    public List<HistoryRep> get_user_history(Integer uid) {
        List<HistoryRep> result = new ArrayList<>();
        List<History> histories = history_mapper.sel_all(uid);
        histories.forEach((history) -> {
            result.add(
                    new HistoryRep(history.getId(), anime_mapper.sel_by_id(history.getRid()), history.getTime()));
        });
        return result;
    }

    public void add_history(Integer uid, Integer rid) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        History history = new History(null, uid, rid, now);
        history_mapper.ins(history);
    }

    public void del_history(Integer id) {
        history_mapper.del(id);
    }
}
