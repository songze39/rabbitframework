package com.rabbitframework.security.mgt;

import com.rabbitframework.security.authc.AuthenticationException;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.Authenticator;
import com.rabbitframework.security.authz.Authorizer;
import com.rabbitframework.security.session.mgt.SessionManager;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;

/**
 * 是Security核心类，管理所有一切安全执行操作、Subject
 * 对认证和授权、及会话、缓存的管理
 *
 * @author: justin.liang
 * @date: 16/4/18 下午11:46
 */
public interface SecurityManager extends Authenticator, Authorizer, SessionManager {

    Subject login(Subject subject, AuthenticationToken authenticationToken) throws AuthenticationException;


    void logout(Subject subject);


    Subject createSubject(SubjectContext context);
}
