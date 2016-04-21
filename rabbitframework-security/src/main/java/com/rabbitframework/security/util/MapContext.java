package com.rabbitframework.security.util;

import java.io.Serializable;
import java.util.*;

/**
 * map上下文,实现{@link Map}
 *
 * @author: justin.liang
 * @date: 16/4/21 上午10:32
 */
public class MapContext implements Map<String, Object>, Serializable {
    private final Map<String, Object> backingMap;

    public MapContext() {
        this.backingMap = new HashMap<String, Object>();
    }

    public MapContext(Map<String, Object> map) {
        this();
        if (!CollectionUtils.isEmpty(map)) {
            backingMap.putAll(map);
        }
    }

    protected <E> E getTypedValue(String key, Class<E> type) {
        E found = null;
        Object o = backingMap.get(key);
        if (o != null) {
            if (!type.isAssignableFrom(o.getClass())) {
                String msg = "Invalid object found in SubjectContext Map under key [" + key + "].  Expected type " +
                        "was [" + type.getName() + "], but the object under that key is of type " +
                        "[" + o.getClass().getName() + "].";
                throw new IllegalArgumentException(msg);
            }
            found = (E) o;
        }
        return found;
    }

    protected void nullSafePut(String key, Object value) {
        if (value != null) {
            put(key, value);
        }
    }


    @Override
    public int size() {
        return backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return backingMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return backingMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return backingMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        backingMap.putAll(m);
    }

    @Override
    public void clear() {
        backingMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(backingMap.keySet());
    }

    @Override
    public Collection<Object> values() {
        return Collections.unmodifiableCollection(backingMap.values());
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(backingMap.entrySet());
    }
}
