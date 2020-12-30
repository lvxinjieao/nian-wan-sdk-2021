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

/**
 * 描述：获取微信官方支付结果 作者：苏杭 时间: 2017-12-11 17:00
 */
public class WXPayResultRequest {
	
	Handler mHandler;

	public WXPayResultRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("微信支付 url is null add params is null");
			noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, "参数为空");
			return;
		}


		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status = -1;
				try {
					JSONObject obj = new JSONObject(result);
					status = obj.optInt("status");
					if (status == 200 || status == 1) {
						noticeResult(Constant.GET_WX_PAY_RESULT_SUCCESS, "支付成功");
					} else {
						String msg;
						if (!TextUtils.isEmpty(obj.optString("return_msg"))) {
							msg = obj.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.w("msg:" + msg);
						noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, msg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure:" + e);
				noticeResult(Constant.GET_WX_PAY_RESULT_FAIL, "网络异常");
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
