package com.rabbitframework.security.authz;

/**
 * 权限接口,判断是否有权限访问资源
 *
 * @author: justin.liang
 * @date: 16/4/19 下午10:20
 * @see com.rabbitframework.security.authz.permission.WildcardPermission WildcardPermission
 */
public interface Permission {
    /**
     * 权限匹配
     *
     * @param p
     * @return
     */
    boolean implies(Permission p);
}
