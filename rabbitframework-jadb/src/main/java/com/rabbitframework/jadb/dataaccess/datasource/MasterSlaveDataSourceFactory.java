package com.rabbitframework.jadb.dataaccess.datasource;

import javax.sql.DataSource;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.SqlCommendType;

/**
 * 主从数据源配置,所有写操作使用master,读操作使用slave 允许有多master/salve,使用
 * {@link RandomDataSourceFactory}随机获取{@link DataSourceFactory}
 * 在多master/salve数据源情况下，主要通过key来区分,key中只要包含有master/salve字符,程序将自动匹配
 * 当不匹配master/savle时，将默认同时都可以使用二者
 */
public class MasterSlaveDataSourceFactory implements DataSourceFactory {
    private final static String MASTER = "master";
    private final static String SLAVE = "slave";
    private DataSourceFactory masters = new RandomDataSourceFactory();
    private DataSourceFactory slaves = new RandomDataSourceFactory();

    @Override
    public void addDataSource(String key, DataSource dataSource) {
        if (StringUtils.isEmpty(key) || dataSource == null) {
            throw new NullPointerException("DataSource add fail: key=" + key
                    + ",DataDataSource=" + dataSource);
        }
        String name = key.toLowerCase();
        if (name.contains(MASTER)) {
            masters.addDataSource(key, dataSource);
        } else if (name.contains(SLAVE)) {
            slaves.addDataSource(key, dataSource);
        } else {
            int index = name.lastIndexOf(MASTER);
            int index1 = name.lastIndexOf(SLAVE);
            if (index == -1 && index1 == -1) {
                slaves.addDataSource(key, dataSource);
                masters.addDataSource(key, dataSource);
            }
        }
    }

    @Override
    public DataSource getDataSource(MappedStatement mappedStatement) {
        SqlCommendType sqlCommendType = mappedStatement.getSqlCommendType();
        if (sqlCommendType == SqlCommendType.SELECT) {
            return slaves.getDataSource(mappedStatement);
        } else {
            return masters.getDataSource(mappedStatement);
        }

    }

}
