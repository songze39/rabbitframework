package com.rabbitframework.generator.builder;

import com.rabbitframework.commons.xmlparser.XPathParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class XMLConfigBuilder {
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
		this.parsed = false;
		this.xPathParser = xPathParser;
	}
}
