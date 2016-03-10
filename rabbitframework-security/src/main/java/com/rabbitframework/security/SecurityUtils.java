package com.rabbitframework.security;

import com.rabbitframework.security.exceptions.UnavailableSecurityManagerException;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.util.ThreadContext;
import com.rabbitframework.security.mgt.SecurityManager;

/**
 * 安全公共类
 *
 * @author justin.liang
 */
public class SecurityUtils {
    /**
     * ONLY used as a 'backup' in VM Singleton environments (that is, standalone environments), since the
     * ThreadContext should always be the primary source for Subject instances when possible.
     */
    private static SecurityManager securityManager;

    public static Subject getSubject() {
        Subject subject = ThreadContext.getSubject();
        if (subject == null) {
            subject = (new Subject.Builder()).buildSubject();
            ThreadContext.bind(subject);
        }
        return subject;
    }

    public static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.securityManager = securityManager;
    }

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