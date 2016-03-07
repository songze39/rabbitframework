package com.rabbitframework.security.session.mgt;

import com.rabbitframework.security.session.Session;

/**
 * session工厂接口，创建一个实体的{@link Session session}类，
 *
 * @author justin.liang
 */
public interface SessionFactory {
    Session createSession(SessionContext initData);
}
