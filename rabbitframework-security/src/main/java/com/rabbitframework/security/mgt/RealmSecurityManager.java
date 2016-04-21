package com.rabbitframework.security.mgt;

import com.rabbitframework.security.cache.CacheManager;
import com.rabbitframework.security.cache.CacheManagerAware;
import com.rabbitframework.security.realm.Realm;
import com.rabbitframework.security.util.LifecycleUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 基于{@link Realm}的安全管理
 *
 * @author: justin.liang
 * @date: 16/4/21 下午9:41
 */
public abstract class RealmSecurityManager extends CachingSecurityManager {
    private Collection<Realm> realms;

    public RealmSecurityManager() {
        super();
    }

    public void setRealm(Realm realm) {
        if (realm == null) {
            throw new IllegalArgumentException("Realm argument cannot be null");
        }
        Collection<Realm> realmsc = new ArrayList<Realm>();
        realmsc.add(realm);
        setRealms(realmsc);
    }

    /**
     * 设置{@link Realm}集合,并调用{@link #afterRealmsSet()}
     *
     * @param realms
     */
    public void setRealms(Collection<Realm> realms) {
        if (realms == null) {
            throw new IllegalArgumentException("Realms collection argument cannot be null.");
        }
        if (realms.isEmpty()) {
            throw new IllegalArgumentException("Realms collection argument cannot be enpty.");
        }
        this.realms = realms;
        afterRealmsSet();
    }

    public Collection<Realm> getRealms() {
        return realms;
    }

    /**
     * 在设置realms之后调用此方法
     */
    protected void afterRealmsSet() {
        applyCacheManagerToRealms();
    }

    protected void applyCacheManagerToRealms() {
        CacheManager cacheManager = getCacheManager();
        Collection<Realm> realms = getRealms();
        if (cacheManager != null && realms != null && !realms.isEmpty()) {
            for (Realm realm : realms) {
                if (realm instanceof CacheManagerAware) {
                    ((CacheManagerAware) realm).setCacheManager(cacheManager);
                }
            }
        }
    }

    @Override
    protected void afterCacheManagerSet() {
        applyCacheManagerToRealms();
    }

    @Override
    public void destroy() throws Exception {
        LifecycleUtils.destroy(getRealms());
        super.destroy();
    }
}
