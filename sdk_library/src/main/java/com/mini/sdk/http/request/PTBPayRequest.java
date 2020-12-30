package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.PTBPayResult;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PTBPayRequest {

	private static final String TAG = "PTBPayRequest";

	Handler mHandler;

	public PTBPayRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {

		if (TextUtils.isEmpty(url) || null == params) {
			noticeResult(Constant.PTB_PAY_FAIL, "参数异常");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				PTBPayResult ptbPayResult = new PTBPayResult();
				int status;
				try {
					JSONObject json = new JSONObject(result);
					status = json.optInt("status");

					if (status == 200 || status == 1) {
						ptbPayResult.setReturn_code(json.optString("return_code"));
						ptbPayResult.setReturn_status("1");
						ptbPayResult.setReturn_msg(json.optString("return_msg"));
						ptbPayResult.setOrderNumber(json.optString("out_trade_no"));
						noticeResult(Constant.PTB_PAY_SUCCESS, ptbPayResult);
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.PTB_PAY_FAIL, msg);
						PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "平台币 支付失败");
					}

				} catch (JSONException e) {
					noticeResult(Constant.PTB_PAY_FAIL, "解析参数异常");
					Logs.e("fun#post JSONException:" + e);
				} catch (Exception e) {
					noticeResult(Constant.PTB_PAY_FAIL, "网络异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {

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
