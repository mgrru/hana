package com.hana.hana_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.dao.AnnouncementMapper;
import com.hana.hana_spring.entity.Announcement;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcement_mapper;

    public List<Announcement> get_all_announcement() {
        return announcement_mapper.sel_all();
    }

    public void add_announcement(Announcement announcement) {
        announcement_mapper.ins(announcement);
    }

    public void del_announcement(int id) {
        announcement_mapper.del(id);
    }

    public void upd_announcement(Announcement announcement) {
        if (announcement == null) {
            return;
        }

        if (announcement.getTitle() != null) {
            announcement_mapper.upd_title(announcement);
        }

        if (announcement.getContent() != null) {
            announcement_mapper.upd_content(announcement);
        }

        if (announcement.getTime() != null) {
            announcement_mapper.upd_time(announcement);
        }
    }
}
