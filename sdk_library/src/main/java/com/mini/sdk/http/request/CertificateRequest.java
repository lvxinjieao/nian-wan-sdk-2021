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

public class CertificateRequest {

	private static final String TAG = "CertificateRequest";

	Handler mHandler;

	public CertificateRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}

	}

	public void post(String url, String params) {

		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.TO_CERTIFICATE_FAIL, "url未设或参数为空");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {

			@Override
			public void requestSuccess(String result) {
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(result);
					int status = jsonObject.optInt("status");
					if (status == 200 || status == 1) {
						String idcard = jsonObject.optString("idcard", "");
						noticeResult(Constant.TO_CERTIFICATE_SUCCESS, idcard);
					} else {
						String msg;
						if (!TextUtils.isEmpty(jsonObject.optString("return_msg"))) {
							msg = jsonObject.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.TO_CERTIFICATE_FAIL, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Logs.e("解析异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {
				noticeResult(Constant.TO_CERTIFICATE_FAIL, "网络异常");
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
