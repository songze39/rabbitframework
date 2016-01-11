package com.rabbitframework.jadb.dataaccess.datasource;

import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.jadb.annontations.Mapper;
import com.rabbitframework.jadb.mapping.MappedStatement;

/**
 * 多数据源配置,根据{@link Mapper}中的 catalog中的名称来确定
 * 如果名称为空将使用默认的DataSource数据源,但是需要配置默认数据源，只要key为"default"时，将使用默认数据源,
 */
public class MultiDataSourceFactory implements DataSourceFactory {
    private final static String DEFAULT = "default";
    private ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>();
    private DataSource defaultDataSource;

    @Override
    public void addDataSource(String key, DataSource dataSource) {
        if (StringUtils.isEmpty(key) || dataSource == null) {
            throw new NullPointerException("DataSource add fail: key=" + key
                    + ",DataDataSource=" + dataSource);
        }
        if (DEFAULT.equals(key)) {
            defaultDataSource = dataSource;
        } else {
            dataSources.putIfAbsent(key, dataSource);
        }
    }

    @Override
    public DataSource getDataSource(MappedStatement mappedStatement) {
        String catalog = mappedStatement.getCatalog();
        DataSource dataSource = dataSources.get(catalog);
        if (dataSource != null) {
            return dataSource;
        }
        if (StringUtils.isBlank(catalog)) {
            return defaultDataSource;
        }

        return null;
    }
}
