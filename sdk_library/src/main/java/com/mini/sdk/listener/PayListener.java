package com.mini.sdk.listener;

public interface PayListener {
	public static final int SDK_PAY_FAILED = -1;// ("支付失败");
	public static final int SDK_PAY_CANCEL = 0;// 用户取消");
	public static final int SDK_PAY_SUCCEED = 1;// ("支付买成功");

	void result(int code, String massage);

}
