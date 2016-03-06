package com.rabbitfragmework.jadb.test.mapper;

import java.util.List;
import java.util.Map;

import com.rabbitfragmework.jadb.test.model.TestUser;
import com.rabbitframework.jadb.annontations.CacheNamespace;
import com.rabbitframework.jadb.annontations.Create;
import com.rabbitframework.jadb.annontations.DbDialect;
import com.rabbitframework.jadb.annontations.Delete;
import com.rabbitframework.jadb.annontations.Insert;
import com.rabbitframework.jadb.annontations.MapKey;
import com.rabbitframework.jadb.annontations.Mapper;
import com.rabbitframework.jadb.annontations.Param;
import com.rabbitframework.jadb.annontations.Select;
import com.rabbitframework.jadb.annontations.Update;
import com.rabbitframework.jadb.dataaccess.dialect.MySqlDialect;
import com.rabbitframework.jadb.mapping.RowBounds;
import com.rabbitframework.jadb.mapping.param.WhereParamType;

@Mapper
public interface TestUserMapper {

	@Create("create table test_user (id int primary key auto_increment, test_name varchar(200))")
	public int createTestUser();

	// @Insert("insert into test_user(test_name) values(#{testName})")
	@Insert
	public int insertTest(TestUser testUser);

	@Update("update test_user set test_name=#{testName} where id=#{id}")
	public int updateTest(@Param("id") long id, @Param("testName") String testName);

	@Update
	public int updateTestByUser(TestUser testUser);

	@Delete("delete from test_user where id=#{id}")
	public int delTestUser(long id);

	@Select("select * from test_user")
	@CacheNamespace(pool = "defaultCache", key = { "seltestuser" })
	public List<TestUser> selectTestUser();

	@Select("select * from test_user")
	@MapKey("id")
	public Map<Long, TestUser> selectTestUserToMap();

	@Select("select * from test_user")
	@DbDialect(dialect = MySqlDialect.class)
	public List<TestUser> selectTestUserByPage(RowBounds rowBounds);

	@Select("select * from test_user")
	public List<TestUser> selectTesstUserByParamType(WhereParamType paramType);
}
