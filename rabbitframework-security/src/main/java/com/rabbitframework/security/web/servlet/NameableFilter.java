package com.rabbitframework.security.web.servlet;

import com.rabbitframework.security.util.Nameable;

import javax.servlet.FilterConfig;

public abstract class NameableFilter extends AbstractFilter implements Nameable {
    private String name;

    public String getName() {
        if (name == null) {
            FilterConfig config = getFilterConfig();
            if (config != null) {
                name = config.getFilterName();
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected StringBuilder toStringBuilder() {
        String name = getName();
        if (name == null) {
            return super.toStringBuilder();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            return sb;
        }
    }
}
