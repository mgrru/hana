package com.hana.hana_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

	@Test
	void test_del_role() {
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

		List<Role> origin_roles = new ArrayList<>();
		List<Role> result_roles = new ArrayList<>();

		// 检查1
		origin_roles = role_service.get_all_role();
		role_service.add_role(role1);
		result_roles = role_service.get_all_role();
		assertNotEquals(origin_roles, result_roles);

		role_service.del_role(role1);
		result_roles = role_service.get_all_role();
		assertEquals(origin_roles, result_roles);

		// 检查2
		origin_roles = role_service.get_all_role();
		role_service.add_role(role2);
		result_roles = role_service.get_all_role();
		assertNotEquals(origin_roles, result_roles);

		role_service.del_role(role2);
		result_roles = role_service.get_all_role();
		assertEquals(origin_roles, result_roles);

		// 检查3
		origin_roles = role_service.get_all_role();
		role_service.add_role(role3);
		result_roles = role_service.get_all_role();
		assertNotEquals(origin_roles, result_roles);

		role_service.del_role(role3);
		result_roles = role_service.get_all_role();
		assertEquals(origin_roles, result_roles);

		// 检查4
		origin_roles = role_service.get_all_role();
		role_service.add_role(role);
		result_roles = role_service.get_all_role();
		assertEquals(origin_roles, result_roles);

		role_service.del_role(role);
		result_roles = role_service.get_all_role();
		assertEquals(origin_roles, result_roles);
	}

}
