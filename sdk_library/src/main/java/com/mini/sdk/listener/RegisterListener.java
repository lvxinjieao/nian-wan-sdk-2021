package com.mini.sdk.listener;

public interface RegisterListener {

    /**
     * 注册账号成功
     */
    public final static int SDK_REGISTER_SUCCESS = 1;

    /**
     * 注册账号失败
     */
    public final static int SDK_REGISTER_FAILED = -1;

    void result(int code, String message);
}
