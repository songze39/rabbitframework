package com.rabbitframework.security;
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
//            subject = (new Subject.Builder())
        }
        return subject;
    }

    public static SecurityManager getSecurityManager() {
        SecurityManager securityManager = ThreadContext.getSecurityManager();
        return securityManager;
    }
}