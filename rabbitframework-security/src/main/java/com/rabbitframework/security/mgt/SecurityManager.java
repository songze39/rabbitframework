package com.rabbitframework.security.mgt;

import com.rabbitframework.security.authz.Authorizer;
import com.rabbitframework.security.session.mgt.SessionManager;


/**
 * @author justin.liang
 */
public interface SecurityManager extends Authorizer, SessionManager {
}
