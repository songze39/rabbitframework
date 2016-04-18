package com.rabbitframework.security.session.mgt;

import java.io.Serializable;

/**
 * @author: justin.liang
 * @date: 16/4/18 下午11:21
 */
public interface SessionKey {

    Serializable getSessionId();
}
