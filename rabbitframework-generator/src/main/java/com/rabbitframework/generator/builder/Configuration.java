package com.rabbitframework.generator.builder;

import java.util.Properties;

import com.rabbitframework.generator.dataaccess.Environment;

public class Configuration {
	private Properties variables;
	private Environment environment;

	public Properties getVariables() {
		return variables;
	}

	public void setVariables(Properties variables) {
		this.variables = variables;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

}
