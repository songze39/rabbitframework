package com.rabbitframework.security.authc;

import com.rabbitframework.security.crypto.codec.ByteSource;

/**
 * 加盐身份验证信息
 */
public interface SaltedAuthenticationInfo extends AuthenticationInfo {
    /**
     * 获取用户加盐证书
     *
     * @return
     */
    ByteSource getCredentialsSalt();
}
