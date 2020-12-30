package com.mini.sdk.bean;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.http.process.LoginProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.SharedPreferenceUtil;

public class UserReLogin {

	private ReLoginCallback loginCallback;

	public interface ReLoginCallback {
		 void reLoginResult(boolean b);
	}

	private Context context;

	/**
	 * 重新登录
	 */
	public UserReLogin(Context context) {
		Logs.i("重新登录...");
		this.context = context;
	}

	/**
	 * 用户重新登录发起请求
	 *
	 * @param callback 用户重新登录的回调接口
	 */
	public void userToLogin(ReLoginCallback callback) {
		this.loginCallback = callback;
		LoginProcess loginprocess = new LoginProcess();
		loginprocess.setAccount(SharedPreferenceUtil.getUsers(context).getFirst().getAccount());
		loginprocess.setPassword(SharedPreferenceUtil.getUsers(context).getFirst().getPassword());
		loginprocess.post(mHandler);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.LOGIN_SUCCESS:// 登陆成功
				UserLogin loginSuccess = (UserLogin) msg.obj;
				PluginApi.userLogin = loginSuccess;
				if (null != loginCallback) {
					loginCallback.reLoginResult(true);
				}
				break;
			case Constant.LOGIN_FAIL:// 登陆失败
				loginCallback.reLoginResult(false);
				break;
			}
		}
	};
}
