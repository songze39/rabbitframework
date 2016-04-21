package com.rabbitframework.security.authc;

import com.rabbitframework.security.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 抽象实现{@link Authenticator}接口
 */
public abstract class AbstractAuthenticator implements Authenticator, LogoutAware {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticator.class);

    private Collection<AuthenticationListener> authenticationListeners;

    public AbstractAuthenticator() {
        authenticationListeners = new ArrayList<AuthenticationListener>();
    }

    public void setAuthenticationListenerss(Collection<AuthenticationListener> authenticationListeners) {
        if (authenticationListeners == null) {
            this.authenticationListeners = new ArrayList<AuthenticationListener>();
        } else {
            this.authenticationListeners = authenticationListeners;
        }
    }

    public Collection<AuthenticationListener> getAuthenticationListeners() {
        return authenticationListeners;
    }

    /**
     * 注册成功通知,使用了监听类{@link AuthenticationListener},调用
     * {@link AuthenticationListener#onSuccess(AuthenticationToken, AuthenticationInfo)}方法
     *
     * @param token
     * @param info
     */
    protected void notifySuccess(AuthenticationToken token, AuthenticationInfo info) {
        for (AuthenticationListener listener : this.authenticationListeners) {
            listener.onSuccess(token, info);
        }
    }

    /**
     * 注册错误通知,使用了监听类{@link AuthenticationListener},调用
     * {@link AuthenticationListener#onFailure(AuthenticationToken, AuthenticationException)}方法
     *
     * @param token
     * @param ae
     */
    protected void notifyFailure(AuthenticationToken token, AuthenticationException ae) {
        for (AuthenticationListener listener : this.authenticationListeners) {
            listener.onFailure(token, ae);
        }
    }

    /**
     * 用户退出通知,使用了监听类{@link AuthenticationListener},调用
     * {@link AuthenticationListener#onLogout(PrincipalCollection)}
     *
     * @param principals
     */
    protected void notifyLogout(PrincipalCollection principals) {
        for (AuthenticationListener listener : this.authenticationListeners) {
            listener.onLogout(principals);
        }
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        notifyLogout(principals);
    }

    @Override
    public AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {
        if (token == null) {
            throw new IllegalArgumentException("Method argumet (authentication token) cannot be null.");
        }
        logger.trace("Authentication attempt received for token [{}]", token);
        AuthenticationInfo info;
        try {
            info = doAuthenticate(token);
            if (info == null) {
                String msg = "No account information found for authentication token [" + token + "] by this " +
                        "Authenticator instance.  Please check that it is configured correctly.";
                throw new AuthenticationException(msg);
            }
        } catch (Throwable t) {
            AuthenticationException ae = null;
            if (t instanceof AuthenticationException) {
                ae = (AuthenticationException) t;
            }
            if (ae == null) {
                //Exception thrown was not an expected AuthenticationException.  Therefore it is probably a little more
                //severe or unexpected.  So, wrap in an AuthenticationException, log to warn, and propagate:
                String msg = "Authentication failed for token submission [" + token + "].  Possible unexpected " +
                        "error? (Typical or expected login exceptions should extend from AuthenticationException).";
                ae = new AuthenticationException(msg, t);
            }
            try {
                notifyFailure(token, ae);
            } catch (Throwable t2) {
                if (logger.isWarnEnabled()) {
                    String msg = "Unable to send notification for failed authentication attempt - listener error?.  " +
                            "Please check your AuthenticationListener implementation(s).  Logging sending exception " +
                            "and propagating original AuthenticationException instead...";
                    logger.warn(msg, t2);
                }
            }
            throw ae;
        }
        logger.debug("Authentication successful for token [{}].  Returned account [{}]", token, info);
        notifySuccess(token, info);
        return info;
    }

    protected abstract AuthenticationInfo doAuthenticate(AuthenticationToken token)
            throws AuthenticationException;


}
