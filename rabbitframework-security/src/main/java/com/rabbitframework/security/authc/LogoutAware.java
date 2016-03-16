package com.rabbitframework.security.authc;


import com.rabbitframework.security.subject.PrincipalCollection;

public interface LogoutAware {
    public void onLogout(PrincipalCollection principals);
}
