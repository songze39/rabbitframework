package com.rabbitframework.security.mgt;

import com.rabbitframework.security.authc.Authenticator;
import com.rabbitframework.security.authz.Authorizer;
import com.rabbitframework.security.session.mgt.SessionManager;


/**
 * 安全管理接口
 * @author justin.liang
 */
public interface SecurityManager extends Authenticator, Authorizer, SessionManager {

}
