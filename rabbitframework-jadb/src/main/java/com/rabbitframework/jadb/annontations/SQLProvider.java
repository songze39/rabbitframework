package com.rabbitframework.jadb.annontations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rabbitframework.jadb.mapping.SqlCommendType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SQLProvider {
	Class<?> type();

	String method();

	SqlCommendType sqlType();
}
