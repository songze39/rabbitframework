package com.rabbitframework.security.web.servlet;

import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.web.filter.mgt.FilterChainResolver;
import com.rabbitframework.security.web.mgt.WebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecurityFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AbstractSecurityFilter.class);
    private static final String STATIC_INIT_PARAM_NAME = "staticSecurityManagerEnabled";
    private WebSecurityManager securityManager;
    private FilterChainResolver filterChainResolver;
    private boolean staticSecurityManagerEnabled;

    protected AbstractSecurityFilter() {
        this.staticSecurityManagerEnabled = false;
    }

    public void setSecurityManager(WebSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public WebSecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }

    public FilterChainResolver getFilterChainResolver() {
        return filterChainResolver;
    }

    public void setStaticSecurityManagerEnabled(boolean staticSecurityManagerEnabled) {
        this.staticSecurityManagerEnabled = staticSecurityManagerEnabled;
    }

    public boolean isStaticSecurityManagerEnabled() {
        return staticSecurityManagerEnabled;
    }

    @Override
    protected void onFilterConfigSet() throws Exception {
        applyStaticSecurityManagerEnabled();
        init();
        if (isStaticSecurityManagerEnabled()) {
            SecurityUtils.setSecurityManager(getSecurityManager());
        }
    }

    public void init() throws Exception {

    }

    private void applyStaticSecurityManagerEnabled() {
        String value = getInitParam(STATIC_INIT_PARAM_NAME);
        if (value != null) {
            Boolean b = Boolean.valueOf(value);
            if (b != null) {
                setStaticSecurityManagerEnabled(b);
            }
        }
    }
}