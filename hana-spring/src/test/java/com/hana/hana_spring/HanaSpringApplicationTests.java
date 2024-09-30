package com.hana.hana_spring;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hana.hana_spring.entity.Auth;
import com.hana.hana_spring.entity.Role;
import com.hana.hana_spring.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
class HanaSpringApplicationTests {

	@Autowired
	private RoleService role_service;

	@Test
	void test_auth() {
		List<Auth> auths = role_service.get_all_auth();
		log.info(auths.toString());
	}

	@Test
	void test_role() {
		List<Role> roles = role_service.get_all_role();
		log.info("roles=" + roles);
	}

	@Test
	void test_add_role() {
		List<Auth> auths = role_service.get_all_auth();

		List<Auth> auths1 = new ArrayList<>();
		List<Auth> auths2 = new ArrayList<>();
		List<Auth> empty_auths = new ArrayList<>();

		auths1.add(auths.get(0));

		auths2.add(auths.get(1));
		auths2.add(auths.get(2));

		Role role1 = new Role(3, "测试会员1", auths1);
		Role role2 = new Role(4, "测试会员2", auths2);
		Role role3 = new Role(5, "测试会员3", empty_auths);
		Role role = new Role();

		role_service.add_role(role1);
		test_role();

		role_service.add_role(role2);
		test_role();

		role_service.add_role(role3);
		test_role();

		role_service.add_role(role);
		test_role();

	}

}
