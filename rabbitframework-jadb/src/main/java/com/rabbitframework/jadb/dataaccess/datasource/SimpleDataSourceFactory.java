package com.rabbitframework.jadb.dataaccess.datasource;

import com.rabbitframework.jadb.mapping.MappedStatement;

import javax.sql.DataSource;

/**
 * 简单数据源工厂，此类只能添加一个{@link DataSource} 数据源
 */
public class SimpleDataSourceFactory implements DataSourceFactory {
    private DataSource dataSource;

    @Override
    public void addDataSource(String key, DataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource is null");
        }
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource(MappedStatement mappedStatement) {
        return dataSource;
    }

}
