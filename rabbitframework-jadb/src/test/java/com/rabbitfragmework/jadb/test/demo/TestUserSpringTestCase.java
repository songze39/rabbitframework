package com.rabbitfragmework.jadb.test.demo;

import java.util.List;

import org.junit.Test;

import com.rabbitfragmework.jadb.test.code.AbstractSpringTestCase;
import com.rabbitfragmework.jadb.test.model.TestUser;
import com.rabbitfragmework.jadb.test.service.TestUserService;

public class TestUserSpringTestCase extends AbstractSpringTestCase {
	@Test
	public void testSelectAll() {
		TestUserService testUserService = getBean(TestUserService.class);
		List<TestUser> testUsers = testUserService.selectTestUser();
		for (TestUser testUser : testUsers) {
			System.out.println("id:" + testUser.getId() + ",name:"
					+ testUser.getTestName());

		}
	}
}
