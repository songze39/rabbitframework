package com.rabbitframework.security.util;

import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ThreadContext主要提供绑定或不绑定对象给当前的基本线程
 * 键值对匹配
 *
 * @author justin.liang
 */
public class ThreadContext {
    private static final Logger log = LoggerFactory.getLogger(ThreadContext.class);
    public static final String SECURITY_MANAGER_KEY = ThreadContext.class.getName() + "_SECURITY_MANAGER_KEY";
    public static final String SUBJECT_KEY = ThreadContext.class.getName() + "_SUBJECT_KEY";
    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

    protected ThreadContext() {

    }

    public static Map<Object, Object> getResources() {
        if (resources.get() == null) {
            return Collections.emptyMap();
        } else {
            return new HashMap<Object, Object>(resources.get());
        }
    }

    public void setResources(Map<Object, Object> newResources) {
        if (CollectionUtils.isEmpty(newResources)) {
            return;
        }
        ensureResourcesInitialized();
        resources.get().clear();
        resources.get().putAll(newResources);
    }

    private static Object getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? perThreadResources.get(key) : null;
    }

    /**
     * 安全的初始化资源
     */
    private static void ensureResourcesInitialized() {
        if (resources.get() == null) {
            resources.set(new HashMap<Object, Object>());
        }
    }

    public static Object get(Object key) {
        if (log.isTraceEnabled()) {
            String msg = "get() - in thread[" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
        Object value = getValue(key);
        if ((value != null) && log.isTraceEnabled()) {
            String msg = "retrieved value of type [" + value.getClass().getName() + "] for key [" + key + "] "
                    + "bound to thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
        return value;
    }

    public static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        if (value == null) {
            remove(key);
            return;
        }
        ensureResourcesInitialized();
        resources.get().put(key, value);
        if (log.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] to thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

    }

    public static Object remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;
        if ((value == null) && log.isTraceEnabled()) {
            String msg = "Remove value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] from thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
        return value;
    }

    public static void remove() {
        resources.remove();
    }

    public static SecurityManager getSecurityManager() {
        return (SecurityManager) get(SECURITY_MANAGER_KEY);
    }

    public static void bind(SecurityManager securityManager) {
        if (securityManager != null) {
            put(SECURITY_MANAGER_KEY, securityManager);
        }
    }

    public static SecurityManager unbindSecurityManager() {
        return (SecurityManager) remove(SECURITY_MANAGER_KEY);
    }


    public static Subject getSubject() {
        return (Subject) get(SUBJECT_KEY);
    }

    public static void bind(Subject subject) {
        if (subject != null) {
            put(SUBJECT_KEY, subject);
        }
    }

    public static Subject unbindSubject() {
        return (Subject) remove(SUBJECT_KEY);
    }


    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {
        @Override
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    }
}
