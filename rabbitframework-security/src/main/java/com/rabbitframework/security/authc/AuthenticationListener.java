package com.rabbitframework.security.authc;

import com.rabbitframework.security.exceptions.AuthenticationException;
import com.rabbitframework.security.subject.PrincipalCollection;

/**
 * An {@code AuthenticationListener} listens for notifications while {@code Subject}s authenticate with the system.
 *
 * @since 0.9
 */
public interface AuthenticationListener {

    /**
     * Callback triggered when an authentication attempt for a {@code Subject} has succeeded.
     *
     * @param token the authentication token submitted during the {@code Subject} (user)'s authentication attempt.
     * @param info  the authentication-related account data acquired after authentication for the corresponding {@code Subject}.
     */
    void onSuccess(AuthenticationToken token, AuthenticationInfo info);

    /**
     * Callback triggered when an authentication attempt for a {@code Subject} has failed.
     *
     * @param token the authentication token submitted during the {@code Subject} (user)'s authentication attempt.
     * @param ae    the {@code AuthenticationException} that occurred as a result of the attempt.
     */
    void onFailure(AuthenticationToken token, AuthenticationException ae);

    /**
     * Callback triggered when a {@code Subject} logs-out of the system.
     * <p/>
     * This method will only be triggered when a Subject explicitly logs-out of the session.  It will not
     * be triggered if their Session times out.
     *
     * @param principals the identifying principals of the Subject logging out.
     */
    void onLogout(PrincipalCollection principals);
}