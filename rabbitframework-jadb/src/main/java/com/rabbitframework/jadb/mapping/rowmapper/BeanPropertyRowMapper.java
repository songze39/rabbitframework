package com.rabbitframework.jadb.mapping.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import com.rabbitframework.commons.reflect.MetaClass;
import com.rabbitframework.commons.reflect.MetaObject;
import com.rabbitframework.commons.reflect.SystemMetaObject;

@SuppressWarnings("rawtypes")
public class BeanPropertyRowMapper implements RowMapper {
	protected final Logger logger = LogManager.getLogger(getClass());
	private final Class<?> mappedClass;
	private Map<String, String> mappedFields;

	public BeanPropertyRowMapper(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		initialize();
	}

	/**
	 * Initialize the mapping metadata for the given class.
	 *
	 * @param mappedClass
	 *            the mapped class.
	 */
	protected void initialize() {
		this.mappedFields = new HashMap<String, String>();
		MetaClass metaClass = MetaClass.forClass(mappedClass);
		String[] propertyNames = metaClass.getSetterNames();
		for (String property : propertyNames) {
			String fields = converPropertyToDbName(property);
			mappedFields.put(fields, property);
		}

	}

	public static String converPropertyToDbName(String property) {
		StringBuilder result = new StringBuilder();
		if (property != null && property.length() > 0) {
			result.append(property.substring(0, 1).toLowerCase());
			for (int i = 1; i < property.length(); i++) {
				char ch = property.charAt(i);
				if (Character.isUpperCase(ch)) {
					result.append("_");
					result.append(Character.toLowerCase(ch));
				} else {
					result.append(ch);
				}
			}
		}
		return result.toString();
	}

	public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Object mappedObject = instantiateClass(this.mappedClass);
		MetaObject metaObject = SystemMetaObject.forObject(mappedObject);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index)
					.toLowerCase(Locale.ENGLISH);
			String property = mappedFields.get(column);
			if (metaObject.hasSetter(property)) {
				Class<?> type = metaObject.getSetterType(property);
				Object obj = JdbcUtils.getResultSetValue(rs, index, type);
				metaObject.setValue(property, obj);
			}
		}
		return mappedObject;
	}

	private static Object instantiateClass(Class<?> clazz)
			throws BeanInstantiationException {
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			throw new BeanInstantiationException(clazz, ex.getMessage(), ex);
		}
	}

}
