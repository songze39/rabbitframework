package com.rabbitfragmework.jadb.test.code;

import java.io.IOException;
import java.io.Reader;

import com.rabbitframework.commons.utils.ResourceUtils;
import com.rabbitframework.jadb.RabbitJadbFactory;
import com.rabbitframework.jadb.RabbitJadbFactoryBuilder;
import com.rabbitframework.jadb.dataaccess.SqlDataAccess;

public class DataAccessTestCase extends AbstractJadeTestCase {
	private SqlDataAccess sqlDataAccess;

	@Override
	protected void initJadb() throws IOException {
		RabbitJadbFactoryBuilder builder = new RabbitJadbFactoryBuilder();
		Reader reader;
		reader = ResourceUtils.getResourceAsReader("jadbConfig.xml");
		RabbitJadbFactory jadbFactory = builder.build(reader);
		sqlDataAccess = jadbFactory.openSqlDataAccess();
	}

	public <T> T getMapper(Class<T> clazz) {
		return sqlDataAccess.getMapper(clazz);
	}
}
