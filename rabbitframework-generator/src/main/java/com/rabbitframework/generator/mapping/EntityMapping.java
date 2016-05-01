package com.rabbitframework.generator.mapping;

import java.util.List;

public class EntityMapping {
    private String tableName;
    private String objectName;
    private List<EntityProperty> entityProperties;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getObjectName() {
        return objectName;
    }



    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<EntityProperty> getEntityProperties() {
        return entityProperties;
    }

    public void setEntityProperties(List<EntityProperty> entityProperties) {
        this.entityProperties = entityProperties;
    }
}
