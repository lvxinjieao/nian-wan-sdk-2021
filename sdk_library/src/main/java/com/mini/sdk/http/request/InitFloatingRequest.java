package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.bean.InitBean;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

/**
 * 描述：初始化悬浮窗 作者：苏杭 时间: 2018-05-14 9:26
 */

public class InitFloatingRequest {
	private static final String TAG = "InitFloatingRequest";
	Handler mHandler;

	public InitFloatingRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.INIT_FLOATING_FAIL, "参数为空");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status = -1;
				InitBean initBean = new InitBean();
				try {
					JSONObject obj = new JSONObject(result);
					status = obj.getInt("status");
					if (status == 200 || status == 1) {
						initBean.setStatus(obj.optInt("ball_status"));
						initBean.setLogo(obj.optString("sites_ball_logo"));
						noticeResult(Constant.INIT_FLOATING_SUCCESS, initBean);
					} else {
						String msg;
						if (!TextUtils.isEmpty(obj.optString("return_msg"))) {
							msg = obj.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.INIT_FLOATING_FAIL, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					noticeResult(Constant.INIT_FLOATING_FAIL, "解析数据异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {
				noticeResult(Constant.REQUEST_ANTI_ADDICTION_FAIL, "网络异常");
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
