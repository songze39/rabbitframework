package com.rabbitframework.security.spring.web;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

/**
 * 
 * 权限过滤工厂，为spring加载入口类，继承{@link ShiroFilterFactoryBean}
 * 
 * @author justin.liang
 *
 */
public class SecurityFilterFactoryBean extends ShiroFilterFactoryBean {
	public SecurityFilterFactoryBean() {
		super();
	}
}
