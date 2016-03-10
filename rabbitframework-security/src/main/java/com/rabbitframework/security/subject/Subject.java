package com.rabbitframework.security.subject;

import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authz.Permission;
import com.rabbitframework.security.exceptions.AuthenticationException;
import com.rabbitframework.security.exceptions.AuthorizationException;
import com.rabbitframework.security.exceptions.ExecutionException;
import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.support.DefaultSubjectContext;
import com.rabbitframework.security.util.CollectionUtils;
import com.rabbitframework.security.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A {@code Subject} represents state and security operations for a <em>single</em> application user.
 * These operations include authentication (login/logout), authorization (access control), and
 * session access. It is Shiro's primary mechanism for single-user security functionality.
 * <h3>Acquiring a Subject</h3>
 * To acquire the currently-executing {@code Subject}, application developers will almost always use
 * {@code SecurityUtils}:
 * <pre>
 * {@link SecurityUtils SecurityUtils}.{@link SecurityUtils#getSubject() getSubject()}</pre>
 * Almost all security operations should be performed with the {@code Subject} returned from this method.
 * <h3>Permission methods</h3>
 * Note that there are many *Permission methods in this interface overloaded to accept String arguments instead of
 * {@link Permission Permission} instances. They are a convenience allowing the caller to use a String representation of
 * a {@link Permission Permission} if desired.  The underlying Authorization subsystem implementations will usually
 * simply convert these String values to {@link Permission Permission} instances and then just call the corresponding
 * type-safe method.  (Shiro's default implementations do String-to-Permission conversion for these methods using
 * {@link org.apache.shiro.authz.permission.PermissionResolver PermissionResolver}s.)
 *
 * These overloaded *Permission methods forgo type-saftey for the benefit of convenience and simplicity,
 * so you should choose which ones to use based on your preferences and needs.
 *
 * @since 0.1
 */
public interface Subject {
    /**
     * 获取应用唯一的标识
     *
     * @return
     */
    Object getPrincipal();

    /**
     * 获取主题中的主要属性
     *
     * @return
     */
    PrincipalCollection getPrincipals();

    /**
     * 根据参数判断是否有权限
     *
     * @param permission
     * @return
     */
    boolean isPermitted(String permission);

    /**
     * 根据{@link Permission}判断是否有权限
     *
     * @param permission
     * @return
     */
    boolean isPermitted(Permission permission);

    boolean[] isPermitted(String... permissions);

    boolean[] isPermitted(List<Permission> permissions);

    boolean isPermittedAll(String... permissions);

    boolean isPermittedAll(Collection<Permission> permissions);

    void checkPermission(String permission) throws AuthorizationException;

    void checkPermission(Permission permission) throws AuthorizationException;

    void checkPermissions(String... permissions) throws AuthorizationException;

    void checkPermissions(Collection<Permission> permissions) throws AuthorizationException;

    boolean hasRole(String roleIdentifier);

    boolean[] hasRoles(List<String> roleIdentifiers);

    boolean hasAllRoles(Collection<String> roleIdentifiers);

    void checkRole(String roleIdentifier) throws AuthorizationException;

    void checkRoles(Collection<String> roleIdentifiers) throws AuthorizationException;

    void checkRoles(String... roleIdentifiers) throws AuthorizationException;

    void login(AuthenticationToken token) throws AuthenticationException;

    boolean isAuthenticated();

    boolean isRemembered();

    Session getSession();

    Session getSession(boolean create);

    void logout();

    <V> V execute(Callable<V> callable) throws ExecutionException;

    void execute(Runnable runnable);

    <V> Callable<V> associateWith(Callable<V> callable);

    Runnable associateWith(Runnable runnable);

    void runAs(PrincipalCollection principals) throws NullPointerException, IllegalStateException;

    boolean isRunAs();

    PrincipalCollection getPreviousPrincipals();

    PrincipalCollection releaseRunAs();

    public static class Builder {
        /**
         * Hold all contextual data via the Builder instance's method invocations to be sent to the
         * {@code SecurityManager} during the {@link #buildSubject} call.
         */
        private final SubjectContext subjectContext;

        /**
         * The SecurityManager to invoke during the {@link #buildSubject} call.
         */
        private final SecurityManager securityManager;

        public Builder() {
            this(SecurityUtils.getSecurityManager());
        }

        public Builder(SecurityManager securityManager) {
            if (securityManager == null) {
                throw new NullPointerException("SecurityManager method argument cannot be null.");
            }
            this.securityManager = securityManager;
            this.subjectContext = newSubjectContextInstance();
            if (this.subjectContext == null) {
                throw new IllegalStateException("Subject instance returned from 'newSubjectContextInstance' " +
                        "cannot be null.");
            }
            this.subjectContext.setSecurityManager(securityManager);
        }

        /**
         * Creates a new {@code SubjectContext} instance to be used to populate with subject contextual data that
         * will then be sent to the {@code SecurityManager} to create a new {@code Subject} instance.
         *
         * @return a new {@code SubjectContext} instance
         */
        protected SubjectContext newSubjectContextInstance() {
            return new DefaultSubjectContext();
        }

        protected SubjectContext getSubjectContext() {
            return this.subjectContext;
        }

        public Builder sessionId(Serializable sessionId) {
            if (sessionId != null) {
                this.subjectContext.setSessionId(sessionId);
            }
            return this;
        }

        public Builder host(String host) {
            if (StringUtils.hasText(host)) {
                this.subjectContext.setHost(host);
            }
            return this;
        }

        public Builder session(Session session) {
            if (session != null) {
                this.subjectContext.setSession(session);
            }
            return this;
        }

        public Builder principals(PrincipalCollection principals) {
            if (!CollectionUtils.isEmpty(principals)) {
                this.subjectContext.setPrincipals(principals);
            }
            return this;
        }

        public Builder sessionCreationEnabled(boolean enabled) {
            this.subjectContext.setSessionCreationEnabled(enabled);
            return this;
        }

        public Builder authenticated(boolean authenticated) {
            this.subjectContext.setAuthenticated(authenticated);
            return this;
        }

        public Builder contextAttribute(String attributeKey, Object attributeValue) {
            if (attributeKey == null) {
                String msg = "Subject context map key cannot be null.";
                throw new IllegalArgumentException(msg);
            }
            if (attributeValue == null) {
                this.subjectContext.remove(attributeKey);
            } else {
                this.subjectContext.put(attributeKey, attributeValue);
            }
            return this;
        }

        public Subject buildSubject() {
            return this.securityManager.createSubject(this.subjectContext);
        }

    }

}
