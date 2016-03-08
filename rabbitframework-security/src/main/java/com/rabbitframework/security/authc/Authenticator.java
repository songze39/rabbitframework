package com.rabbitframework.security.authc;

import com.rabbitframework.security.exceptions.AuthenticationException;

/**
 * 权限认证接口
 * @author justin.liang
 */
public interface Authenticator {
    /**
     * 对一个用户进行身份认证。
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationInfo authenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException;
}
