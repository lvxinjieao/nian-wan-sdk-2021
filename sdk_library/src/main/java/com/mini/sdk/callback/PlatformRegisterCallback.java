package com.mini.sdk.callback;

import android.view.View;

public interface PlatformRegisterCallback {

	public void platformRegister(String username, String password, String repassword, String msg, String result,
                                 boolean isRead);

	public void getPhoneValidateMessage(View v, String userName, RefreshVerifyCode refreshCode);
}
