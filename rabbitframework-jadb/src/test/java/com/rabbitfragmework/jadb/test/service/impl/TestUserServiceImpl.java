package com.rabbitfragmework.jadb.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rabbitfragmework.jadb.test.mapper.TestUserMapper;
import com.rabbitfragmework.jadb.test.model.TestUser;
import com.rabbitfragmework.jadb.test.service.TestUserService;

@Service("testUserService")
public class TestUserServiceImpl implements TestUserService {
	@Resource
	private TestUserMapper testUserMapper;

	@Override
	public List<TestUser> selectTestUser() {
		return testUserMapper.selectTestUser();
	}

}
