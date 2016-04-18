package com.rabbitframework.security.session.mgt;

import java.io.Serializable;
import java.util.Map;

/**
 * session上下文,继承自{@link Map map}
 *
 * @author: justin.liang
 * @date: 16/4/18 下午11:14
 */
public interface SessionContext extends Map<String, Object> {
    /**
     * 设置host和ip地址
     *
     * @param host
     */
    void setHost(String host);

    /**
     * 返回host和ip地址
     * @return
     */
    String getHost();

    /**
     * 设置sessionId
     * @param sessionId
     */
    void setSessionId(Serializable sessionId);

    /**
     * 返回sessionId
     * @return
     */
    Serializable getSessionId();
}
