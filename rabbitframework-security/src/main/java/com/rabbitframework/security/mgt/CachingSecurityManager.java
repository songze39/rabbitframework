package com.rabbitframework.security.mgt;

import com.rabbitframework.security.cache.CacheManager;
import com.rabbitframework.security.cache.CacheManagerAware;
import com.rabbitframework.security.util.Destroyable;
import com.rabbitframework.security.util.LifecycleUtils;

/**
 * A very basic starting point for the SecurityManager interface that merely provides logging and caching
 * support.  All actual {@code SecurityManager} method implementations are left to subclasses.
 * <p/>
 * <b>Change in 1.0</b> - a default {@code CacheManager} instance is <em>not</em> created by default during
 * instantiation.  As caching strategies can vary greatly depending on an application's needs, a {@code CacheManager}
 * instance must be explicitly configured if caching across the framework is to be enabled.
 *
 * @since 0.9
 */
public abstract class CachingSecurityManager implements SecurityManager, Destroyable, CacheManagerAware {

    /**
     * The CacheManager to use to perform caching operations to enhance performance.  Can be null.
     */
    private CacheManager cacheManager;

    /**
     * Default no-arg constructor that will automatically attempt to initialize a default cacheManager
     */
    public CachingSecurityManager() {
    }

    /**
     * Returns the CacheManager used by this SecurityManager.
     *
     * @return the cacheManager used by this SecurityManager
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * Sets the CacheManager used by this {@code SecurityManager} and potentially any of its
     * children components.
     * <p/>
     * After the cacheManager attribute has been set, the template method
     * {@link #afterCacheManagerSet afterCacheManagerSet()} is executed to allow subclasses to adjust when a
     * cacheManager is available.
     *
     * @param cacheManager the CacheManager used by this {@code SecurityManager} and potentially any of its
     *                     children components.
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        afterCacheManagerSet();
    }

    /**
     * Template callback to notify subclasses that a
     * {@link CacheManager CacheManager} has been set and is available for use via the
     * {@link #getCacheManager getCacheManager()} method.
     */
    protected void afterCacheManagerSet() {
    }

    /**
     * Destroys the {@link #getCacheManager() cacheManager} via {@link LifecycleUtils#destroy LifecycleUtils.destroy}.
     */
    public void destroy() {
        LifecycleUtils.destroy(getCacheManager());
        this.cacheManager = null;
    }

}