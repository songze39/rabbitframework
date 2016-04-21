package com.rabbitframework.security.cache;

/**
 * 缓存管理
 *
 * @author: justin.liang
 * @date: 16/4/21 下午5:22
 */
public interface CacheManager {
    /**
     * 根据缓存名称获取缓存
     *
     * @param name
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    public <K, V> Cache<K, V> getCache(String name) throws CacheException;
}
