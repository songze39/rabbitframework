package com.rabbitframework.jadb.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.rabbitframework.commons.reflect.MetaClass;
import com.rabbitframework.commons.utils.StringUtils;
import com.rabbitframework.jadb.annontations.Column;
import com.rabbitframework.jadb.annontations.ID;
import com.rabbitframework.jadb.annontations.Table;
import com.rabbitframework.jadb.mapping.EntityMap;
import com.rabbitframework.jadb.mapping.EntityProperty;
import com.rabbitframework.jadb.mapping.GenerationType;

/**
 * 实体类解析
 *
 * @author Justin Liang
 *
 *
 */
public class EntityBuilder extends BaseBuilder {
	public EntityBuilder(Configuration configuration) {
		super(configuration);
	}

	public EntityMap parseEntity(Class<?> entity) {
		String entityId = entity.getName(); // 获取完整类名当唯一标识
		Table table = entity.getAnnotation(Table.class); // 获取当前实体表名
		if (table == null) {
			return null;
		}
		String tableName = table.name();
		if (StringUtils.isBlank(tableName)) {
			tableName = converPropertyToDbName(entity.getSimpleName());
		}
		List<EntityProperty> entityProperties = new ArrayList<EntityProperty>();
		parserEntityProperty(entity, entityProperties);
		EntityMap entityMap = new EntityMap.Builder(entityId, tableName,
				entity, entityProperties).build();
		return entityMap;
	}

	/**
	 *
	 * 将类属性转换成数据库名称 转换规则以字母大写分割，添加下划线分隔符 如:converPropertyToDbName(fileName)
	 * return "file_name"
	 *
	 * @param property
	 * @return
	 */
	private String converPropertyToDbName(String property) {
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

	private void parserEntityProperty(Class<?> entity,
			List<EntityProperty> entityProperties) {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			String property = field.getName();
			ID id = field.getAnnotation(ID.class);
			Column column = field.getAnnotation(Column.class);
			if (id == null && column == null) {
				continue;
			}

			Class<?> javaType = null;
			long length = 255;
			String columnName = "";
			GenerationType generationType = null;
			String selectKey = "";
			if (id != null) {
				generationType = id.keyType();
				selectKey = id.selectKey();
				columnName = id.column();
				length = id.length(); // 长度
			} else if (column != null) {
				columnName = column.column();
				length = column.length(); // 长度
			}
			if (StringUtils.isBlank(columnName)) {
				columnName = converPropertyToDbName(property);
			}
			EntityProperty entityProperty = buildEntityProperty(property,
					entity, length, javaType, selectKey, generationType,
					columnName);
			entityProperties.add(entityProperty);
		}
		if (entity.getSuperclass() != null) {
			parserEntityProperty(entity.getSuperclass(), entityProperties);
		}
	}

	private EntityProperty buildEntityProperty(String property,
			Class<?> entity, long length, Class<?> javaType, String selectKey,
			GenerationType generationType, String column) {
		Class<?> javaTypeClass = resolveResultJavaType(entity, property,
				javaType);
		EntityProperty.Builder builder = new EntityProperty.Builder(property,
				column);
		builder.javaType(javaTypeClass);
		builder.length(length);
		builder.generationType(generationType);
		builder.selectKey(selectKey);
		return builder.build();
	}

	/**
	 * 获取javaType类型
	 *
	 * @param resultType
	 * @param property
	 * @param javaType
	 * @return
	 */
	private Class<?> resolveResultJavaType(Class<?> resultType,
			String property, Class<?> javaType) {
		if ((javaType == null) && property != null) {
			try {
				MetaClass metaResultType = MetaClass.forClass(resultType);
				javaType = metaResultType.getSetterType(property);
			} catch (Exception e) {
				// ignore
			}
		}
		if (javaType == null) {
			javaType = Object.class;
		}
		return javaType;
	}

}
