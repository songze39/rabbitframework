package com.rabbitframework.security.subject.support;

import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.PrincipalCollection;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;
import com.rabbitframework.security.util.MapContext;
import java.io.Serializable;

/**
 * 默认实现{@link com.rabbitframework.security.subject.SubjectContext}接口
 *
 * @author justin.liang
 */
public class DefaultSubjectContext extends MapContext implements SubjectContext {


    @Override
    public SecurityManager getSecurityManager() {
        return null;
    }

    @Override
    public void setSecurityManager(SecurityManager securityManager) {

    }

    @Override
    public SecurityManager resolveSecurityManager() {
        return null;
    }

    @Override
    public Serializable getSessionId() {
        return null;
    }

    @Override
    public void setSessionId(Serializable sessionId) {

    }

    @Override
    public Subject getSubject() {
        return null;
    }

    @Override
    public void setSubject(Subject subject) {

    }

    @Override
    public PrincipalCollection getPrincipals() {
        return null;
    }

    @Override
    public PrincipalCollection resolvePrincipals() {
        return null;
    }

    @Override
    public void setPrincipals(PrincipalCollection principals) {

    }

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public void setSession(Session session) {

    }

    @Override
    public Session resolveSession() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean authc) {

    }

    @Override
    public boolean isSessionCreationEnabled() {
        return false;
    }

    @Override
    public void setSessionCreationEnabled(boolean enabled) {

    }

    @Override
    public boolean resolveAuthenticated() {
        return false;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo() {
        return null;
    }

    @Override
    public void setAuthenticationInfo(AuthenticationInfo info) {

    }

    @Override
    public AuthenticationToken getAuthenticationToken() {
        return null;
    }

    @Override
    public void setAuthenticationToken(AuthenticationToken token) {

    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public void setHost(String host) {

    }

    @Override
    public String resolveHost() {
        return null;
    }
}
