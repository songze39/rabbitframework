package com.rabbitframework.security.cache;

import com.rabbitframework.security.exceptions.CacheException;

/**
 * A CacheManager provides and maintains the lifecycles of {@link Cache Cache} instances.
 *
 * <p>Shiro doesn't implement a full Cache mechanism itself, since that is outside the core competency of a
 * Security framework.  Instead, this interface provides an abstraction (wrapper) API on top of an underlying
 * cache framework's main Manager component (e.g. JCache, Ehcache, JCS, OSCache, JBossCache, TerraCotta, Coherence,
 * GigaSpaces, etc, etc), allowing a Shiro user to configure any cache mechanism they choose.
 *
 * @since 0.9
 */
public interface CacheManager {

    /**
     * Acquires the cache with the specified <code>name</code>.  If a cache does not yet exist with that name, a new one
     * will be created with that name and returned.
     *
     * @param name the name of the cache to acquire.
     * @return the Cache with the given name
     * @throws CacheException if there is an error acquiring the Cache instance.
     */
    public <K, V> Cache<K, V> getCache(String name) throws CacheException;
}