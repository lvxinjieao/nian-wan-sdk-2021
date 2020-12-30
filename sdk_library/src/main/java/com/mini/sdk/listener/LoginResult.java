package com.mini.sdk.listener;

public class LoginResult {

	public final static int SDK_LOGIN_FAILED = -1;// 登录失败
	public final static int SDK_LOGIN_SUCCESS = 1;// 登录成功

	private int code;// 操作结果
	private String message;
	
	private String account;
	private String accountId;
	private String token;

	private String sign;

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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
