package com.rabbitframework.security;

public class SecurityUser implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private int userId;
	private String loginName;

	public SecurityUser() {

	}

	public SecurityUser(int userId, String loginName) {
		this.userId = userId;
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String toString() {
		return loginName;
	}
}
