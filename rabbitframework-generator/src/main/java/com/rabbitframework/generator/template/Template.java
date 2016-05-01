package com.rabbitframework.generator.template;

import com.rabbitframework.commons.org.springframework.io.Resource;
import com.rabbitframework.commons.utils.ResourceUtils;
import com.rabbitframework.generator.exceptions.GeneratorException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * @author: justin.liang
 * @date: 16/4/30 下午11:26
 */
public class Template {
    private static final Logger logger = LoggerFactory.getLogger(Template.class);
    public Map<String, JavaModeGenerate> templateMapping;
    private Configuration configuration;
    private StringTemplateLoader templateLoader;

    public Template() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        // configuration.setObjectWrapper(new
        // DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        templateMapping = new HashMap<String, JavaModeGenerate>();
        templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
    }

    public void put(JavaModeGenerate javaModeGenerate) {
        try {
            Resource resource = ResourceUtils.getResource(javaModeGenerate.getTemplatePath());
            String fileName = resource.getFilename();
            String value = IOUtils.toString(resource.getInputStream());
            templateLoader.putTemplate(fileName, value);
            templateMapping.put(fileName, javaModeGenerate);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GeneratorException(e);
        }
    }

    public Map<String, JavaModeGenerate> getTemplateMapping() {
        return Collections.unmodifiableMap(templateMapping);
    }

    public void printToConsole(Object obj, String fileName) throws Exception {
        freemarker.template.Template template = configuration.getTemplate(fileName);
        template.process(obj, new OutputStreamWriter(System.out));
    }

}
