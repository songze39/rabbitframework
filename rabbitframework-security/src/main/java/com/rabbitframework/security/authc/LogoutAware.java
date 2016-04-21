package com.rabbitframework.security.authc;

import com.rabbitframework.security.subject.PrincipalCollection;

public interface LogoutAware {

    /**
     * Callback triggered when a <code>Subject</code> logs out of the system.
     *
     * @param principals the identifying principals of the Subject logging out.
     */
    public void onLogout(PrincipalCollection principals);
}
