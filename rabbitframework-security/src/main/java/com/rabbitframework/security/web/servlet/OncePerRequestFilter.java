package com.rabbitframework.security.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OncePerRequestFilter extends NameableFilter {
    private static final Logger log = LoggerFactory.getLogger(OncePerRequestFilter.class);
    
}
