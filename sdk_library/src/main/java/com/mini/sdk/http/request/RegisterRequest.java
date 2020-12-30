package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.UserRegister;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterRequest {

	Handler handler;
	String username;
	String password;

	public RegisterRequest(Handler handler_, String username_, String password_) {
		if (null != handler_) {
			this.handler = handler_;
		}
		if (!TextUtils.isEmpty(username_)) {
			this.username = username_;
		} else {
			this.username = "";
		}
		if (!TextUtils.isEmpty(password_)) {
			this.password = password_;
		} else {
			this.password = "";
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("Register post url is null add params is null");
			noticeResult(Constant.REGISTER_FAIL, "url未设或参数为空");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				UserRegister registerSuccess = new UserRegister();
				registerSuccess.setUserName(username);
				registerSuccess.setPassword(password);

				String res = "注册失败";
				int status;
				JSONObject json = null;

				try {
					json = new JSONObject(result);
					status = json.optInt("status");
					res = json.optString("return_msg");
					registerSuccess.setCode(1);
				} catch (JSONException e) {
					res = "数据解析异常";
					status = 0;
				} catch (Exception e) {
					res = "数据解析异常";
					status = 0;
				}

				if (status == 200 || status == 1) {
					noticeResult(Constant.REGISTER_SUCCESS, registerSuccess);
				} else {
					String message;
					if (!TextUtils.isEmpty(json.optString("return_msg"))) {
						message = json.optString("return_msg");
					} else {
						message = CodeUtils.getErrorMsg(status);
					}
					Logs.e("message : " + message);
					registerSuccess.setMessage(message);
					noticeResult(Constant.REGISTER_FAIL, message);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				noticeResult(Constant.REGISTER_FAIL, "网络异常");
			}
		});


	}

	private void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;
		if (null != handler) {
			handler.sendMessage(msg);
		}
	}

}
