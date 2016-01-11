package com.rabbitframework.jadb.executor;

import com.rabbitframework.jadb.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jadb.mapping.BoundSql;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.PreparedStatement;
import java.util.List;

public interface StatementHandler {
    BoundSql getBoundSql();

    int update(JdbcTemplateHolder jdbcTemplateHolder);

    <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder);

    PreparedStatementCreator createPreparedStatement();

}
