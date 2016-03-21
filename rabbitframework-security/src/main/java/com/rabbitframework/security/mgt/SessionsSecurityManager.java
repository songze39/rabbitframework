package com.rabbitframework.security.mgt;

import com.rabbitframework.security.cache.CacheManagerAware;
import com.rabbitframework.security.exceptions.AuthorizationException;
import com.rabbitframework.security.exceptions.SessionException;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.session.mgt.DefaultSessionManager;
import com.rabbitframework.security.session.mgt.SessionContext;
import com.rabbitframework.security.session.mgt.SessionKey;
import com.rabbitframework.security.session.mgt.SessionManager;
import com.rabbitframework.security.util.LifecycleUtils;

/**
 * Security support of a {@link SecurityManager} class hierarchy that delegates all
 * {@link Session session} operations to a wrapped
 * {@link SessionManager SessionManager} instance.  That is, this class implements the
 * methods in the {@link SessionManager SessionManager} interface, but in reality, those methods are merely
 * passthrough calls to the underlying 'real' {@code SessionManager} instance.
 * <p/>
 * The remaining {@code SecurityManager} methods not implemented by this class or its parents are left to be
 * implemented by subclasses.
 * <p/>
 * In keeping with the other classes in this hierarchy and Security's desire to minimize configuration whenever
 * possible, suitable default instances for all dependencies will be created upon instantiation.
 */
public abstract class SessionsSecurityManager extends AuthorizingSecurityManager {
    /**
     * The internal delegate <code>SessionManager</code> used by this security manager that manages all the
     * application's {@link Session Session}s.
     */
    private SessionManager sessionManager;

    /**
     * /**
     * Default no-arg constructor, internally creates a suitable default {@link SessionManager SessionManager} delegate
     * instance.
     */
    public SessionsSecurityManager() {
        super();
        this.sessionManager = new DefaultSessionManager();
        applyCacheManagerToSessionManager();
    }

    /**
     * Sets the underlying delegate {@link SessionManager} instance that will be used to support this implementation's
     * <tt>SessionManager</tt> method calls.
     * <p/>
     * This <tt>SecurityManager</tt> implementation does not provide logic to support the inherited
     * <tt>SessionManager</tt> interface, but instead delegates these calls to an internal
     * <tt>SessionManager</tt> instance.
     * <p/>
     * If a <tt>SessionManager</tt> instance is not set, a default one will be automatically created and
     * initialized appropriately for the the existing runtime environment.
     *
     * @param sessionManager delegate instance to use to support this manager's <tt>SessionManager</tt> method calls.
     */
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        afterSessionManagerSet();
    }

    protected void afterSessionManagerSet() {
        applyCacheManagerToSessionManager();
    }

    /**
     * Returns this security manager's internal delegate {@link SessionManager SessionManager}.
     *
     * @return this security manager's internal delegate {@link SessionManager SessionManager}.
     * @see #setSessionManager(SessionManager) setSessionManager
     */
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    /**
     * Calls {@link AuthorizingSecurityManager#afterCacheManagerSet() super.afterCacheManagerSet()} and then immediately calls
     * {@link #applyCacheManagerToSessionManager() applyCacheManagerToSessionManager()} to ensure the
     * <code>CacheManager</code> is applied to the SessionManager as necessary.
     */
    protected void afterCacheManagerSet() {
        super.afterCacheManagerSet();
        applyCacheManagerToSessionManager();
    }

    /**
     * Ensures the internal delegate <code>SessionManager</code> is injected with the newly set
     * {@link #setCacheManager CacheManager} so it may use it for its internal caching needs.
     * <p/>
     * Note:  This implementation only injects the CacheManager into the SessionManager if the SessionManager
     * instance implements the {@link CacheManagerAware CacheManagerAware} interface.
     */
    protected void applyCacheManagerToSessionManager() {
        if (this.sessionManager instanceof CacheManagerAware) {
            ((CacheManagerAware) this.sessionManager).setCacheManager(getCacheManager());
        }
    }

    public Session start(SessionContext context) throws AuthorizationException {
        return this.sessionManager.start(context);
    }

    public Session getSession(SessionKey key) throws SessionException {
        return this.sessionManager.getSession(key);
    }

    public void destroy() {
        LifecycleUtils.destroy(getSessionManager());
        this.sessionManager = null;
        super.destroy();
    }
}
