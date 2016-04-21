package com.rabbitframework.security.subject;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 身份集合,继承{@link Iterable}
 *
 * @author: justin.liang
 * @date: 16/4/19 下午9:56
 */
public interface PrincipalCollection extends Iterable, Serializable {
    /**
     * 获取主要的身份
     *
     * @return
     */
    Object getPrimaryPrincipal();

    /**
     * 根据身份类型获取第一个
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T oneByType(Class<T> type);

    /**
     * 根据身份类型获取一组
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> Collection<T> byType(Class<T> type);

    /**
     * 将身份集合转换为{@link List}
     *
     * @return
     */
    List asList();

    /**
     * 将身份集合转换为{@link Set}
     *
     * @return
     */
    Set asSet();

    /**
     * 根据 Realm 名字(每个 Principal 都与一个 Realm 关联)获取相应的身份。
     *
     * @param realmName
     * @return
     */
    Collection fromRealm(String realmName);

    /**
     * 获取所有realm名字
     *
     * @return
     */
    Set<String> getRealmNames();

    /**
     * 判断当前是否为空
     *
     * @return
     */
    boolean isEmpty();
}
