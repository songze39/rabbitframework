package com.rabbitframework.security.subject.support;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.UnavailableSecurityManagerException;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.HostAuthenticationToken;
import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.PrincipalCollection;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;
import com.rabbitframework.security.util.CollectionUtils;
import com.rabbitframework.security.util.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 默认实现{@link SubjectContext}接口
 */
public class DefaultSubjectContext extends MapContext implements SubjectContext {
    private static final transient Logger logger = LoggerFactory.getLogger(DefaultSubjectContext.class);
    private static final String CLASS_NAME = DefaultSubjectContext.class.getName();
    private static final String SECURITY_MANAGER = CLASS_NAME + ".SECURITY_MANAGER";
    private static final String SESSION_ID = CLASS_NAME + ".SESSION_ID";
    private static final String AUTHENTICATION_TOKEN = CLASS_NAME + ".AUTHENTICATION_TOKEN";
    private static final String AUTHENTICATION_INFO = CLASS_NAME + ".AUTHENTICATION_INFO";
    private static final String SUBJECT = CLASS_NAME + ".SUBJECT";
    private static final String PRINCIPALS = CLASS_NAME + ".PRINCIPALS";
    private static final String SESSION = CLASS_NAME + ".SESSION";
    private static final String AUTHENTICATED = CLASS_NAME + ".AUTHENTICATED";
    private static final String HOST = CLASS_NAME + ".HOST";
    public static final String SESSION_CREATION_ENABLED = CLASS_NAME + ".SESSION_CREATION_ENABLED";
    /**
     * The session key that is used to store subject principals.
     */
    public static final String PRINCIPALS_SESSION_KEY = CLASS_NAME + "_PRINCIPALS_SESSION_KEY";

    /**
     * The session key that is used to store whether or not the user is authenticated.
     */
    public static final String AUTHENTICATED_SESSION_KEY = CLASS_NAME + "_AUTHENTICATED_SESSION_KEY";

    public DefaultSubjectContext() {
        super();
    }

    public DefaultSubjectContext(SubjectContext ctx) {
        super(ctx);
    }

    public SecurityManager getSecurityManager() {
        return getTypedValue(SECURITY_MANAGER, SecurityManager.class);
    }

    public void setSecurityManager(SecurityManager securityManager) {
        nullSafePut(SECURITY_MANAGER, securityManager);
    }

    /**
     * 获取{@link SecurityManager},先从当前map中获取,如果为空则从本地线程当中获取
     *
     * @return
     */
    public SecurityManager resolveSecurityManager() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No SecurityManager available in subject context map.  " +
                        "Falling back to SecurityUtils.getSecurityManager() lookup.");
            }
            try {
                securityManager = SecurityUtils.getSecurityManager();
            } catch (UnavailableSecurityManagerException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No SecurityManager available via SecurityUtils.  Heuristics exhausted.", e);
                }
            }
        }
        return securityManager;
    }

    public Serializable getSessionId() {
        return getTypedValue(SESSION_ID, Serializable.class);
    }

    public void setSessionId(Serializable sessionId) {
        nullSafePut(SESSION_ID, sessionId);
    }

    public Subject getSubject() {
        return getTypedValue(SUBJECT, Subject.class);
    }

    public void setSubject(Subject subject) {
        nullSafePut(SUBJECT, subject);
    }

    public PrincipalCollection getPrincipals() {
        return getTypedValue(PRINCIPALS, PrincipalCollection.class);
    }

    public void setPrincipals(PrincipalCollection principals) {
        if (!CollectionUtils.isEmpty(principals)) {
            put(PRINCIPALS, principals);
        }
    }

    public PrincipalCollection resolvePrincipals() {
        PrincipalCollection principals = getPrincipals();

        if (CollectionUtils.isEmpty(principals)) {
            //check to see if they were just authenticated:
            AuthenticationInfo info = getAuthenticationInfo();
            if (info != null) {
                principals = info.getPrincipals();
            }
        }
        if (CollectionUtils.isEmpty(principals)) {
            Subject subject = getSubject();
            if (subject != null) {
                principals = subject.getPrincipals();
            }
        }
        if (CollectionUtils.isEmpty(principals)) {
            //try the session:
            Session session = resolveSession();
            if (session != null) {
                principals = (PrincipalCollection) session.getAttribute(PRINCIPALS_SESSION_KEY);
            }
        }

        return principals;
    }


    public Session getSession() {
        return getTypedValue(SESSION, Session.class);
    }

    public void setSession(Session session) {
        nullSafePut(SESSION, session);
    }

    public Session resolveSession() {
        Session session = getSession();
        if (session == null) {
            //try the Subject if it exists:
            Subject existingSubject = getSubject();
            if (existingSubject != null) {
                session = existingSubject.getSession(false);
            }
        }
        return session;
    }

    public boolean isSessionCreationEnabled() {
        Boolean val = getTypedValue(SESSION_CREATION_ENABLED, Boolean.class);
        return val == null || val;
    }

    public void setSessionCreationEnabled(boolean enabled) {
        nullSafePut(SESSION_CREATION_ENABLED, enabled);
    }

    public boolean isAuthenticated() {
        Boolean authc = getTypedValue(AUTHENTICATED, Boolean.class);
        return authc != null && authc;
    }

    public void setAuthenticated(boolean authc) {
        put(AUTHENTICATED, authc);
    }

    public boolean resolveAuthenticated() {
        Boolean authc = getTypedValue(AUTHENTICATED, Boolean.class);
        if (authc == null) {
            //see if there is an AuthenticationInfo object.  If so, the very presence of one indicates a successful
            //authentication attempt:
            AuthenticationInfo info = getAuthenticationInfo();
            authc = info != null;
        }
        if (!authc) {
            //fall back to a session check:
            Session session = resolveSession();
            if (session != null) {
                Boolean sessionAuthc = (Boolean) session.getAttribute(AUTHENTICATED_SESSION_KEY);
                authc = sessionAuthc != null && sessionAuthc;
            }
        }

        return authc;
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return getTypedValue(AUTHENTICATION_INFO, AuthenticationInfo.class);
    }

    public void setAuthenticationInfo(AuthenticationInfo info) {
        nullSafePut(AUTHENTICATION_INFO, info);
    }


    public AuthenticationToken getAuthenticationToken() {
        return getTypedValue(AUTHENTICATION_TOKEN, AuthenticationToken.class);
    }

    public void setAuthenticationToken(AuthenticationToken token) {
        nullSafePut(AUTHENTICATION_TOKEN, token);
    }

    public String getHost() {
        return getTypedValue(HOST, String.class);
    }

    public void setHost(String host) {
        if (StringUtils.hasText(host)) {
            put(HOST, host);
        }
    }

    public String resolveHost() {
        String host = getHost();
        if (host == null) {
            //check to see if there is an AuthenticationToken from which to retrieve it:
            AuthenticationToken token = getAuthenticationToken();
            if (token instanceof HostAuthenticationToken) {
                host = ((HostAuthenticationToken) token).getHost();
            }
        }

        if (host == null) {
            Session session = resolveSession();
            if (session != null) {
                host = session.getHost();
            }
        }

        return host;
    }
}
