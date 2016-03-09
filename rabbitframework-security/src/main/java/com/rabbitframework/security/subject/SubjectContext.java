package com.rabbitframework.security.subject;

import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.session.Session;

import java.io.Serializable;
import java.util.Map;

/**
 * 主题上下文接口，实现{@link Map Map} 接口
 * 用于提供给{@link com.rabbitframework.security.mgt.SecurityManager}解析这个数据去构造{@link Subject}实例
 *
 * @author justin.liang
 */
public interface SubjectContext extends Map<String, Object> {
    /**
     * Returns the SecurityManager instance that should be used to back the constructed {@link Subject} instance or
     * {@code null} if one has not yet been provided to this context.
     *
     * @return the SecurityManager instance that should be used to back the constructed {@link Subject} instance or
     * {@code null} if one has not yet been provided to this context.
     */
    SecurityManager getSecurityManager();

    /**
     * Sets the SecurityManager instance that should be used to back the constructed {@link Subject} instance
     * (typically used to support {@link org.apache.shiro.subject.support.DelegatingSubject DelegatingSubject} implementations).
     *
     * @param securityManager the SecurityManager instance that should be used to back the constructed {@link Subject}
     *                        instance.
     */
    void setSecurityManager(SecurityManager securityManager);

    /**
     * Resolves the {@code SecurityManager} instance that should be used to back the constructed {@link Subject}
     * instance (typically used to support {@link org.apache.shiro.subject.support.DelegatingSubject DelegatingSubject} implementations).
     *
     * @return the {@code SecurityManager} instance that should be used to back the constructed {@link Subject}
     * instance
     */
    SecurityManager resolveSecurityManager();

    /**
     * Returns the session id of the session that should be associated with the constructed {@link Subject} instance.
     * <p/>
     * The construction process is expected to resolve the session with the specified id and then construct the Subject
     * instance based on the resolved session.
     *
     * @return the session id of the session that should be associated with the constructed {@link Subject} instance.
     */
    Serializable getSessionId();

    /**
     * Sets the session id of the session that should be associated with the constructed {@link Subject} instance.
     * <p/>
     * The construction process is expected to resolve the session with the specified id and then construct the Subject
     * instance based on the resolved session.
     *
     * @param sessionId the session id of the session that should be associated with the constructed {@link Subject}
     *                  instance.
     */
    void setSessionId(Serializable sessionId);

    /**
     * Returns any existing {@code Subject} that may be in use at the time the new {@code Subject} instance is
     * being created.
     * <p/>
     * This is typically used in the case where the existing {@code Subject} instance returned by
     * this method is unauthenticated and a new {@code Subject} instance is being created to reflect a successful
     * authentication - you want to return most of the state of the previous {@code Subject} instance when creating the
     * newly authenticated instance.
     *
     * @return any existing {@code Subject} that may be in use at the time the new {@code Subject} instance is
     * being created.
     */
    Subject getSubject();

    /**
     * Sets the existing {@code Subject} that may be in use at the time the new {@code Subject} instance is
     * being created.
     * <p/>
     * This is typically used in the case where the existing {@code Subject} instance returned by
     * this method is unauthenticated and a new {@code Subject} instance is being created to reflect a successful
     * authentication - you want to return most of the state of the previous {@code Subject} instance when creating the
     * newly authenticated instance.
     *
     * @param subject the existing {@code Subject} that may be in use at the time the new {@code Subject} instance is
     *                being created.
     */
    void setSubject(Subject subject);

    /**
     * Returns the principals (aka identity) that the constructed {@code Subject} should reflect.
     *
     * @return the principals (aka identity) that the constructed {@code Subject} should reflect.
     */
    PrincipalCollection getPrincipals();

    PrincipalCollection resolvePrincipals();

    /**
     * Sets the principals (aka identity) that the constructed {@code Subject} should reflect.
     *
     * @param principals the principals (aka identity) that the constructed {@code Subject} should reflect.
     */
    void setPrincipals(PrincipalCollection principals);

    /**
     * Returns the {@code Session} to use when building the {@code Subject} instance.  Note that it is more
     * common to specify a {@link #setSessionId sessionId} to acquire the desired session rather than having to
     * construct a {@code Session} to be returned by this method.
     *
     * @return the {@code Session} to use when building the {@code Subject} instance.
     */
    Session getSession();

    /**
     * Sets the {@code Session} to use when building the {@code Subject} instance.  Note that it is more
     * common to specify a {@link #setSessionId sessionId} to automatically resolve the desired session rather than
     * constructing a {@code Session} to call this method.
     *
     * @param session the {@code Session} to use when building the {@code Subject} instance.
     */
    void setSession(Session session);

    Session resolveSession();

    /**
     * Returns {@code true} if the constructed {@code Subject} should be considered authenticated, {@code false}
     * otherwise.  Be careful setting this value to {@code true} - you should know what you are doing and have a good
     * reason for ignoring Shiro's default authentication state mechanisms.
     *
     * @return {@code true} if the constructed {@code Subject} should be considered authenticated, {@code false}
     * otherwise.
     */
    boolean isAuthenticated();

    /**
     * Sets whether or not the constructed {@code Subject} instance should be considered as authenticated.  Be careful
     * when specifying {@code true} - you should know what you are doing and have a good reason for ignoring Shiro's
     * default authentication state mechanisms.
     *
     * @param authc whether or not the constructed {@code Subject} instance should be considered as authenticated.
     */
    void setAuthenticated(boolean authc);

    /**
     * Returns {@code true} if the constructed {@code Subject} should be allowed to create a session, {@code false}
     * otherwise.  Shiro's configuration defaults to {@code true} as most applications find value in Sessions.
     *
     * @return {@code true} if the constructed {@code Subject} should be allowed to create sessions, {@code false}
     * otherwise.
     * @since 1.2
     */
    boolean isSessionCreationEnabled();

    /**
     * Sets whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
     *
     * @param enabled whether or not the constructed {@code Subject} instance should be allowed to create a session,
     *                {@code false} otherwise.
     * @since 1.2
     */
    void setSessionCreationEnabled(boolean enabled);

    boolean resolveAuthenticated();

    AuthenticationInfo getAuthenticationInfo();

    void setAuthenticationInfo(AuthenticationInfo info);

    AuthenticationToken getAuthenticationToken();

    void setAuthenticationToken(AuthenticationToken token);

    /**
     * Returns the host name or IP that should reflect the constructed {@code Subject}'s originating location.
     *
     * @return the host name or IP that should reflect the constructed {@code Subject}'s originating location.
     */
    String getHost();

    /**
     * Sets the host name or IP that should reflect the constructed {@code Subject}'s originating location.
     *
     * @param host the host name or IP that should reflect the constructed {@code Subject}'s originating location.
     */
    void setHost(String host);

    String resolveHost();


}
