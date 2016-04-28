package com.rabbitframework.generator.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.utils.ResourceUtils;
import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.commons.xmlparser.XNode;
import com.rabbitframework.commons.xmlparser.XPathParser;
import com.rabbitframework.generator.exceptions.BuilderException;

public class XMLConfigBuilder {
	private static final Logger logger = LoggerFactory.getLogger(XMLConfigBuilder.class);
	private boolean parsed;
	private XPathParser xPathParser;
	protected final Configuration configuration;

	public XMLConfigBuilder(Reader reader) {
		this(reader, null);
	}

	public XMLConfigBuilder(Reader reader, Properties properties) {
		this(new XPathParser(reader, false, properties, null), properties);
	}

	public XMLConfigBuilder(InputStream inputStream) {
		this(inputStream, null);
	}

	public XMLConfigBuilder(InputStream inputStream, Properties properties) {
		this(new XPathParser(inputStream, false, properties, null), properties);
	}

	private XMLConfigBuilder(XPathParser xPathParser, Properties pro) {
		configuration = new Configuration();
		this.parsed = false;
		this.xPathParser = xPathParser;
		configuration.setVariables(pro);
	}

	public Configuration parse() {
		if (parsed) {
			logger.error("Each XMLConfigBuilder can only be used once.");
			throw new BuilderException("Each XMLConfigBuilder can only be used once.");
		}
		parsed = true;
		parseConfiguration(xPathParser.evalNode("/configuration"));
		return configuration;
	}

	private void parseConfiguration(XNode root) {
		try {
			propertiesElement(root.evalNode("properties"));
		} catch (Exception e) {
			logger.error("Error parsing generator Configuration. Cause: " + e, e);
			throw new BuilderException("Error parsing generator Configuration. Cause: " + e, e);
		}
	}

	private void propertiesElement(XNode pro) throws Exception {
		if (pro == null) {
			return;
		}
		Properties properties = pro.getChildrenAsProperties();
		String resource = pro.getStringAttribute("resource");
		if (StringUtils.isNotBlank(resource)) {
			Properties propertiesResource = ResourceUtils.getResourceAsProperties(resource);
			properties.putAll(propertiesResource);
		}
		Properties variables = configuration.getVariables();
		if (variables != null) {
			properties.putAll(variables);
		}
		xPathParser.setVariables(properties);
		configuration.setVariables(properties);
	}
}
