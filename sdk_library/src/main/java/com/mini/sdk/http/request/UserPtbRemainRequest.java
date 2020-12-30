package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.UserPTBInfo;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UserPtbRemainRequest {

	Handler mHandler;

	public UserPtbRemainRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.PTB_MONEY_FAIL, "参数为空");
			return;
		}


		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				UserPTBInfo userPtb = new UserPTBInfo();
				try {
					JSONObject json = new JSONObject(result);
					int status = json.optInt("status");

					if (status == 200 || status == 1) {
						userPtb.setPtbMoney(stringToFloat(json.optString("balance")));
						userPtb.setBindptbMoney(stringToFloat(json.optString("bind_balance")));
						noticeResult(Constant.PTB_MONEY_SUCCESS, userPtb);
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.PTB_MONEY_FAIL, msg);
					}

				} catch (JSONException e) {
					noticeResult(Constant.PTB_MONEY_FAIL, "数据解析异常");
					Logs.e("fun#get json e = " + e);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				Message fialmsg = new Message();
				fialmsg.what = Constant.PTB_MONEY_FAIL;
				if (null != mHandler) {
					mHandler.sendMessage(fialmsg);
				}
			}
		});
		

	}

	protected float stringToFloat(String optString) {
		if (TextUtils.isEmpty(optString)) {
			return 0;
		}
		float money;
		try {
			money = Float.parseFloat(optString);
		} catch (NumberFormatException e) {
			money = 0;
		}

		return money;
	}

	protected void noticeResult(int type, Object obj) {
		Message msg = new Message();
		msg.what = type;
		msg.obj = obj;

		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}

	}
}
