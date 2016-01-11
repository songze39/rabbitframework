package com.rabbitframework.jadb.dataaccess.datasource;

import com.rabbitframework.jadb.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

public class RandomDataSourceFactory implements DataSourceFactory {
	private Random random = new Random();
	private List<DataSource> dataSources = Collections.emptyList();

	@Override
	public void addDataSource(String key, DataSource dataSource) {
		if (dataSource == null) {
			throw new NullPointerException("dataSource is null");
		}
		if (dataSources.size() == 0) {
			dataSources = new ArrayList<DataSource>(dataSources);
		}
	}

	@Override
	public DataSource getDataSource(MappedStatement mappedStatement) {
		if (dataSources.size() == 0) {
			return null;
		}
		int index = random.nextInt(dataSources.size()); // 0.. size
		return dataSources.get(index);
	}
}
