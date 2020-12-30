package com.mini.sdk.entity;

public class UserRegister {

	/** 用户名 */
	private String userName;
	/** 密码 */
	private String password;
	/** 状态 */
	private int code;
	/** 描述信息 */
	private String message;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
