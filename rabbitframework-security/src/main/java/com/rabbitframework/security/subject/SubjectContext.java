package com.rabbitframework.security.subject;

import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.session.Session;

import java.io.Serializable;
import java.util.Map;

/**
 * 主体上下文,继承{@link Map}
 */
public interface SubjectContext extends Map<String, Object> {

    SecurityManager getSecurityManager();

    void setSecurityManager(SecurityManager securityManager);

    SecurityManager resolveSecurityManager();

    Serializable getSessionId();

    void setSessionId(Serializable sessionId);

    Subject getSubject();

    void setSubject(Subject subject);

    PrincipalCollection getPrincipals();

    PrincipalCollection resolvePrincipals();

    void setPrincipals(PrincipalCollection principals);

    Session getSession();

    void setSession(Session session);

    Session resolveSession();

    boolean isAuthenticated();

    void setAuthenticated(boolean authc);

    boolean isSessionCreationEnabled();

    void setSessionCreationEnabled(boolean enabled);

    boolean resolveAuthenticated();

    AuthenticationInfo getAuthenticationInfo();

    void setAuthenticationInfo(AuthenticationInfo info);

    AuthenticationToken getAuthenticationToken();

    void setAuthenticationToken(AuthenticationToken token);

    String getHost();

    void setHost(String host);

    String resolveHost();
}
