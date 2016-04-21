package com.rabbitframework.security.cache;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.security.util.Destroyable;
import com.rabbitframework.security.util.LifecycleUtils;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: justin.liang
 * @date: 16/4/21 下午5:26
 */
public abstract class AbstractCacheManager implements CacheManager, Destroyable {
    private final ConcurrentMap<String, Cache> caches;

    public AbstractCacheManager() {
        caches = new ConcurrentHashMap<String, Cache>();
    }


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Cache name cannot be null or empty.");
        }
        Cache cache;
        cache = caches.get(name);
        if (cache == null) {
            cache = createCache(name);
            Cache existing = caches.putIfAbsent(name, cache);
            if (existing != null) {
                cache = existing;
            }
        }
        return cache;
    }

    protected abstract Cache createCache(String name) throws CacheException;

    @Override
    public void destroy() throws Exception {
        if (!caches.isEmpty()) {
            for (Cache cache : caches.values()) {
                LifecycleUtils.destroy(cache);
            }
            caches.clear();
        }
    }

    public String toString() {
        Collection<Cache> values = caches.values();
        StringBuilder sb = new StringBuilder(getClass().getSimpleName())
                .append(" with ")
                .append(caches.size())
                .append(" cache(s)): [");
        int i = 0;
        for (Cache cache : values) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cache.toString());
            i++;
        }
        sb.append("]");
        return sb.toString();
    }
}
