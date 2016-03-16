package com.rabbitframework.security.authc.pam;

import com.rabbitframework.security.authc.AbstractAuthenticator;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.exceptions.AuthenticationException;
import com.rabbitframework.security.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class ModularRealmAuthenticator extends AbstractAuthenticator {
    private static final Logger log = LoggerFactory.getLogger(ModularRealmAuthenticator.class);

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    /**
     * List of realms that will be iterated through when a user authenticates.
     */
    private Collection<Realm> realms;
    /**
     * Sets all realms used by this Authenticator, providing PAM (Pluggable Authentication Module) configuration.
     *
     * @param realms the realms to consult during authentication attempts.
     */
    public void setRealms(Collection<Realm> realms) {
        this.realms = realms;
    }

    /**
     * Returns the realm(s) used by this {@code Authenticator} during an authentication attempt.
     *
     * @return the realm(s) used by this {@code Authenticator} during an authentication attempt.
     */
    protected Collection<Realm> getRealms() {
        return this.realms;
    }
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException {
        return null;
    }
}
