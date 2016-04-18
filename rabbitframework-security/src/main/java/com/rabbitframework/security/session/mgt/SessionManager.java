package com.rabbitframework.security.session.mgt;

import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.session.SessionException;

/**
 * 会话管理
 *
 * @author: justin.liang
 * @date: 16/4/18 下午11:47
 */
public interface SessionManager {
    /**
     * 开始创建一个新的session
     *
     * @param context
     * @return
     */
    Session start(SessionContext context);

    /**
     * 根据{@link SessionKey} 获取 {@link Session}
     *
     * @param key
     * @return
     * @throws SessionException
     */
    Session getSession(SessionKey key) throws SessionException;

}
