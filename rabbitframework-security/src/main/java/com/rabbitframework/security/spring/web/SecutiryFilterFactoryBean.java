package com.rabbitframework.security.spring.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import javax.servlet.Filter;
import java.util.Map;

/**
 * TODO 待开发
 *
 * security权限框架spring注入工厂类,主要定义了框架过滤器,实现{@link FactoryBean}、
 * {@link BeanPostProcessor}
 * <h4>使用说明</h4> 在{@code web.xml}中声明DelegatingFilterProxy, 过滤器名称与bean的id一致:
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;<b>securityFilter</b>&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.springframework.web.filter.DelegatingFilterProxy&lt;filter-class&gt;
 *   &lt;init-param&gt;
 *    &lt;param-name&gt;targetFilterLifecycle&lt;/param-name&gt;
 *     &lt;param-value&gt;true&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * 
 * Then, in your spring XML file that defines your web ApplicationContext:
 * 
 * <pre>
 * &lt;bean id="<b>securityFilter</b>" class="org.apache.shiro.spring.web.SecurityFilterFactoryBean"&gt;
 *    &lt;property name="securityManager" ref="securityManager"/&gt;
 *    &lt;!-- other properties as necessary ... --&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <h4>过虑器注入</h4> While there is a {@link #setFilters(Map) filters} property
 * that allows you to assign a filter beans to the 'pool' of filters available
 * when defining {@link #setFilterChainDefinitions(String) filter chains}, it is
 * optional.
 * <p/>
 * This implementation is also a {@link BeanPostProcessor} and will acquire any
 * {@link Filter Filter} beans defined independently in your Spring application
 * context. Upon discovery, they will be automatically added to the
 * {@link #setFilters(Map) map} keyed by the bean ID. That ID can then be used
 * in the filter chain definitions, for example:
 * <p>
 * 
 * <pre>
 * &lt;bean id="<b>myCustomFilter</b>" class="com.class.that.implements.javax.servlet.Filter"/&gt;
 * ...
 * &lt;bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean"&gt;
 *    ...
 *    &lt;property name="filterChainDefinitions"&gt;
 *        &lt;value&gt;
 *            /some/path/** = authc, <b>myCustomFilter</b>
 *        &lt;/value&gt;
 *    &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <h4>Global Property Values</h4> Most Shiro servlet Filter implementations
 * exist for defining custom Filter {@link #setFilterChainDefinitions(String)
 * chain definitions}. Most implementations subclass one of the
 * {@link AccessControlFilter}, {@link AuthenticationFilter},
 * {@link AuthorizationFilter} classes to simplify things, and each of these 3
 * classes has configurable properties that are application-specific.
 * <p/>
 * A dilemma arises where, if you want to for example set the application's
 * 'loginUrl' for any Filter, you don't want to have to manually specify that
 * value for <em>each</em> filter instance definied.
 * <p/>
 * To prevent configuration duplication, this implementation provides the
 * following properties to allow you to set relevant values in only one place:
 * <ul>
 * <li>{@link #setLoginUrl(String)}</li>
 * <li>{@link #setSuccessUrl(String)}</li>
 * <li>{@link #setUnauthorizedUrl(String)}</li>
 * </ul>
 *
 * @author: justin.liang
 * @date: 16/4/18 下午10:07
 * @see org.springframework.web.filter.DelegatingFilterProxy
 *      DelegatingFilterProxy
 */
public class SecutiryFilterFactoryBean implements FactoryBean<Object>, BeanPostProcessor {
	/**
	 * 实现{@link FactoryBean}
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getObject() throws Exception {
		return null;
	}

	/**
	 * 实现{@link FactoryBean}
	 *
	 * @return
	 */
	@Override
	public Class<?> getObjectType() {
		return null;
	}

	/**
	 * 实现{@link FactoryBean}
	 *
	 * @return
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * 初始化之前处理,实现{@link BeanPostProcessor}
	 *
	 * @param o
	 * @param s
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
		return null;
	}

	/**
	 * 初始化之后处理,实现{@link BeanPostProcessor}
	 *
	 * @param o
	 * @param s
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
		return null;
	}
}
