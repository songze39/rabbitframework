package com.rabbitframework.security.web.mgt;

import com.rabbitframework.security.mgt.SecurityManager;

/**
 * @author: justin.liang
 * @date: 16/4/21 下午12:07
 */
public interface WebSecurityManager extends SecurityManager {

    /**
     * 判断当前是否为http的session模式*
     *
     * @return
     */
    boolean isHttpSessionMode();
}
