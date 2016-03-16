package com.rabbitframework.security.authc;

/**
 * <p>An extension of the {@link AuthenticationInfo} interface to be implemented by
 * classes that support merging with other {@link AuthenticationInfo} instances.</p>
 *
 * <p>This allows an instance of this class to be an <em>aggregation</em>, or <em>composition</em> of account data
 * from across multiple <code>Realm</code>s <tt>Realm</tt>s, not just one realm.</p>
 *
 * <p>This is useful in a multi-realm authentication configuration - the individual <tt>AuthenticationInfo</tt>
 * objects obtained from each realm can be {@link #merge merged} into a single instance.  This instance can then be
 * returned at the end of the authentication process, giving the impression of a single underlying
 * realm/data source.
 *
 */
public interface MergableAuthenticationInfo extends AuthenticationInfo {

    /**
     * Merges the given {@link AuthenticationInfo} into this instance.  The specific way
     * that the merge occurs is up to the implementation, but typically it involves combining
     * the principals and credentials together in this instance.  The <code>info</code> argument should
     * not be modified in any way.
     *
     * @param info the info that should be merged into this instance.
     */
    void merge(AuthenticationInfo info);

}