package com.rabbitframework.generator.builder;

import java.util.Properties;

import com.rabbitframework.generator.dataaccess.Environment;
import com.rabbitframework.generator.mapping.type.JavaTypeResolver;
import com.rabbitframework.generator.mapping.type.JavaTypeResolverDefaultImpl;
import com.rabbitframework.generator.template.Template;

public class Configuration {
    private Properties variables;
    private Environment environment;
    private Template template;
    private TableConfiguration tableConfiguration;
    private JavaTypeResolver javaTypeResolver;

    public Configuration() {
        javaTypeResolver = new JavaTypeResolverDefaultImpl();
    }

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

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public JavaTypeResolver getJavaTypeResolver() {
        return javaTypeResolver;
    }
}
