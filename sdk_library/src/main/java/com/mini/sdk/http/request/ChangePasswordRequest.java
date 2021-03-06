package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class ChangePasswordRequest {

	private static final String TAG = "ChangePasswordRequest";

	Handler mHandler;

	public ChangePasswordRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.MODIFY_PASSWORD_FAIL, "参数异常");
			return;
		}
		Logs.i("修改密码 url = " + url);
		Logs.i("修改密码 params = " + params);

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status;
				String tip = "";

				try {
					JSONObject json = new JSONObject(result);
					status = json.optInt("status");
					tip = json.optString("return_msg");
					Logs.e("tip:" + tip);
				} catch (JSONException e) {
					status = -1;
					tip = "参数异常";
				} catch (Exception e) {
					status = -1;
					tip = "参数异常";
				}
				if (status == 200 || status == 1) {
					noticeResult(Constant.MODIFY_PASSWORD_SUCCESS, "");
				} else {
					noticeResult(Constant.MODIFY_PASSWORD_FAIL, tip);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.MODIFY_PASSWORD_FAIL, "网络异常");
			}
		});



	}

	private void noticeResult(int type, String str) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = str;
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
