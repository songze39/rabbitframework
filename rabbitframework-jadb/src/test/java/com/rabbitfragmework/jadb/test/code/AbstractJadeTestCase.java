package com.rabbitfragmework.jadb.test.code;

import java.io.IOException;

import org.junit.Before;

public abstract class AbstractJadeTestCase {
	protected abstract void initJadb() throws IOException;

	@Before
	public void before() {
		try {
			initJadb();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
