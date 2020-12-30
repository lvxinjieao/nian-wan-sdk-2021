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

public class VerificationOrderRequest {


	Handler mHandler;

	public VerificationOrderRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {

		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.VERIFICATION_FAIL, "参数异常");
			return;
		}


		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				try {
					JSONObject json = new JSONObject(result);
					int status = json.optInt("status");
					if (status == 200 || status == 1) {
						noticeResult(Constant.VERIFICATION_SUCCESS, "支付成功");
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.VERIFICATION_FAIL, msg);
					}
				} catch (JSONException e) {
					// verificationResult = null;
					Logs.e("fun#post JSONException:" + e);
				} catch (Exception e) {
					noticeResult(Constant.VERIFICATION_FAIL, "服务器异常");
					Logs.e("fun#post JSONException:" + e);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.VERIFICATION_FAIL, "网络异常");
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
