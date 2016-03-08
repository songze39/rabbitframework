package com.rabbitframework.security.authc;

/**
 * 认证的token接口类
 *
 * @author justin.liang
 */
public interface AuthenticationToken extends java.io.Serializable {
    /**
     * 获取身份认证信息
     *
     * @return
     */
    Object getPrincipal();

    /**
     * 获取用户身份认证证书
     *
     * @return
     */
    Object getCredentials();
}
