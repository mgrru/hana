package com.hana.hana_spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Announcement;
import com.hana.hana_spring.service.AnnouncementService;
import com.hana.hana_spring.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("announcements")
@CrossOrigin("*")
public class AnnouncementCtr {
    @Autowired
    private AnnouncementService announcement_service;

    @GetMapping
    public Result get_all_announcement() throws JsonProcessingException {
        List<Announcement> announcements = announcement_service.get_all_announcement();
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(announcements);
        return Result.success(data);
    }

    @PostMapping
    public Result add_announcement(@RequestBody String entity) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Announcement announcement = mapper.readValue(entity, Announcement.class);
        announcement_service.add_announcement(announcement);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result upd_announcement(@PathVariable Integer id, @RequestBody String entity)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Announcement announcement = mapper.readValue(entity, Announcement.class);
        announcement.setId(id);
        announcement_service.upd_announcement(announcement);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result del_section(@PathVariable Integer id) {
        announcement_service.del_announcement(id);
        return Result.success();
    }
}