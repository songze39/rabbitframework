package com.rabbitframework.jadb.executor;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.RowBounds;

import java.util.List;

/**
 * 执行
 */
public class SimpleExecutor implements Executor {
    private Configuration configuration;
    private JdbcTemplateHolder jdbcTemplateHolder;

    public SimpleExecutor(Configuration configuration, JdbcTemplateHolder jdbcTemplateHolder) {
        this.configuration = configuration;
        this.jdbcTemplateHolder = jdbcTemplateHolder;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) {
        BoundSql boundSql = ms.getBoundSql(parameter, null);
        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter, boundSql);
        return statementHandler.update(jdbcTemplateHolder);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) {
        BoundSql boundSql = ms.getBoundSql(parameter, rowBounds);
        StatementHandler statementHandler = configuration.newStatementHandler(ms, parameter, boundSql);
        return statementHandler.query(jdbcTemplateHolder);
    }
}
