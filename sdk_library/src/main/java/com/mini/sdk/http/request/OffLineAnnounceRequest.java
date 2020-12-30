package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;


public class OffLineAnnounceRequest {
	private static final String TAG = "OffLineRequest";

	Handler mHandler;

	public OffLineAnnounceRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.LOGIN_FAIL, "参数为空");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status;
				try {
					JSONObject obj = new JSONObject(result);
					status = obj.getInt("status");
					if (status == 200 || status == 1) {
						noticeResult(Constant.OFFLINE_SUCCESS, obj.optString("return_msg"));
					} else {
						String msg;
						if (!TextUtils.isEmpty(obj.optString("return_msg"))) {
							msg = obj.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.OFFLINE_FAIL, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					noticeResult(Constant.OFFLINE_FAIL, "解析数据异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {
				noticeResult(Constant.OFFLINE_FAIL, "网络异常");
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
