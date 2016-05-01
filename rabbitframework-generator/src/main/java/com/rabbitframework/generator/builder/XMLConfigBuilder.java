package com.rabbitframework.generator.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.rabbitframework.generator.template.JavaModeGenerate;
import com.rabbitframework.generator.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.utils.ResourceUtils;
import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.commons.xmlparser.XNode;
import com.rabbitframework.commons.xmlparser.XPathParser;
import com.rabbitframework.generator.dataaccess.Environment;
import com.rabbitframework.generator.exceptions.BuilderException;

public class XMLConfigBuilder extends BaseBuilder {
	private static final Logger logger = LoggerFactory.getLogger(XMLConfigBuilder.class);
	private boolean parsed;
	private XPathParser xPathParser;

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
		super(new Configuration());
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
			dataSourceElement(root.evalNode("dataSource"));
			generatorsElement(root.evalNode("generators"));
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

	private void dataSourceElement(XNode dataAccessNode) throws Exception {
		if (dataAccessNode == null) {
			return;
		}
		Environment environment = new Environment();
		Properties variables = configuration.getVariables();
		String type = dataAccessNode.getStringAttribute("type");
		String dataSourceClazz = dataAccessNode.getStringAttribute("class");
		Properties properties = dataAccessNode.getChildrenAsProperties();
		DataSource dataSource = (DataSource) resolveClass(dataSourceClazz).newInstance();
		PropertiesConvert.setProperties(properties, dataSource, variables);
		environment.setDataSource(dataSource);
		environment.setType(type);
		configuration.setEnvironment(environment);
	}

	private void generatorsElement(XNode generators) {
		if (generators == null) {
			return;
		}
		Template template = new Template();
		List<XNode> xnode = generators.evalNodes("generator");
		for (XNode cXnode : xnode) {
			JavaModeGenerate javaModeGenerate = new JavaModeGenerate();
			String templatePath = cXnode.getStringAttribute("templatePath");
			String targetPackage = cXnode.getStringAttribute("targetPackage");
			String targetProject = cXnode.getStringAttribute("targetProject");
			javaModeGenerate.setTargetPackage(targetPackage);
			javaModeGenerate.setTargetProject(targetProject);
			javaModeGenerate.setTemplatePath(templatePath);
			template.put(javaModeGenerate);
		}
		configuration.setTemplate(template);
	}
}
