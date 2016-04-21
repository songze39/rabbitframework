package com.rabbitframework.security.mgt;

import com.rabbitframework.security.authc.Authenticator;

/**
 * @author: justin.liang
 * @date: 16/4/21 下午10:13
 */
public abstract class AuthenticatingSecurityManager extends RealmSecurityManager {
    private Authenticator authenticator;

    public AuthenticatingSecurityManager() {
        super();
//        this.authenticator =
    }
}
