package com.hana.hana_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hana.hana_spring.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class HanaSpringApplicationTests {

	@Autowired
	private RoleService auth_service;

	@Test
	void test_auth() {
		String auths = auth_service.get_all_auth();
		log.info(auths);
	}

	@Test
	void test_role() {
		String roles = auth_service.get_all_role();
		log.info("roles=" + roles);
	}

}
