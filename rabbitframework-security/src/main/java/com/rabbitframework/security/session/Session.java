package com.rabbitframework.security.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * {@Code Session} 是一个请求与系统之间的会话状态
 *
 * @author: justin.liang
 * @date: 16/4/18 下午11:24
 */
public interface Session {
    /**
     * 获取Session的id值
     *
     * @return
     */
    Serializable getId();

    /**
     * 获取{@Code Session} 开始时间,由系统创建
     *
     * @return
     */
    Date getStartTimestamp();

    /**
     * 获取最后的访问时间
     *
     * @return
     */
    Date getLastAccessTime();

    /**
     * 获取回话的超时时间
     *
     * @return
     * @throws InvalidSessionException
     */
    long getTimeout() throws InvalidSessionException;

    /**
     * 获取host地址和ip,如果为{@code null}时表示没有获取到
     *
     * @return
     */
    String getHost();

    /**
     * Explicitly updates the {@link #getLastAccessTime() lastAccessTime} of this session to the current time when
     * this method is invoked.  This method can be used to ensure a session does not time out.
     * <p/>
     *
     * @throws InvalidSessionException
     */
    void touch() throws InvalidSessionException;

    void stop() throws InvalidSessionException;

    /**
     * Returns the keys of all the attributes stored under this session.  If there are no
     * attributes, this returns an empty collection.
     *
     * @return the keys of all attributes stored under this session, or an empty collection if
     * there are no session attributes.
     * @throws InvalidSessionException if this session has stopped or expired prior to calling this method.
     */
    Collection<Object> getAttributeKeys() throws InvalidSessionException;

    /**
     * Returns the object bound to this session identified by the specified key.  If there is no
     * object bound under the key, {@code null} is returned.
     *
     * @param key the unique name of the object bound to this session
     * @return the object bound under the specified {@code key} name or {@code null} if there is
     * no object bound under that name.
     * @throws InvalidSessionException if this session has stopped or expired prior to calling
     *                                 this method.
     */
    Object getAttribute(Object key) throws InvalidSessionException;

    /**
     * Binds the specified {@code value} to this session, uniquely identified by the specifed
     * {@code key} name.  If there is already an object bound under the {@code key} name, that
     * existing object will be replaced by the new {@code value}.
     * <p/>
     * If the {@code value} parameter is null, it has the same effect as if
     * {@link #removeAttribute(Object) removeAttribute} was called.
     *
     * @param key   the name under which the {@code value} object will be bound in this session
     * @param value the object to bind in this session.
     * @throws InvalidSessionException if this session has stopped or expired prior to calling
     *                                 this method.
     */
    void setAttribute(Object key, Object value) throws InvalidSessionException;

    /**
     * Removes (unbinds) the object bound to this session under the specified {@code key} name.
     *
     * @param key the name uniquely identifying the object to remove
     * @return the object removed or {@code null} if there was no object bound under the name
     * {@code key}.
     * @throws InvalidSessionException if this session has stopped or expired prior to calling
     *                                 this method.
     */
    Object removeAttribute(Object key) throws InvalidSessionException;

}
