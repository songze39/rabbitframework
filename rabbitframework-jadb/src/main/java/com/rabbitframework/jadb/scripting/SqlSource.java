package com.rabbitframework.jadb.scripting;

import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.RowBounds;

public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject, RowBounds rowBounds);
}
