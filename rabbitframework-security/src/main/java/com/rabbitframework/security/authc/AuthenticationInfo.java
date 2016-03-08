package com.rabbitframework.security.authc;

import com.rabbitframework.security.subject.PrincipalCollection;

/**
 * 用户认证信息接口
 *
 * @author justin.liang
 */
public interface AuthenticationInfo extends java.io.Serializable {
    /**
     * 获取与认证信息相关的主题集合
     *
     * @return
     */
    PrincipalCollection getPrincipals();

    /**
     * 获取认证信息相关的凭证与相应的主题
     *
     * @return
     */
    Object getCredentials();
}
