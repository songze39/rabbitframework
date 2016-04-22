/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.rabbitframework.security.authc.pam;

import com.rabbitframework.security.authc.*;
import com.rabbitframework.security.realm.Realm;
import com.rabbitframework.security.subject.PrincipalCollection;
import com.rabbitframework.security.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * {@link Authenticator}实现,用于多Realm的身份验证,该操作通过{@link AuthenticationStrategy}来完成。
 * 默认策略为{@link AtLeastOneSuccessfulStrategy}
 *
 * @see #setRealms
 * @see AtLeastOneSuccessfulStrategy
 * @see AllSuccessfulStrategy
 * @see FirstSuccessfulStrategy
 */
public class ModularRealmAuthenticator extends AbstractAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(ModularRealmAuthenticator.class);

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    /**
     * List of realms that will be iterated through when a user authenticates.
     */
    private Collection<Realm> realms;

    private AuthenticationStrategy authenticationStrategy;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    public ModularRealmAuthenticator() {
        this.authenticationStrategy = new AtLeastOneSuccessfulStrategy();
    }

    public void setRealms(Collection<Realm> realms) {
        this.realms = realms;
    }

    protected Collection<Realm> getRealms() {
        return this.realms;
    }


    public AuthenticationStrategy getAuthenticationStrategy() {
        return authenticationStrategy;
    }

    public void setAuthenticationStrategy(AuthenticationStrategy authenticationStrategy) {
        this.authenticationStrategy = authenticationStrategy;
    }

    /**
     * 判断{@link Realm}s是否初始化
     *
     * @throws IllegalStateException
     */
    protected void assertRealmsConfigured() throws IllegalStateException {
        Collection<Realm> realms = getRealms();
        if (CollectionUtils.isEmpty(realms)) {
            String msg = "Configuration error:  No realms have been configured!  One or more realms must be " +
                    "present to execute an authentication attempt.";
            throw new IllegalStateException(msg);
        }
    }

    /**
     * 单{@link Realm}情况下执行
     *
     * @param realm
     * @param token
     * @return
     */
    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
        if (!realm.supports(token)) {
            String msg = "Realm [" + realm + "] does not support authentication token [" +
                    token + "].  Please ensure that the appropriate Realm implementation is " +
                    "configured correctly or that the realm accepts AuthenticationTokens of this type.";
            throw new UnsupportedTokenException(msg);
        }
        AuthenticationInfo info = realm.getAuthenticationInfo(token);
        if (info == null) {
            String msg = "Realm [" + realm + "] was unable to find account data for the " +
                    "submitted AuthenticationToken [" + token + "].";
            throw new UnknownAccountException(msg);
        }
        return info;
    }

    /**
     * 多{@link Realm}s情况下执行
     *
     * @param realms
     * @param token
     * @return
     */
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {

        AuthenticationStrategy strategy = getAuthenticationStrategy();

        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        if (logger.isTraceEnabled()) {
            logger.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        for (Realm realm : realms) {

            aggregate = strategy.beforeAttempt(realm, token, aggregate);

            if (realm.supports(token)) {

                logger.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

                AuthenticationInfo info = null;
                Throwable t = null;
                try {
                    info = realm.getAuthenticationInfo(token);
                } catch (Throwable throwable) {
                    t = throwable;
                    if (logger.isDebugEnabled()) {
                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                        logger.debug(msg, t);
                    }
                }
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);

            } else {
                logger.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }

        aggregate = strategy.afterAllAttempts(token, aggregate);

        return aggregate;
    }

    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        Collection<Realm> realms = getRealms();
        if (realms.size() == 1) {
            return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
        } else {
            return doMultiRealmAuthentication(realms, authenticationToken);
        }
    }

    public void onLogout(PrincipalCollection principals) {
        super.onLogout(principals);
        Collection<Realm> realms = getRealms();
        if (!CollectionUtils.isEmpty(realms)) {
            for (Realm realm : realms) {
                if (realm instanceof LogoutAware) {
                    ((LogoutAware) realm).onLogout(principals);
                }
            }
        }
    }
}
