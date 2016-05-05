package com.rabbitframework.jadb.dataaccess.datasource;

import javax.sql.DataSource;

import com.rabbitframework.jadb.dataaccess.DataSourceBean;
import com.rabbitframework.jadb.dataaccess.dialect.Dialect;
import com.rabbitframework.jadb.mapping.MappedStatement;

/**
 * 数据源工厂类
 */
public interface DataSourceFactory {
	/**
	 * 添加数据源
	 *
	 * @param key
	 * @param dataSource
	 */
	void addDataSource(String key, DataSourceBean dataSourceBean);

	DataSource getDataSource(MappedStatement mappedStatement);
	
	Dialect getDialect(MappedStatement mappedStatement);
}
