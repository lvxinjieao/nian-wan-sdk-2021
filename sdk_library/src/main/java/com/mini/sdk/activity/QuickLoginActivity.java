package com.mini.sdk.activity;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.listener.LoginResult;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.DensityUtil;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.RequestParamUtil;
import com.mini.sdk.view.QuickLoginView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuickLoginActivity extends Activity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		QuickLoginView quickLogin = new QuickLoginView(QuickLoginActivity.this);
		setContentView(quickLogin.getContentView());

		Window dialogWindow = this.getWindow();
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = this.getWindow().getAttributes();

		int width = display.getWidth();
		int height = display.getHeight();
		if (width > height) {// 横屏
			params.width = (int) (display.getWidth() * 0.60); // 宽度设置为屏幕的0.7
		} else {
			params.width = (int) (display.getWidth() * 0.85); // 宽度设置为屏幕的0.7
		}

		dialogWindow.setGravity(Gravity.TOP);
		params.y = DensityUtil.dip2px(this, 10); // 新位置Y坐标
		getWindow().setAttributes(params); // 设置生效

	}

	@Override
	public void onBackPressed() {
		LoginResult loginResult = new LoginResult();
		loginResult.setCode(LoginResult.SDK_LOGIN_FAILED);
		loginResult.setMessage("取消登陆");
		PluginApi.loginListener.result(loginResult);
	}

}
