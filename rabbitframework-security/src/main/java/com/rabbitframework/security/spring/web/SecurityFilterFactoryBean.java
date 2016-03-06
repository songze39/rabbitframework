package com.rabbitframework.security.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限过虑工厂,实现
 * {@link FactoryBean}与{@link BeanPostProcessor}
 * spring配置基本入口
 *
 * @author justin.liang
 */
public class SecurityFilterFactoryBean implements FactoryBean, BeanPostProcessor {
    private static transient final Logger log = LoggerFactory.getLogger(SecurityFilterFactoryBean.class);
    private Map<String, Filter> filters;
    private Map<String, String> filterChainDefinitionMap;
    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;

    public SecurityFilterFactoryBean() {
        filters = new LinkedHashMap<>();
        //order matters
        filterChainDefinitionMap = new LinkedHashMap<>();
    }

    /**
     * {@link FactoryBean getObject()｝实现
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    /**
     * {@link FactoryBean getObjectTpye()} 实现
     *
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return null;
    }

    /**
     * {@link FactoryBean isSingleton()} 实现
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@link BeanPostProcessor postProcessBeforeInitialization()} 实现
     *
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return null;
    }

    /**
     * {@link BeanPostProcessor postProcessAfterInitialization()} 实现
     *
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return null;
    }
}
