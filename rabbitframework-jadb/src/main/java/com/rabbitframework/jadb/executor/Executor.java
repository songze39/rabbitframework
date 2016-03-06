package com.rabbitframework.jadb.executor;


import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.RowBounds;

import java.util.List;

public interface Executor {

    int update(MappedStatement ms, Object parameter);

    <E> List<E> query(MappedStatement ms, Object parameter,RowBounds rowBounds);
}
