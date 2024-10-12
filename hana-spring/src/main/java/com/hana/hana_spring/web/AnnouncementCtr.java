package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Announcement;
import com.hana.hana_spring.service.AnnouncementService;
import com.hana.hana_spring.utils.Result;

@RestController
@RequestMapping("announcements")
@CrossOrigin("*")
@Validate(auth = true)
public class AnnouncementCtr {
    @Autowired
    private AnnouncementService announcement_service;

    /**
     * 获取公告
     * 
     * @return {id, title, content, time}
     * @throws JsonProcessingException
     */
    @Validate(login = false, auth = false)
    @GetMapping
    public ResponseEntity<String> get_all_announcement() throws JsonProcessingException {
        List<Announcement> announcements = announcement_service.get_all_announcement();
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(announcements);
        return Result.success(data);
    }

    /**
     * 添加公告
     * 
     * @param entity {title, content, time}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PostMapping
    public ResponseEntity<String> add_announcement(@RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Announcement announcement = mapper.readValue(entity, Announcement.class);
        announcement_service.add_announcement(announcement);
        return Result.success();
    }

    /**
     * 修改公告
     * 
     * @param id     要修改的公告id
     * @param entity {title, content, time}
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @PutMapping("{id}")
    public ResponseEntity<String> upd_announcement(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Announcement announcement = mapper.readValue(entity, Announcement.class);
        announcement.setId(id);
        announcement_service.upd_announcement(announcement);
        return Result.success();
    }

    /**
     * 删除公告
     * 
     * @param id 要删除的公告id
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> del_section(@PathVariable Integer id) {
        announcement_service.del_announcement(id);
        return Result.success();
    }
}
