package com.mini.sdk.listener;

public interface ExitListener {

	public final static int SDK_EXIT_SUCCESS = 1;
	public final static int SDK_EXIT_FAILED = -1;

	void result(int code, String message);
}
