package com.rabbitframework.security.authz;

import com.rabbitframework.security.subject.PrincipalCollection;

import java.util.Collection;
import java.util.List;

/**
 * 访问控制器,用来决定主体是否有权限进行相应的操作;
 * 控制着用户能访问应用中的哪些功能;
 *
 * @author: justin.liang
 * @date: 16/4/19 下午9:47
 */
public interface Authorizer {

    boolean isPermitted(PrincipalCollection principals, String permission);

    boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission);

    boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions);


    boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions);


    boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions);


    boolean isPermittedAll(PrincipalCollection subjectPrincipal, Collection<Permission> permissions);


    void checkPermission(PrincipalCollection subjectPrincipal, String permission) throws AuthorizationException;

    
    void checkPermission(PrincipalCollection subjectPrincipal, Permission permission) throws AuthorizationException;


    void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions) throws AuthorizationException;

    void checkPermissions(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) throws AuthorizationException;


    boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier);

    boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers);


    boolean hasAllRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers);


    void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier) throws AuthorizationException;


    void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) throws AuthorizationException;


    void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers) throws AuthorizationException;
}
