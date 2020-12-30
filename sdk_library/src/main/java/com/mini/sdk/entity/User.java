package com.mini.sdk.entity;

import com.google.gson.annotations.SerializedName;

public class User{

	
	@SerializedName("account")
	private String account;
	
	@SerializedName("password")
	private String password;

	public User() {

	}

	public User(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

 
}
