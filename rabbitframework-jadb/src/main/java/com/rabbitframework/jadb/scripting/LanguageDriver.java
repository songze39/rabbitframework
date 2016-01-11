package com.rabbitframework.jadb.scripting;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.executor.ParameterHandler;
import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.MappedStatement;


public interface LanguageDriver {
    /**
     * 创建参数处理类{@link com.rabbitframework.jadb.executor.DefaultParameterHandler}
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @return
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement,
                                            Object parameterObject, BoundSql boundSql);

    /**
     * 创建{@link SqlSource}
     *
     * @param configuration
     * @param sqlScript
     * @return
     */
    SqlSource createSqlSource(Configuration configuration, String sqlScript);
}
