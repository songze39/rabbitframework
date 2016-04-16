package com.rabbitframework.generator.test.freemarker;

import com.rabbitframework.commons.org.springframework.io.Resource;
import com.rabbitframework.commons.utils.ResourceUtils;
import com.rabbitframework.generator.builder.Model;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class GeneratorConfig {
    Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(GeneratorConfig.class);

    public GeneratorConfig() {
        before();
    }

    public void before() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
//        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        try {
//            File resource = ResourceUtils.getResourceAsFile("/");
//            logger.debug("path:" + resource.getAbsolutePath());
            StringTemplateLoader templateLoader = new StringTemplateLoader();
            Resource[] resources = ResourceUtils.getResources("classpath*:/template/*.ftl");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                String value = IOUtils.toString(resource.getInputStream());
                templateLoader.putTemplate(fileName, value);
            }
//            templateLoader.putTemplate();
            configuration.setTemplateLoader(templateLoader);
//            configuration.setDirectoryForTemplateLoading(resource);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void simple() {
        try {
            Template template = configuration.getTemplate("simple.ftl");
            Map map = new HashMap();
            map.put("user", "rabbit");
            template.process(map, new OutputStreamWriter(System.out));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void list() {
        try {
            Template template = configuration.getTemplate("list.ftl");
            List<com.rabbitframework.generator.builder.Model> models = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                com.rabbitframework.generator.builder.Model model = new Model();
                model.setId(i);
                model.setName("list:" + i);
                models.add(model);
            }
            Map map = new HashMap();
            map.put("modelList", models);
            template.process(map, new OutputStreamWriter(System.out));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Properties variables;

    public Properties getVariables() {
        return variables;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }
}
