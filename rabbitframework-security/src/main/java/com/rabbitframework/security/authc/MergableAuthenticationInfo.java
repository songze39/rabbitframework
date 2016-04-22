package com.rabbitframework.security.authc;

/**
 * 合并用户身份信息
 */
public interface MergableAuthenticationInfo extends AuthenticationInfo {
    void merge(AuthenticationInfo info);
}
