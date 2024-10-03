package com.hana.hana_spring;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.entity.User;
import com.hana.hana_spring.service.RoleService;
import com.hana.hana_spring.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class UserTest {
    @Autowired
    private UserService user_service;

    @Autowired
    private RoleService role_service;

    @Test
    void test_get_user() {
        List<User> users = user_service.get_all_user();
        log.info("users: " + users);
    }

    @Test
    void test_add_user() {
        List<Role> roles = role_service.get_all_role();

        User user = new User(2, "test", "test", "tuser", false, 18, null, null, roles.get(0));
        user_service.add_user(user);
        test_get_user();
    }
}
