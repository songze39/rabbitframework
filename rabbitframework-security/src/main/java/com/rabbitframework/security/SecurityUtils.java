package com.rabbitframework.security;

import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.util.ThreadContext;

/**
 * @author: justin.liang
 * @date: 16/4/21 上午10:16
 */
public abstract class SecurityUtils {
    private static SecurityManager securityManager;

    /**
     * 从本地线程当中获取{@link Subject},如果为空则通过
     * {@link com.rabbitframework.security.subject.Subject.Builder}构建
     *
     * @return
     */
    public static Subject getSubject() {
        Subject subject = ThreadContext.getSubject();
        if (subject == null) {
            subject = (new Subject.Builder()).buildSubject();
        }
        return subject;
    }

    public static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.securityManager = securityManager;
    }

    /**
     * 从本地线程当中获取{@link SecurityManager},如果为空的话,取本类的securityManager属性。
     * 如果还为空则抛出异常
     *
     * @return
     * @throws UnavailableSecurityManagerException
     */
    public static SecurityManager getSecurityManager() throws UnavailableSecurityManagerException {
        SecurityManager securityManager = ThreadContext.getSecurityManager();
        if (securityManager == null) {
            securityManager = SecurityUtils.securityManager;
        }
        if (securityManager == null) {
            String msg = "No SecurityManager accessible to the calling code, either bound to the " +
                    ThreadContext.class.getName() + " or as a vm static singleton.  This is an invalid application " +
                    "configuration.";
            throw new UnavailableSecurityManagerException(msg);
        }
        return securityManager;
    }

}
