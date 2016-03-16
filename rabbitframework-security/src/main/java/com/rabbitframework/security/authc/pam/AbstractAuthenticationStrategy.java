package com.rabbitframework.security.authc.pam;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.MergableAuthenticationInfo;
import com.rabbitframework.security.authc.SimpleAuthenticationInfo;
import com.rabbitframework.security.exceptions.AuthenticationException;
import com.rabbitframework.security.realm.Realm;
import java.util.Collection;

/**
 * Abstract base implementation for Shiro's concrete <code>AuthenticationStrategy</code>
 * implementations.
 */
public abstract class AbstractAuthenticationStrategy implements AuthenticationStrategy {
    /**
     * Simply returns <code>new {@link SimpleAuthenticationInfo SimpleAuthenticationInfo}();</code>, which supports
     * aggregating account data across realms.
     */
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms,
                                                AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    /**
     * Simply returns the <code>aggregate</code> method argument, without modification.
     */
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token,
                                            AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }

    /**
     * Base implementation that will aggregate the specified <code>singleRealmInfo</code> into the
     * <code>aggregateInfo</code> and then returns the aggregate.  Can be overridden by subclasses for custom behavior.
     */
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,
                                           AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if (aggregateInfo == null) {
                info = singleRealmInfo;
            } else {
                info = merge(singleRealmInfo, aggregateInfo);
            }
        }
        return info;
    }

    /**
     * Merges the specified <code>info</code> argument into the <code>aggregate</code> argument and then returns an
     * aggregate for continued use throughout the login process.
     * <p/>
     * This implementation merely checks to see if the specified <code>aggregate</code> argument is an instance of
     * {@link MergableAuthenticationInfo MergableAuthenticationInfo}, and if so, calls
     * <code>aggregate.merge(info)</code>  If it is <em>not</em> an instance of
     * <code>MergableAuthenticationInfo</code>, an {@link IllegalArgumentException IllegalArgumentException} is thrown.
     * Can be overridden by subclasses for custom merging behavior if implementing the
     * {@link MergableAuthenticationInfo MergableAuthenticationInfo} is not desired for some reason.
     */
    protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        if( aggregate instanceof MergableAuthenticationInfo) {
            ((MergableAuthenticationInfo)aggregate).merge(info);
            return aggregate;
        } else {
            throw new IllegalArgumentException( "Attempt to merge authentication info from multiple realms, but aggregate " +
                    "AuthenticationInfo is not of type MergableAuthenticationInfo." );
        }
    }

    /**
     * Simply returns the <code>aggregate</code> argument without modification.  Can be overridden for custom behavior.
     */
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token,
                                               AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}