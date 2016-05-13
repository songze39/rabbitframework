package com.rabbitframework.commons.utils;

import com.rabbitframework.commons.exceptions.ReflectionException;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Map;

/**
 * @author: justin.liang
 * @date: 16/5/14 上午12:38
 */
public class ReflectUtils {

    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }


    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        return getGenericClass(clazz, null, index);
    }

    public static Class<?> getGenericClass(Class<?> clazz, Class<?> superclass, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            if (superclass != null) {
                if (superclass != genericSuperclass) {
                    return getGenericClass(clazz.getSuperclass(), superclass, index);
                }
                throw new ReflectionException(clazz.getName() + "not extends " + superclass.getName());
            }
        }
        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index
                    + ", Size of Parameterized Type: " + params.length);
        }
        Type rawType = params[index];
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return (Class<?>) rawType;
    }

    /**
     * 获取返回类型
     * <p/>
     * 获取泛型中的值
     *
     * @param method
     * @return
     */
    public static Class<?> getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (void.class.equals(returnType)) {
            return returnType;
        } else {
            returnType = getType(returnType, method.getGenericReturnType());
        }

        return returnType;
    }

    private static Class<?> getType(Class<?> type, Type returnTypeParameter) {
        Class<?> returnType = type;
        if (Collection.class.isAssignableFrom(type)) {
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
                                .getGenericComponentType();
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            }
        } else if (Map.class.isAssignableFrom(type)) {
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    returnTypeParameter = actualTypeArguments[1];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }
                }
            }
        }
        return returnType;
    }

    public static Class<?> getGenericClassByField(Field field) {
        Type genericType = field.getGenericType();
        Class<?> type = field.getType();
        if (void.class.equals(type)) {
            return type;
        } else {
            type = getType(type, genericType);
        }
        return type;
    }


}
