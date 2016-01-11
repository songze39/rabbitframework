package com.rabbitfragmework.jadb.test.builder;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.rabbitfragmework.jadb.test.mapper.TestUserMapper;
import com.rabbitframework.jadb.annontations.SQLProvider;
import com.rabbitframework.jadb.builder.Configuration;
import com.rabbitframework.jadb.builder.MapperParser;
import com.rabbitframework.jadb.mapping.MappedStatement;

public class MapperParserTest {
	private static final Logger logger = LogManager
			.getLogger(MapperParserTest.class);

	public static void main(String[] args) {
		System.out.println(Object.class.getSimpleName().equals(String.class.getSimpleName()));
	}

	// @User
	public void testMapperParser() {
		Configuration configuration = new Configuration();
		MapperParser mapperParser = new MapperParser(configuration,
				TestUserMapper.class);
		mapperParser.parse();
		Collection<MappedStatement> mappedStatements = configuration
				.getMappedStatements();
		logger.debug("mappedStatements.size():" + mappedStatements.size());
		Iterator<MappedStatement> mappedStatementIterator = mappedStatements
				.iterator();
		while (mappedStatementIterator.hasNext()) {
			MappedStatement mappedStatement = mappedStatementIterator.next();
			logger.debug("id:" + mappedStatement.getId());
		}
	}

	// @User
	public void testMapperSQLProvider() throws Exception {
		Method method = TestUserMapper.class.getMethod("insertTest", null);
		SQLProvider sqlProvider = method.getAnnotation(SQLProvider.class);
		Class<?> typeClazz = sqlProvider.type();
		String methodName = sqlProvider.method();
		Method methodAnn = typeClazz.getMethod(methodName, null);
		String string = (String) methodAnn.invoke(typeClazz.newInstance());
		logger.debug("value:" + string);
	}

	@Test
	public void testMapperType() throws Exception {
		Method method = TestUserMapper.class.getMethod("selectTest", null);
		Class<?> result = getReturnType(method);
		System.out.println("result:" + result);
	}

	private Class<?> getReturnType(Method method) {
		Class<?> returnType = method.getReturnType();
		if (void.class.equals(returnType)) {
			return returnType;
		} else if (Collection.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
			if (returnTypeParameter instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter)
						.getActualTypeArguments();
				if (actualTypeArguments != null
						&& actualTypeArguments.length == 1) {
					returnTypeParameter = actualTypeArguments[0];
					if (returnTypeParameter instanceof Class) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter)
								.getRawType();
					} else if (returnTypeParameter instanceof GenericArrayType) {
						Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
								.getGenericComponentType();
						returnType = Array.newInstance(componentType, 0)
								.getClass();
					}
				}
			}
		} else if (Map.class.isAssignableFrom(returnType)) {
			Type returnTypeParameter = method.getGenericReturnType();
			if (returnTypeParameter instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter)
						.getActualTypeArguments();
				if (actualTypeArguments != null
						&& actualTypeArguments.length == 2) {
					returnTypeParameter = actualTypeArguments[1];
					if (returnTypeParameter instanceof Class) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter)
								.getRawType();
					}
				}
			}
		}
		return returnType;
	}
}
