package com.rabbitframework.security.subject;

import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.security.util.CollectionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * 简单的实现了{@link MutablePrincipalCollection}
 * 都存储在{@link java.util.LinkedHashMap}中
 */
public class SimplePrincipalCollection implements MutablePrincipalCollection {
    private static final long serialVersionUID = 1L;
    private Map<String, Set> realmPrincipals;
    private transient String cachedToString;

    public SimplePrincipalCollection() {
    }

    public SimplePrincipalCollection(Object principal, String realmName) {
        if (principal instanceof Collection) {
            addAll((Collection) principal, realmName);
        } else {
            add(principal, realmName);
        }
    }

    public SimplePrincipalCollection(Collection principals, String realmName) {
        addAll(principals, realmName);
    }

    public SimplePrincipalCollection(PrincipalCollection principalCollection) {
        addAll(principalCollection);
    }

    protected Collection getPrincipalsLazy(String realmName) {
        if (realmPrincipals == null) {
            realmPrincipals = new LinkedHashMap<String, Set>();
        }
        Set principals = realmPrincipals.get(realmName);
        if (principals == null) {
            principals = new LinkedHashSet();
            realmPrincipals.put(realmName, principals);
        }
        return principals;
    }

    @Override
    public Object getPrimaryPrincipal() {
        if (isEmpty()) {
            return null;
        }
        return iterator().next();
    }

    @Override
    public void add(Object principal, String realmName) {
        if (realmName == null) {
            throw new IllegalArgumentException("realmName argument cannot be null.");
        }
        if (principal == null) {
            throw new IllegalArgumentException("principal argument cannot be null.");
        }
        this.cachedToString = null;
        getPrincipalsLazy(realmName).add(principal);
    }

    @Override
    public void addAll(Collection principals, String realmName) {
        if (realmName == null) {
            throw new IllegalArgumentException("realmName argument cannot be null.");
        }
        if (principals == null) {
            throw new IllegalArgumentException("principals argument cannot be null.");
        }
        if (principals.isEmpty()) {
            throw new IllegalArgumentException("principals argument cannot be an empty collection.");
        }
        this.cachedToString = null;
        getPrincipalsLazy(realmName).addAll(principals);
    }

    @Override
    public void addAll(PrincipalCollection principals) {
        if (principals.getRealmNames() != null) {
            for (String realmName : principals.getRealmNames()) {
                for (Object principal : principals.fromRealm(realmName)) {
                    add(principal, realmName);
                }
            }
        }
    }

    /**
     * 根据类类型获取一个身份认证信息
     *
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T oneByType(Class<T> type) {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return null;
        }
        Collection<Set> values = realmPrincipals.values();
        for (Set set : values) {
            for (Object o : set) {
                if (type.isAssignableFrom(o.getClass())) {
                    return (T) o;
                }
            }
        }
        return null;
    }

    /**
     * 根据类类型获取身份认证集合
     *
     * @param type
     * @param <T>
     * @return {@link LinkedHashSet}
     */
    @Override
    public <T> Collection<T> byType(Class<T> type) {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<T> typed = new LinkedHashSet<T>();
        Collection<Set> values = realmPrincipals.values();
        for (Set set : values) {
            for (Object o : set) {
                if (type.isAssignableFrom(o.getClass())) {
                    typed.add((T) o);
                }
            }
        }
        if (typed.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return Collections.unmodifiableSet(typed);
    }

    @Override
    public List asList() {
        Set all = asSet();
        if (all.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(new ArrayList(all));
    }

    @Override
    public Set asSet() {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set aggregated = new LinkedHashSet();
        Collection<Set> values = realmPrincipals.values();
        for (Set set : values) {
            aggregated.addAll(set);
        }
        if (aggregated.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        return Collections.unmodifiableSet(aggregated);
    }

    @Override
    public Collection fromRealm(String realmName) {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set principals = realmPrincipals.get(realmName);
        if (principals == null || principals.isEmpty()) {
            principals = Collections.EMPTY_SET;
        }
        return Collections.unmodifiableSet(principals);
    }

    @Override
    public Set<String> getRealmNames() {
        if (realmPrincipals == null) {
            return null;
        } else {
            return realmPrincipals.keySet();
        }
    }

    @Override
    public boolean isEmpty() {
        return realmPrincipals == null || realmPrincipals.isEmpty();
    }

    @Override
    public void clear() {
        this.cachedToString = null;
        if (realmPrincipals != null) {
            realmPrincipals.clear();
            realmPrincipals = null;
        }
    }

    @Override
    public Iterator iterator() {
        return asSet().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection other = (SimplePrincipalCollection) o;
            return this.realmPrincipals != null ? this.realmPrincipals.equals(other.realmPrincipals) : other.realmPrincipals == null;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.realmPrincipals != null && !realmPrincipals.isEmpty()) {
            return realmPrincipals.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        if (this.cachedToString == null) {
            Set<Object> principals = asSet();
            if (!CollectionUtils.isEmpty(principals)) {
                this.cachedToString = StringUtils.toString(principals.toArray());
            } else {
                this.cachedToString = "empty";
            }
        }
        return this.cachedToString;
    }


    /**
     * Serialization write support.
     * <p/>
     * NOTE: Don't forget to change the serialVersionUID constant at the top of this class
     * if you make any backwards-incompatible serializatoin changes!!!
     * (use the JDK 'serialver' program for this)
     *
     * @param out output stream provided by Java serialization
     * @throws IOException if there is a stream error
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        boolean principalsExist = !CollectionUtils.isEmpty(realmPrincipals);
        out.writeBoolean(principalsExist);
        if (principalsExist) {
            out.writeObject(realmPrincipals);
        }
    }

    /**
     * Serialization read support - reads in the Map principals collection if it exists in the
     * input stream.
     * <p/>
     * NOTE: Don't forget to change the serialVersionUID constant at the top of this class
     * if you make any backwards-incompatible serializatoin changes!!!
     * (use the JDK 'serialver' program for this)
     *
     * @param in input stream provided by
     * @throws IOException            if there is an input/output problem
     * @throws ClassNotFoundException if the underlying Map implementation class is not available to the classloader.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        boolean principalsExist = in.readBoolean();
        if (principalsExist) {
            this.realmPrincipals = (Map<String, Set>) in.readObject();
        }
    }
}
