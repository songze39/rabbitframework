package com.rabbitframework.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登陆/退出公共类
 *
 * @author: justin.liang
 * @date: 16/5/7 上午1:23
 */
public class RabbitSecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(RabbitSecurityUtils.class);

    public static SecurityUser getSecurityUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Object obj = subject.getPrincipal();
            if (obj != null && (obj instanceof SecurityUser)) {
                return (SecurityUser) obj;
            }
            return null;
        } else {
            // 没有权限时使用
            return null;
        }
    }


    /**
     * 安全登陆,默认不记住我
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    public static boolean login(String loginName, String loginPwd) {
        return login(loginName, loginPwd, false);
    }

    /**
     * 安全登陆方法
     *
     * @param loginName   用户名称
     * @param loginPwd    用户密码
     * @param isRemeberMe 是否记住我
     * @return
     */
    public static boolean login(String loginName, String loginPwd, boolean isRemeberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, loginPwd);
        // 记录该令牌，如果不记录则类似购物车功能不能使用。
        token.setRememberMe(isRemeberMe);
        // subject理解成权限对象。类似user
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
//        } catch (UnknownAccountException ex) {// 用户名没有找到
//            logger.error(ex.getMessage(), ex);
//        } catch (IncorrectCredentialsException ex) {// 用户名密码不匹配
//            logger.error(ex.getMessage(), ex);
//        } catch (AuthenticationException e) {// 其他的登录错误
//            logger.error(e.getMessage(), e);
//        }

        if (subject.isAuthenticated()) {
            return true;
        }
        return false;
    }

    public static boolean login(UsernamePasswordToken token) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (subject.isAuthenticated()) {
            return true;
        }
        return false;
    }

    /**
     * 退出登陆
     */
    public static void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
