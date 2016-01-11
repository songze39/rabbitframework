package com.rabbitframework.jadb.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rabbitframework.jadb.dataaccess.dialect.Dialect;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DbDialect {
	Class<? extends Dialect> dialect();
}
