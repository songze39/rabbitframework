<#if packageName??>
package ${packageName};
</#if>
<#list entity.importPackage as importPackage>
import ${importPackage}
</#list>
import com.rabbitframework.jadb.annontations.*;
@Table
public class ${entity.objectName} {
<#list entity.idProperties as idProperties>
    @ID
    private ${idProperties.javaType.shortName} ${idProperties.javaProperty};
</#list>
<#list entity.columnProperties as columnProperties>
    @Column
    private ${columnProperties.javaType.shortName} ${columnProperties.javaProperty};
</#list>

<#list entity.idProperties as mIdProperties>
    public void set${mIdProperties.upperJavaProperty}(${mIdProperties.javaType.shortName} ${mIdProperties.javaProperty}) {
        this.${mIdProperties.javaProperty} = ${mIdProperties.javaProperty};
    }
    public ${mIdProperties.javaType.shortName} get${mIdProperties.upperJavaProperty}() {
        return ${mIdProperties.javaProperty}
    }
</#list>
<#list entity.columnProperties as mColumnProperties>
    public void set${mColumnProperties.upperJavaProperty}(${mColumnProperties.javaType.shortName} ${mColumnProperties.javaProperty}) {
        this.${mColumnProperties.javaProperty} = ${mColumnProperties.javaProperty};
    }
    public ${mColumnProperties.javaType.shortName} get${mColumnProperties.upperJavaProperty}() {
        return ${mColumnProperties.javaProperty}
    }
</#list>
}
