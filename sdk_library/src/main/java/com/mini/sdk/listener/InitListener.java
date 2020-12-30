package com.mini.sdk.listener;

public interface InitListener {

	public final static int SDK_INIT_SUCCESS = 1;
	public final static int SDK_INIT_FAILED = -1;

	void result(int code, String message);

}
