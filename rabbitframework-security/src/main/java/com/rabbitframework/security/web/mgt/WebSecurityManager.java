package com.rabbitframework.security.web.mgt;

import com.rabbitframework.security.mgt.SecurityManager;

public interface WebSecurityManager extends SecurityManager {
    /**
     * Security information needs to be retained from request to request, so Shiro makes use of a
     * session for this. Typically, a security manager will use the servlet container's HTTP session
     * but custom session implementations, for example based on EhCache, may also be used. This
     * method indicates whether the security manager is using the HTTP session or not.
     *
     * @return <code>true</code> if the security manager is using the HTTP session; otherwise,
     *         <code>false</code>.
     */
    boolean isHttpSessionMode();
}
