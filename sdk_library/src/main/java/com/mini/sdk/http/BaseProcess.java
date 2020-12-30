package com.mini.sdk.http;

import android.os.Handler;

public abstract class BaseProcess {

	public abstract String getParamStr();

	public abstract void post(Handler handler);
}
