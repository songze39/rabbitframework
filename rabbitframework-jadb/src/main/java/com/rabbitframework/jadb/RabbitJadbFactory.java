package com.rabbitframework.jadb;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.dataaccess.SqlDataAccess;

public interface RabbitJadbFactory {

	public SqlDataAccess openSqlDataAccess();

	public Configuration getConfiguration();
}