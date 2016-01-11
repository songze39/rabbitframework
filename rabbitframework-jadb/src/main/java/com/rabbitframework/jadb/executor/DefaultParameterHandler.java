package com.rabbitframework.jadb.executor;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.rabbitframework.commons.reflect.MetaObject;
import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.ParameterMapping;

import org.apache.commons.lang.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

public class DefaultParameterHandler implements ParameterHandler {
	private Logger log = LogManager.getLogger(DefaultParameterHandler.class);
	private Configuration configuration;
	private final Object parameterObject;
	private BoundSql boundSql;

	public DefaultParameterHandler(MappedStatement mappedStatement,
			Object parameterObject, BoundSql boundSql) {
		this.parameterObject = parameterObject;
		configuration = mappedStatement.getConfiguration();
		this.boundSql = boundSql;
	}

	@SuppressWarnings("unchecked")
	public void setParameters(PreparedStatement ps) throws SQLException {
		try {
			List<ParameterMapping> parameterMappings = boundSql
					.getParameterMappings();
			if (parameterMappings != null) {
				for (int i = 0; i < parameterMappings.size(); i++) {
					ParameterMapping parameterMapping = parameterMappings
							.get(i);
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = "";
					} else {
						Class<?> type = parameterObject.getClass();
						boolean isPrimitive = isColumnType(type);
						if (isPrimitive) {
							value = parameterObject;
						} else {
							MetaObject metaObject = configuration
									.newMetaObject(parameterObject);
							value = metaObject.getValue(propertyName);
						}
					}
					StatementCreatorUtils.setParameterValue(ps, i + 1,
							SqlTypeValue.TYPE_UNKNOWN, value);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SQLException(e.getMessage(), e);
		}
	}

	private boolean isColumnType(Class<?> columnTypeCandidate) {
		return String.class == columnTypeCandidate
				|| org.springframework.util.ClassUtils
						.isPrimitiveOrWrapper(columnTypeCandidate);
	}
}