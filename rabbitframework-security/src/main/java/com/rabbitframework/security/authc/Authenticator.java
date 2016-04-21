package com.rabbitframework.security.authc;

/**
 * 认证器,负责主体认证的,这是一个扩展点,如果用户觉得Security默认的不好,
 * 可以自定义实现;其需要认证策略(Authentication Strategy),即什么情况下算用户认证通过了;
 *
 * @author: justin.liang
 * @date: 16/4/19 下午11:03
 */
public interface Authenticator {
    /**
     * 对一个用户进行身份验证的基础上提交{@code AuthenticationToken}.
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationInfo authenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException;
}
