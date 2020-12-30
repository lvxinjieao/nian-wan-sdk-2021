package com.mini.sdk.listener;

public interface LogoutListener {

	/** 退出账号成功 */
	public final static int SDK_LOGOUT_SUCCESS = 1;

	/** 退出账号失败 */
	public final static int SDK_LOGOUT_FAILED = -1;

	void result(int code, String message);

}
