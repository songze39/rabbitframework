package com.rabbitfragmework.jadb.test.intercept;

import com.rabbitframework.jadb.annontations.Intercept;
import com.rabbitframework.jadb.intercept.Interceptor;
import com.rabbitframework.jadb.intercept.Invocation;
import com.rabbitframework.jadb.intercept.Plugin;

@Intercept(args = { String.class }, method = "print", interfaceType = SimplePrintInteface.class)
public class SimpleIntercept2 implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("SimpleIntercept2");
		return invocation.process();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
