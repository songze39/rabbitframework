package com.rabbitframework.jadb.spring;

import static org.springframework.util.Assert.notNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.jadb.RabbitJadbFactory;
import com.rabbitframework.jadb.RabbitJadbFactoryBuilder;
import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.builder.XMLConfigBuilder;
import com.rabbitframework.jadb.cache.Cache;
import com.rabbitframework.jadb.dataaccess.Environment;
import com.rabbitframework.jadb.dataaccess.datasource.DataSourceFactory;

public class RabbitJadbFactoryBean
		implements FactoryBean<RabbitJadbFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
	private static final Logger logger = LoggerFactory.getLogger(RabbitJadbFactoryBean.class);
	private RabbitJadbFactoryBuilder rabbitJadbFactoryBuilder = new RabbitJadbFactoryBuilder();
	private RabbitJadbFactory rabbitJadbFactory;
	private Resource configLocation;
	private Properties configurationProperties;
	private DataSourceFactory dataSourceFactory;
	private Map<String, DataSource> dataSourceMap;
	private Map<String, Cache> cacheMap;
	private String entityPackages;
	private String mapperPackages;
	private boolean failFast;

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public void setRabbitJadbFactoryBuilder(RabbitJadbFactoryBuilder rabbitJadbFactoryBuilder) {
		this.rabbitJadbFactoryBuilder = rabbitJadbFactoryBuilder;
	}

	public void setConfigurationProperties(Properties configurationProperties) {
		this.configurationProperties = configurationProperties;
	}

	public void setCacheMap(Map<String, Cache> cacheMap) {
		this.cacheMap = cacheMap;
	}

	public void setEntityPackages(String entityPackages) {
		this.entityPackages = entityPackages;
	}

	public void setMapperPackages(String mapperPackages) {
		this.mapperPackages = mapperPackages;
	}

	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	@Override
	public RabbitJadbFactory getObject() throws Exception {
		if (this.rabbitJadbFactory == null) {
			afterPropertiesSet();
		}
		return this.rabbitJadbFactory;
	}

	@Override
	public Class<? extends RabbitJadbFactory> getObjectType() {
		return this.rabbitJadbFactory == null ? RabbitJadbFactory.class : this.rabbitJadbFactory.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(dataSourceMap, "Property 'dataSourceMap' is required");
		notNull(rabbitJadbFactoryBuilder, "Property 'RabbitJadbFactoryBuilder' is required");
		notNull(dataSourceFactory, "Property 'dataSourceFactory' is required");
		this.rabbitJadbFactory = buildRabbitJadbFactory();
	}

	public RabbitJadbFactory buildRabbitJadbFactory() throws Exception {
		Configuration configuration = null;
		XMLConfigBuilder configBuilder = null;
		if (configLocation != null) {
			configBuilder = new XMLConfigBuilder(configLocation.getInputStream(), configurationProperties);
			configuration = configBuilder.getConfiguration();
		} else {
			configuration = new Configuration();
			configuration.setVariables(configurationProperties);
		}
		if (configBuilder != null) {
			try {
				configBuilder.parse();
				logger.trace("Parsed configuration file: '" + this.configLocation + "'");
			} catch (Exception ex) {
				throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
			}
		}
		Environment environment = new Environment();
		for (Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
			String name = entry.getKey();
			DataSource dataSource = entry.getValue();
			dataSourceFactory.addDataSource(name, dataSource);
			environment.addCacheDataSource(dataSource);
		}
		environment.setDataSourceFactory(dataSourceFactory);
		String[] entityPackageNames = StringUtils.tokenizeToStringArray(entityPackages);
		configuration.addEntitys(entityPackageNames);
		String[] mapperPackageNames = StringUtils.tokenizeToStringArray(mapperPackages);
		configuration.addMappers(mapperPackageNames);
		configuration.addCaches(cacheMap);
		configuration.setEnvironment(environment);
		return rabbitJadbFactoryBuilder.build(configuration);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (failFast && event instanceof ContextRefreshedEvent) {
			this.rabbitJadbFactory.getConfiguration().getMappedStatementNames();
		}
	}
}
