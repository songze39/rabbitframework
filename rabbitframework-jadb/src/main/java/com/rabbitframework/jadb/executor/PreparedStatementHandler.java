package com.rabbitframework.jadb.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.rabbitframework.commons.reflect.MetaObject;
import com.rabbitframework.jadb.dataaccess.JdbcTemplateHolder;
import com.rabbitframework.jadb.dataaccess.KeyGenerator;
import com.rabbitframework.jadb.log.ConnectionLogger;
import com.rabbitframework.jadb.mapping.BoundSql;
import com.rabbitframework.jadb.mapping.GenerationType;
import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.SqlCommendType;

public class PreparedStatementHandler implements StatementHandler {
	private MappedStatement mappedStatement;
	private Object parameterObject;
	private BoundSql boundSql;

	public PreparedStatementHandler(MappedStatement mappedStatement,
			Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	@Override
	public BoundSql getBoundSql() {
		return boundSql;
	}

	@Override
	public int update(JdbcTemplateHolder jdbcTemplateHolder) {
		KeyHolder generatedKeyHolder = null;
		int result;
		List<KeyGenerator> keyGenerators = mappedStatement.getKeyGenerators();
		if (keyGenerators.size() > 0
				&& mappedStatement.getSqlCommendType() == SqlCommendType.INSERT) {
			generatedKeyHolder = new GeneratedKeyHolder();
		}
		JdbcTemplate jdbcTemplate = jdbcTemplateHolder
				.getJdbcTempleate(mappedStatement);

		PreparedStatementCreator preparedStatement = createPreparedStatement();
		if (generatedKeyHolder == null) {
			result = jdbcTemplate.update(preparedStatement);
		} else {
			result = jdbcTemplate.update(preparedStatement, generatedKeyHolder);
			Number number = generatedKeyHolder.getKey();
			MetaObject metaObject = mappedStatement.getConfiguration()
					.newMetaObject(parameterObject);
			for (int i = 0; i < keyGenerators.size(); i++) {
				KeyGenerator keyGenerator = keyGenerators.get(i);
				String property = keyGenerator.getProperty();
				if (metaObject.hasSetter(property)) {
					Object key = getKeyValue(number, keyGenerator.getJavaType());
					if (key != null) {
						metaObject.setValue(property, key);
					}
				}
			}
		}
		return result;
	}

	private Object getKeyValue(Number result, Class<?> returnType) {
		if (returnType.isPrimitive()) {
			returnType = ClassUtils.primitiveToWrapper(returnType);
		}
		if (result == null || returnType == void.class) {
			return null;
		}
		if (returnType == result.getClass()) {
			return result;
		}
		// 将结果转成方法的返回类型
		if (returnType == Integer.class) {
			return result.intValue();
		} else if (returnType == Long.class) {
			return result.longValue();
		} else if (returnType == Boolean.class) {
			return result.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
		} else if (returnType == Double.class) {
			return result.doubleValue();
		} else if (returnType == Float.class) {
			return result.floatValue();
		} else if (returnType == Number.class) {
			return result;
		} else if (returnType == String.class
				|| returnType == CharSequence.class) {
			return String.valueOf(result);
		} else {
			return null;
		}
	}

	@Override
	public <E> List<E> query(JdbcTemplateHolder jdbcTemplateHolder) {
		JdbcTemplate jdbcTemplate = jdbcTemplateHolder
				.getJdbcTempleate(mappedStatement);
		PreparedStatementCreator preparedStatement = createPreparedStatement();
		RowMapper<E> rowMapper = mappedStatement.getRowMapper();
		return jdbcTemplate.query(preparedStatement,
				new RowMapperResultSetExtractor<E>(rowMapper));
	}

	@Override
	public PreparedStatementCreator createPreparedStatement() {
		boolean isIdentity = false;
		String[] columnNames = null;
		if (SqlCommendType.INSERT == mappedStatement.getSqlCommendType()) {
			List<KeyGenerator> keyGenerators = mappedStatement
					.getKeyGenerators();
			columnNames = new String[keyGenerators.size()];
			int i = 0;
			for (KeyGenerator keyGenerator : keyGenerators) {
				if (keyGenerator.isIdentity()) {
					isIdentity = true;
					break;
				} else {
					if (GenerationType.SEQUENCE == keyGenerator
							.getGenerationType()) {
						columnNames[i] = keyGenerator.getColumn();
						i++;
					}
				}
			}
		}
		return getPreparedStatementCreator(isIdentity, columnNames);
	}

	private PreparedStatementCreator getPreparedStatementCreator(
			final boolean isIdentity, final String[] keyColumn) {
		final Logger logger = mappedStatement.getStatementLogger();
		final String sql = getBoundSql().getSql();
		final ParameterHandler parameterHandler = mappedStatement
				.getConfiguration().newParameterHandler(mappedStatement,
						parameterObject, boundSql);
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				if (logger.isDebugEnabled()) {
					con = ConnectionLogger.newInstance(con, logger);
				}
				PreparedStatement ps;
				if (isIdentity) {
					ps = con.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
				} else if (keyColumn != null && keyColumn.length > 0) {
					ps = con.prepareStatement(sql, keyColumn);
				} else {
					ps = con.prepareStatement(sql);
				}
				parameterHandler.setParameters(ps);
				return ps;
			}
		};
		return creator;
	}
}
