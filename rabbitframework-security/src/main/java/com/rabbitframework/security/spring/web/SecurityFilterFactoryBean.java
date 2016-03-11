package com.rabbitframework.security.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.rabbitframework.security.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限过虑工厂实现
 * {@link FactoryBean}与{@link BeanPostProcessor}
 * spring配置基本入口
 *
 * @author justin.liang
 */
public class SecurityFilterFactoryBean implements FactoryBean, BeanPostProcessor {
    private static transient final Logger log = LoggerFactory.getLogger(SecurityFilterFactoryBean.class);
    private SecurityManager securityManager;
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

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }
}
