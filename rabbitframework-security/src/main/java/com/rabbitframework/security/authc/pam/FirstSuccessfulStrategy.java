package com.rabbitframework.security.authc.pam;

import com.rabbitframework.security.authc.AuthenticationException;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.realm.Realm;
import com.rabbitframework.security.util.CollectionUtils;
import java.util.Collection;

/**
 * 只要有一个 Realm 验证成功即可,只返回第一个 Realm 身份验证 成功的认证信息,其他的忽略;
 */
public class FirstSuccessfulStrategy extends AbstractAuthenticationStrategy {

    /**
     * Returns {@code null} immediately, relying on this class's {@link #merge merge} implementation to return
     * only the first {@code info} object it encounters, ignoring all subsequent ones.
     */
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    /**
     * Returns the specified {@code aggregate} instance if is non null and valid (that is, has principals and they are
     * not empty) immediately, or, if it is null or not valid, the {@code info} argument is returned instead.
     * <p/>
     * This logic ensures that the first valid info encountered is the one retained and all subsequent ones are ignored,
     * since this strategy mandates that only the info from the first successfully authenticated realm be used.
     */
    protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        if (aggregate != null && !CollectionUtils.isEmpty(aggregate.getPrincipals())) {
            return aggregate;
        }
        return info != null ? info : aggregate;
    }
}
