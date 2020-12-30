package com.mini.sdk.callback;

public interface PlatformLoginCallback {
	public void platformLogin(String username, String password, boolean save_password);
}
