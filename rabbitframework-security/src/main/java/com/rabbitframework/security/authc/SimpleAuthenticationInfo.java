package com.rabbitframework.security.authc;

import com.rabbitframework.commons.codec.ByteSource;
import com.rabbitframework.security.subject.PrincipalCollection;

/**
 * Simple implementation of the {@link MergableAuthenticationInfo} interface that holds the principals and
 * credentials.
 *
 * @see AuthenticatingRealm
 * @since 0.9
 */
public class SimpleAuthenticationInfo implements MergableAuthenticationInfo, SaltedAuthenticationInfo {
    @Override
    public void merge(AuthenticationInfo info) {

    }

    @Override
    public ByteSource getCredentialsSalt() {
        return null;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
