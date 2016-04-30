package com.rabbitframework.generator.builder;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.utils.ResourceUtils;

import junit.framework.TestCase;

/**
 * xml初始化测试
 *
 * @author Justin
 */
public class ConfigBuilderTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(ConfigBuilderTest.class);

	public void testConfig() throws IOException {
		Reader reader = ResourceUtils.getResourceAsReader("generator-config.xml");
		XMLConfigBuilder configBuilder = new XMLConfigBuilder(reader);
		Configuration configuration = configBuilder.parse();
		reader.close();
		logger.debug(configuration.getVariables().size() + "");
		logger.debug("datasource:" + configuration.getEnvironment().getDataSource());
	}
}