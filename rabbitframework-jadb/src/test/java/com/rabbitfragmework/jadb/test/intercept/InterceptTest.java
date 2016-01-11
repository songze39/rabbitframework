package com.rabbitfragmework.jadb.test.intercept;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitframework.jadb.intercept.InterceptorChain;

import junit.framework.TestCase;

public class InterceptTest extends TestCase {
	private static Logger logger = LogManager.getLogger(InterceptTest.class);

	public void testIntercept() {
		InterceptorChain interceptorChain = new InterceptorChain();
		SimpleIntercept simpleIntercept = new SimpleIntercept();
		SimpleIntercept2 simpleIntercept2 = new SimpleIntercept2();
		interceptorChain.addInterceptor(simpleIntercept);
		interceptorChain.addInterceptor(simpleIntercept2);
		SimplePrintInteface simplePrint = new SimplePrint();
		SimplePrintInteface print = (SimplePrintInteface) interceptorChain
				.pluginAll(simplePrint);
		print.print("testIntercept");
	}
}