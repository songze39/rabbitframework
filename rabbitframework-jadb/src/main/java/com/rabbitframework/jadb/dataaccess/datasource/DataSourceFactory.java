package com.rabbitframework.jadb.dataaccess.datasource;

import com.rabbitframework.jadb.mapping.MappedStatement;

import javax.sql.DataSource;

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
    void addDataSource(String key, DataSource dataSource);


    DataSource getDataSource(MappedStatement mappedStatement);


}
