package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.entity.AntiAddiction;
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
 * Created by Administrator on 2017/5/4.
 */

public class AntiAddictionRequest {
	private static final String TAG = "AntiAddictionRequest";

	Handler mHandler;

	public AntiAddictionRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {

		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null add params is null");
			noticeResult(Constant.REQUEST_ANTI_ADDICTION_FAIL, "参数为空");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				int status = -1;
				AntiAddiction antiAddiction = new AntiAddiction();
				try {
					JSONObject obj = new JSONObject(result);
					status = obj.getInt("status");
//                    antiAddiction.setStatus(obj.getInt("status"));
					if (status == 200 || status == 1) {
						JSONObject obj2 = obj.getJSONObject("data");
						antiAddiction.setHours(obj2.getString("hours"));
						antiAddiction.setContents_off(obj2.getString("contents_off"));
						antiAddiction.setHours_off_one(obj2.getString("hours_off_one"));
						antiAddiction.setHours_off_two(obj2.getString("hours_off_two"));
						antiAddiction.setContents_one(obj2.getString("contents_one"));
						antiAddiction.setContents_two(obj2.getString("contents_two"));
						antiAddiction.setBat(obj2.getString("bat"));
						antiAddiction.setHours_cover(obj2.getString("hours_cover"));
						antiAddiction.setName(obj2.getString("name"));
						if (TextUtils.isEmpty(obj2.getString("on-off"))) {
							// 实名认证开关0是1否
							antiAddiction.setOnoff("1");
						} else {
							antiAddiction.setOnoff(obj2.getString("on-off"));
						}
						antiAddiction.setAge_status(obj2.getInt("age_status"));
						antiAddiction.setPlay_time(obj2.getInt("play_time"));
						antiAddiction.setDown_time(obj2.getInt("down_time"));
						noticeResult(Constant.REQUEST_ANTI_ADDICTION_SUCCESS, antiAddiction);
					} else {
						String msg;
						if (!TextUtils.isEmpty(obj.optString("return_msg"))) {
							msg = obj.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.REQUEST_ANTI_ADDICTION_FAIL, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					noticeResult(Constant.REQUEST_ANTI_ADDICTION_FAIL, "解析数据异常");
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure:" + e);
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
