package com.rabbitframework.jadb.mapping.rowmapper;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

public class RowMapperUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static RowMapper getRowMapper(Method method) {
		RowMapper<?> rowMapper = null;
		Class<?> returnType = method.getReturnType();
		Class<?> returnParamType = getReturnType(method);
		if (returnParamType.isPrimitive()) {
			returnParamType = ClassUtils.primitiveToWrapper(returnParamType);
		}
		// 判断是否返回单列 Map<String,String>
		if (isColumnType(returnParamType)) {
			if (Map.class.isAssignableFrom(returnType)) {
				rowMapper = new ColumnMapRowMapper();
			} else {
				rowMapper = new SingleColumnRowMapper(returnParamType);
			}
		} else if (returnParamType == Map.class) {
			rowMapper = new ColumnMapRowMapper();
		} else if (((returnParamType == List.class) || (returnParamType == Collection.class))) {
			rowMapper = new ListRowMapper(Object.class);
		} else if (returnParamType == Set.class) {
			rowMapper = new SetRowMapper(Object.class);
		} else {
			rowMapper = new BeanPropertyRowMapper(returnParamType);
		}
		return rowMapper;
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
		} else if (Collection.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
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
		} else if (Map.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
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

	public static boolean isColumnType(Class<?> columnTypeCandidate) {
		return String.class == columnTypeCandidate // NL
				|| org.springframework.util.ClassUtils.isPrimitiveOrWrapper(columnTypeCandidate)// NL
				|| Date.class.isAssignableFrom(columnTypeCandidate) // NL
				|| columnTypeCandidate == byte[].class // NL
				|| columnTypeCandidate == BigDecimal.class // NL
				|| columnTypeCandidate == Blob.class // NL
				|| columnTypeCandidate == Clob.class;
	}
}
