package com.rabbitframework.security.authc;

/**
 * 地址授权领牌继承{@link AuthenticationToken}
 */
public interface HostAuthenticationToken extends AuthenticationToken {
    String getHost();
}
