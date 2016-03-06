package com.rabbitfragmework.jadb.test.intercept;

public class SimplePrint implements SimplePrintInteface {
	public void print(String value) {
		System.out.println("SimplePrint:" + SimplePrint.class + "," + value);
	}
}
