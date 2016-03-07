package com.rabbitframework.security.session.mgt;

import java.io.Serializable;

/**
 * {@code SessionKey}接口获取sessionId,
 * 用于获取一个会话属性通过{@link com.rabbitframework.security.session.Session#getAttribute(Object) Session.getAttribute}方法。
 *
 * @author justin.liang
 */
public interface SessionKey {
    /**
     * 返回sessionId
     *
     * @return
     */
    Serializable getSessionId();
}
