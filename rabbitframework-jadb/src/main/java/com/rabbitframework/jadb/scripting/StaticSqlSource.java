package com.rabbitframework.jadb.scripting;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.ParameterMapping;
import com.rabbitframework.jadb.mapping.RowBounds;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;


    public StaticSqlSource(Configuration configuration, String sql,
                           List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    public BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds) {
        return new BoundSql(configuration, sql, parameterMappings,
                parameterObject);
    }

}
