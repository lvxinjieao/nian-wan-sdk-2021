package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.entity.UserRegister;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class RegisterPhoneRequest {

	Handler mHandler;
	String mUserName;
	String mPassword;

	public RegisterPhoneRequest(Handler mHandler, String userName, String password) {

		if (null != mHandler) {
			this.mHandler = mHandler;
		}
		if (!TextUtils.isEmpty(userName)) {
			this.mUserName = userName;
		} else {
			this.mUserName = "";
		}
		if (!TextUtils.isEmpty(password)) {
			this.mPassword = password;
		} else {
			this.mPassword = "";
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("手机注册 post url is null add params is null");
			noticeResult(Constant.REGISTER_FAIL, "url未设或参数为空");
			return;
		}
		Logs.i("手机注册 post url = " + url);
		Logs.i("手机注册 post params = " + params);


		com.mini.sdk.utils.HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				UserRegister registerSuccess = new UserRegister();
				registerSuccess.setUserName(mUserName);
				registerSuccess.setPassword(mPassword);

				String return_code = "";
				String return_msg = "注册失败";
				int status = 0;

				JSONObject json = null;
				try {
					json = new JSONObject(result);
					status = json.optInt("status");
					return_code = json.optString("return_code");
					return_msg = json.optString("return_msg");
					registerSuccess.setCode(1);
				} catch (JSONException e) {
					return_msg = "数据解析异常";
					status = 0;
				} catch (Exception e) {
					return_msg = "数据解析异常";
					status = 0;
				}

				if (status == 200 || status == 1) {
					registerSuccess.setMessage(return_msg);
					registerSuccess.setAccountId(return_code);
					noticeResult(Constant.REGISTER_SUCCESS, registerSuccess);
				} else {
					String msg;
					if (!TextUtils.isEmpty(json.optString("return_msg"))) {
						msg = json.optString("return_msg");
					} else {
						msg = CodeUtils.getErrorMsg(status);
					}
					Logs.e("msg:" + msg);
					noticeResult(Constant.REGISTER_FAIL, msg);
				}

			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.REGISTER_FAIL, "网络异常");
			}
		});
	}

	private void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}

}
