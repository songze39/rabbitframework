package com.rabbitframework.security.realm;

import com.rabbitframework.security.authc.AuthenticationException;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;

/**
 * 对应用程序提供的安全接口,用于获取用户,角色和权限相关的信息
 *
 * @author: justin.liang
 * @date: 16/4/21 下午9:43
 */
public interface Realm {
    String getName();

    boolean supports(AuthenticationToken token);

    AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;
}
