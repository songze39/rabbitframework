package com.rabbitframework.security.session.mgt;

import com.rabbitframework.security.cache.CacheManager;
import com.rabbitframework.security.cache.CacheManagerAware;
import com.rabbitframework.security.exceptions.AuthorizationException;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.session.UnknownSessionException;

import java.util.Collection;

/**
 * Default business-tier implementation of a {@link ValidatingSessionManager}.  All session CRUD operations are
 * delegated to an internal {@link SessionDAO}.
 *
 * @since 0.1
 */
public class DefaultSessionManager extends AbstractValidatingSessionManager implements CacheManagerAware {
    @Override
    protected Session retrieveSession(SessionKey key) throws UnknownSessionException {
        return null;
    }

    @Override
    protected Session doCreateSession(SessionContext initData) throws AuthorizationException {
        return null;
    }

    @Override
    protected Collection<Session> getActiveSessions() {
        return null;
    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {

    }
}
