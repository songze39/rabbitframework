package com.rabbitframework.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import com.rabbitframework.generator.dataaccess.DatabaseIntrospector;
import com.rabbitframework.generator.mapping.EntityMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.generator.builder.Configuration;
import com.rabbitframework.generator.exceptions.GeneratorException;

public class RabbitGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RabbitGenerator.class);
    private Configuration configuration;

    public RabbitGenerator(Configuration configuration) {
        this.configuration = configuration;
    }

    public void generator() {
        Connection connection = null;
        try {
            connection = configuration.getEnvironment().getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(metaData, configuration);
            List<EntityMapping> entityMappings = databaseIntrospector.introspectTables();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GeneratorException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
