package com.rabbitframework.security.subject;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.authc.AuthenticationException;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authz.AuthorizationException;
import com.rabbitframework.security.authz.Permission;
import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.support.DefaultSubjectContext;
import com.rabbitframework.security.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 主体,是任何可以与应用交互的“用户”
 *
 * @author: justin.liang
 * @date: 16/4/19 下午11:20
 */
public interface Subject {
    /**
     * 获取用户身份信息
     *
     * @return
     */
    Object getPrincipal();

    /**
     * 获取用户身份信息的集合
     *
     * @return
     */
    PrincipalCollection getPrincipals();

    boolean isPermitted(String permission);

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

    /**
     * 登陆提交调用,登陆失败将会抛出异常
     *
     * @param token
     * @throws AuthenticationException
     */
    void login(AuthenticationToken token) throws AuthenticationException;

    /**
     * 判断当前是否登陆
     *
     * @return
     */
    boolean isAuthenticated();

    /**
     * 记住我
     *
     * @return
     */
    boolean isRemembered();

    /**
     * 会话,相当于 getSession(true)
     *
     * @return
     */
    Session getSession();

    Session getSession(boolean create);

    /**
     * 退出时调用
     */
    void logout();

    /**
     * 实现线程之间的{@link Subject}传播,因为 {@link Subject}是线程绑定的;因此在多线程执行中需要传播
     * 到相应的线程才能获取到相应的{@link Subject}。
     * 最简单的办法就是通过 execute(runnable/callable 实例)直接调用;
     * 或者通过 {@link #associateWith(Runnable runable)}或{@link #associateWith(Callable runable)}
     * 得到一个包装后的实例;它们都是通过:
     * 1、把当前线程的{@link Subject}绑定过去;
     * 2、在线程执行结束后自动释放
     *
     * @param callable
     * @param <V>
     * @return
     * @throws ExecutionException
     */
    <V> V execute(Callable<V> callable) throws ExecutionException;


    void execute(Runnable runnable);


    <V> Callable<V> associateWith(Callable<V> callable);


    Runnable associateWith(Runnable runnable);

    /**
     * RunAs 即实现“允许A 假设为B身份进行访问”;通过调用 subject.runAs(b)进行访问;
     * 接着调用subject.getPrincipals将获取到B的身份;此时调用 isRunAs 将返回 true;
     * 而 a 的身份需要通过 {@link #getPreviousPrincipals()} 获取;
     * 如果不需要 RunAs 了调用 {@link #releaseRunAs()} 即可
     *
     * @param principals
     * @throws NullPointerException
     * @throws IllegalStateException
     */
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
