package com.rabbitframework.jadb;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.builder.XMLConfigBuilder;
import com.rabbitframework.jadb.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RabbitJadbFactory}
 */
public class RabbitJadbFactoryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RabbitJadbFactoryBuilder.class);

    public RabbitJadbFactory build(Reader reader) {
        return build(reader, null);
    }

    public RabbitJadbFactory build(Reader reader, Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader,
                    properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJadbFactory build(InputStream inputStream) {
        return build(inputStream, null);
    }

    public RabbitJadbFactory build(InputStream inputStream,
                                   Properties properties) {
        try {
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(
                    inputStream, properties);
            return build(xmlConfigBuilder.parse());
        } catch (Exception e) {
            String msg = "Error building SqlSession.";
            logger.error(msg, e);
            throw new PersistenceException(msg, e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public RabbitJadbFactory build(Configuration configuration) {
        return new DefaultRabbitJadbFactory(configuration);
    }
}
