package com.rabbitframework.security.util;

import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程上下文
 *
 * @author: justin.liang
 * @date: 16/4/21 上午9:36
 */
public abstract class ThreadContext {
    private static final Logger logger = LoggerFactory.getLogger(ThreadContext.class);
    public static final String SECURITY_MANAGER_KEY = ThreadContext.class.getName() + "SECURITY_MANAGER_KEY";
    public static final String SUBJECT_KEY = ThreadContext.class.getName() + "SUBJECT_KEY";
    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<>();

    protected ThreadContext() {

    }

    /**
     * 获取资源并转换为{@link HashMap}
     *
     * @return
     */
    public static Map<Object, Object> getResources() {
        if (resources.get() == null) {
            return Collections.emptyMap();
        }
        return new HashMap<Object, Object>(resources.get());
    }

    public static void setResources(Map<Object, Object> newResources) {
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
     * 根据key获取线程中的值
     *
     * @param key
     * @return
     */
    public static Object get(Object key) {
        if (logger.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        Object value = getValue(key);
        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] " + "bound to thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }
        return value;
    }

    /**
     * 绑定值到当前线程当中
     * <p>
     * 如果key为空时,将抛出{@link IllegalArgumentException}异常
     * <p>
     * 如果value为空时,将根据key删除存在本地线程中的值,如：
     * <pre>
     * if ( value == null ) {
     *     remove( key );
     * }</pre>
     *
     * @param key   The key with which to identify the <code>value</code>.
     * @param value The value to bind to the thread.
     * @throws IllegalArgumentException if the <code>key</code> argument is <tt>null</tt>.
     */
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

        if (logger.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] to thread " + "[" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }
    }

    /**
     * 根据key删除存在本地线程Map中的的值
     *
     * @param key
     * @return
     */
    public static Object remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

        if ((value != null) && logger.isTraceEnabled()) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" +
                    key + "]" + "from thread [" + Thread.currentThread().getName() + "]";
            logger.trace(msg);
        }

        return value;
    }

    /**
     * 删除本地线程,相当于清空线程
     */
    public static void remove() {
        resources.remove();
    }

    /**
     * 从本地线程中获取{@link com.rabbitframework.security.mgt.SecurityManager}
     *
     * @return
     */
    public static SecurityManager getSecurityManager() {
        return (SecurityManager) get(SECURITY_MANAGER_KEY);
    }

    /**
     * 将{@link SecurityManager}存在本地线程的map中。
     *
     * @param securityManager
     */
    public static void bind(SecurityManager securityManager) {
        if (securityManager != null) {
            put(SECURITY_MANAGER_KEY, securityManager);
        }
    }

    /**
     * 删除本地线程map中的{@link SecurityManager}
     *
     * @return
     */
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

    /**
     * 安全初始化资源
     */
    private static void ensureResourcesInitialized() {
        if (resources.get() == null) {
            resources.set(new HashMap<Object, Object>());
        }
    }

    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>>
            extends InheritableThreadLocal<Map<Object, Object>> {
        @Override
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            }
            return null;
        }
    }


}
