package com.rabbitfragmework.jadb.test.demo;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.rabbitfragmework.jadb.test.code.DataAccessTestCase;
import com.rabbitfragmework.jadb.test.mapper.TestUserMapper;
import com.rabbitfragmework.jadb.test.model.TestUser;
import com.rabbitframework.jadb.mapping.RowBounds;
import com.rabbitframework.jadb.mapping.param.WhereParamType;

public class TestUserTestCase extends DataAccessTestCase {
	private Logger logger = LogManager.getLogger(TestUserTestCase.class);

	// @Test
	public void createTestUser() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		int result = testMapper.createTestUser();
		System.out.println(result);
	}

//	 @Test
	public void insertTestUser() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		TestUser testUser = new TestUser();
		testUser.setTestName("testAuto");
		int result = testMapper.insertTest(testUser);
		System.out.println(result);
		System.out.println(testUser.getId());
	}

//	 @Test
	public void updateTestUserById() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		int result = testMapper.updateTest(1L, "updateName");
		System.out.println("result:" + result);
	}

	 @Test
	public void updateTestByUser() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		TestUser testUser = new TestUser();
		testUser.setId(5L);
		testUser.setTestName("testAutoupdate");
		testMapper.updateTestByUser(testUser);
	}

	// @Test
	public void selectTestUserAll() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		List<TestUser> testUsers = testMapper.selectTestUser();
		for (TestUser testUser : testUsers) {
			System.out.println("id:" + testUser.getId() + ",name:" + testUser.getTestName());
		}
	}

	// @Test
	public void selectTestUserByPage() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		List<TestUser> testUsers = testMapper.selectTestUserByPage(new RowBounds());
		for (TestUser testUser : testUsers) {
			System.out.println("id:" + testUser.getId() + ",name:" + testUser.getTestName());
		}
	}

	// @Test
	public void deleteTestUser() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		int result = testMapper.delTestUser(1L);
		System.out.println("result:" + result);
	}

	// @Test
	public void selectTestUserToMap() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		Map<Long, TestUser> testUser = testMapper.selectTestUserToMap();
		System.out.println(testUser.size());
	}

//	@Test
	public void selectTesstUserByParamType() {
		TestUserMapper testMapper = getMapper(TestUserMapper.class);
		WhereParamType paramType = new WhereParamType();
		paramType.createCriteria().andFieldIsEqualTo("id", 2L);
		List<TestUser> testUsers = testMapper.selectTesstUserByParamType(paramType);
		System.out.println(testUsers.size());
	}
}
