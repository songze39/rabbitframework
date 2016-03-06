package com.rabbitframework.jadb.builder;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.rabbitframework.jadb.cache.Cache;
import com.rabbitframework.jadb.dataaccess.KeyGenerator;
import com.rabbitframework.jadb.mapping.MappedStatement;
import com.rabbitframework.jadb.mapping.SqlCommendType;
import com.rabbitframework.jadb.scripting.LanguageDriver;
import com.rabbitframework.jadb.scripting.SqlSource;

public class MapperBuilderAssistant extends BaseBuilder {
	private String catalog;

	public MapperBuilderAssistant(Configuration configuration) {
		super(configuration);
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public void addMappedStatement(String mappedStatementId,
			SqlCommendType sqlCommendType, Cache cache, String[] cacheKey,
			SqlSource sqlSource, LanguageDriver languageDriver,
			List<KeyGenerator> keyGenerators, RowMapper rowMapper) {
		MappedStatement.Builder statementBuilder = new MappedStatement.Builder(
				configuration, mappedStatementId, sqlCommendType, catalog);
		statementBuilder.cacheKey(cacheKey);
		statementBuilder.cache(cache);
		statementBuilder.sqlSource(sqlSource);
		statementBuilder.languageDriver(languageDriver);
		statementBuilder.keyGenerators(keyGenerators);
		statementBuilder.rowMapper(rowMapper);
		configuration.addMappedStatement(statementBuilder.build());
	}

}
