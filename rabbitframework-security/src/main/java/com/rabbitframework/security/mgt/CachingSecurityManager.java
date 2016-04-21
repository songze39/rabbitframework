package com.rabbitframework.security.mgt;

import com.rabbitframework.security.cache.CacheManager;
import com.rabbitframework.security.cache.CacheManagerAware;
import com.rabbitframework.security.util.Destroyable;
import com.rabbitframework.security.util.LifecycleUtils;

/**
 * 基于缓存的安全管理
 *
 * @author: justin.liang
 * @date: 16/4/21 下午9:28
 */
public abstract class CachingSecurityManager implements SecurityManager,
        Destroyable, CacheManagerAware {
    private CacheManager cacheManager;

    public CachingSecurityManager() {

    }

    /**
     * 在缓存设置之后的一个回调,默认为空,一般由子类实现
     */
    protected void afterCacheManagerSet() {

    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * 设置缓存管理,接着调用{@link #afterCacheManagerSet()}
     *
     * @param cacheManager
     */
    @Override
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        afterCacheManagerSet();
    }

    @Override
    public void destroy() throws Exception {
        LifecycleUtils.destroy(getCacheManager());
        cacheManager = null;
    }
}
