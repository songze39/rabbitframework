package com.rabbitfragmework.jadb.test.model;

import com.rabbitframework.jadb.annontations.Column;
import com.rabbitframework.jadb.annontations.ID;
import com.rabbitframework.jadb.annontations.Table;

@Table
public class TestUser implements java.io.Serializable {
	private static final long serialVersionUID = 6601565142528523969L;
	@ID
	private long id;
	@Column
	private String testName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

}
