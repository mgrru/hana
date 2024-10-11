package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.MsgMapper;
import com.hana.hana_spring.entity.Msg;

@Service
@Transactional(rollbackFor = Exception.class)
public class MsgService {
    @Autowired
    private MsgMapper msg_mapper;

    public List<Msg> get_msg(Integer uid) {
        return msg_mapper.sel_all(uid);
    }

    public void send_msg(Msg msg) {
        if (msg != null) {
            msg_mapper.ins(msg);
        }
    }

}
