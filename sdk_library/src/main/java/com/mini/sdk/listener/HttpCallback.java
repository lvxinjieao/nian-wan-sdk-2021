package com.mini.sdk.listener;

import java.io.IOException;

public interface HttpCallback {

	/** 请求成功 */
	void requestSuccess(String result);

	/** 请求失败 */
	void requestFailure(IOException e);

}
