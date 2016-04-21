package com.rabbitframework.security.authc;

import com.rabbitframework.security.subject.PrincipalCollection;

/**
 * 聚合认证信息接口
 *
 * @author: justin.liang
 * @date: 16/4/19 下午11:06
 */
public interface AuthenticationInfo extends java.io.Serializable {
    /**
     * 获取身份集合
     *
     * @return
     */
    PrincipalCollection getPrincipals();

    /**
     * 获取凭据信息,主要指用户密码
     *
     * @return
     */
    Object getCredentials();
}
