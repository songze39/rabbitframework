package com.rabbitframework.jadb;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.dataaccess.DefaultSqlDataAccess;
import com.rabbitframework.jadb.dataaccess.SqlDataAccess;

public class DefaultRabbitJadbFactory implements RabbitJadbFactory {
	private Configuration configuration;

	public DefaultRabbitJadbFactory(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public SqlDataAccess openSqlDataAccess() {
		return new DefaultSqlDataAccess(configuration);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

}
