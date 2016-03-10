package com.rabbitframework.security.mgt;

import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.Authenticator;
import com.rabbitframework.security.authz.Authorizer;
import com.rabbitframework.security.exceptions.AuthenticationException;
import com.rabbitframework.security.session.mgt.SessionManager;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;


/**
 * 安全管理接口
 *
 * @author justin.liang
 */
public interface SecurityManager extends Authenticator, Authorizer, SessionManager {
    /**
     * Logs in the specified Subject using the given {@code authenticationToken}, returning an updated Subject
     * instance reflecting the authenticated state if successful or throwing {@code AuthenticationException} if it is
     * not.
     * <p/>
     * Note that most application developers should probably not call this method directly unless they have a good
     * reason for doing so.  The preferred way to log in a Subject is to call
     * <code>subject.{@link Subject#login login(authenticationToken)}</code> (usually after
     * acquiring the Subject by calling {@link com.rabbitframework.security.SecurityUtils#getSubject() SecurityUtils.getSubject()}).
     * <p/>
     * Framework developers on the other hand might find calling this method directly useful in certain cases.
     *
     * @param subject             the subject against which the authentication attempt will occur
     * @param authenticationToken the token representing the Subject's principal(s) and credential(s)
     * @return the subject instance reflecting the authenticated state after a successful attempt
     * @throws AuthenticationException if the login attempt failed.
     * @since 1.0
     */
    Subject login(Subject subject, AuthenticationToken authenticationToken) throws AuthenticationException;

    /**
     * Logs out the specified Subject from the system.
     * <p/>
     * Note that most application developers should not call this method unless they have a good reason for doing
     * so.  The preferred way to logout a Subject is to call
     * <code>{@link Subject#logout Subject.logout()}</code>, not the
     * {@code SecurityManager} directly.
     * <p/>
     * Framework developers on the other hand might find calling this method directly useful in certain cases.
     *
     * @param subject the subject to log out.
     * @since 1.0
     */
    void logout(Subject subject);

    /**
     * Creates a {@code Subject} instance reflecting the specified contextual data.
     * <p/>
     * The context can be anything needed by this {@code SecurityManager} to construct a {@code Subject} instance.
     * Most Shiro end-users will never call this method - it exists primarily for
     * framework development and to support any underlying custom {@link SubjectFactory SubjectFactory} implementations
     * that may be used by the {@code SecurityManager}.
     * <h4>Usage</h4>
     * After calling this method, the returned instance is <em>not</em> bound to the application for further use.
     * Callers are expected to know that {@code Subject} instances have local scope only and any
     * other further use beyond the calling method must be managed explicitly.
     *
     * @param context any data needed to direct how the Subject should be constructed.
     * @return the {@code Subject} instance reflecting the specified initialization data.
     * @see SubjectFactory#createSubject(SubjectContext)
     * @see Subject.Builder
     * @since 1.0
     */
    Subject createSubject(SubjectContext context);
}
