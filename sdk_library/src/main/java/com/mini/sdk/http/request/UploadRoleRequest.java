package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UploadRoleRequest {

	private static final String TAG = "UploadRoleRequest";

	Handler mHandler;
	String mRole_name;

	public UploadRoleRequest(Handler mHandler, String role_name) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
		if (!TextUtils.isEmpty(role_name)) {
			this.mRole_name = role_name;
		} else {
			this.mRole_name = "";
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.UPLOAD_ROLE_FAIL, "参数为空");
			return;
		}
		Logs.w("fun#post url = " + url);

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status = -1;
				try {
					JSONObject json = new JSONObject(result);
					status = json.optInt("status");
					if (status == 200 || status == 1) {
						noticeResult(Constant.UPLOAD_ROLE_SUCCESS, json.optString("return_msg"));
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.UPLOAD_ROLE_FAIL, msg);
					}
				} catch (JSONException e) {
					noticeResult(Constant.UPLOAD_ROLE_FAIL, "解析数据异常");
				} catch (Exception e) {
					noticeResult(Constant.UPLOAD_ROLE_FAIL, "解析数据异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.UPLOAD_ROLE_FAIL, "网络异常");
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
