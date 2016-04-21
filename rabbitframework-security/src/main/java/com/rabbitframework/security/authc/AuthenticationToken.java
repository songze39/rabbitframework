package com.rabbitframework.security.authc;

/**
 * 认证令牌，获取用户登陆提交的身份(如用户名)及凭据(如密码)
 *
 * @author: justin.liang
 * @date: 16/4/19 下午11:12
 */
public interface AuthenticationToken {
    /**
     * 获取当前身份
     *
     * @return
     */
    Object getPrincipal();

    /**
     * 获取当前凭据
     *
     * @return
     */
    Object getCredentials();
}
