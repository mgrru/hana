package com.hana.hana_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hana.hana_spring.service.AuthService;

@SpringBootTest
class HanaSpringApplicationTests {

	@Autowired
	private AuthService auth_service;

	@Test
	void test_auth(){
		auth_service.get_all_auth();
	}

}
