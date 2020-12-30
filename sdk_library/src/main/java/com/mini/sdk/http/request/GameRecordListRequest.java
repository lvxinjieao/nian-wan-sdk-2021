package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.GameRecordEntity;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GameRecordListRequest {

	private static final String TAG = "GameRecordListRequest";

	Handler mHandler;

	public GameRecordListRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {

		if (TextUtils.isEmpty(url) || null == params) {
			noticeResult(Constant.RECORD_LIST_FAIL, null, "参数异常");
			return;
		}

		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				GameRecordEntity gre = GameRecordEntity.getInstance();
				GameRecordEntity gameRecordEntity;
				int status;
				try {
					JSONObject json = new JSONObject(result);
					status = json.optInt("status");
					// {"status":1,"list":null}
					if (status == 200 || status == 1) {// 请求成功
						if (TextUtils.isEmpty(json.optString("list"))) {
							noticeResult(Constant.GAME_RECODE_FAIL, null, "没有记录");
						} else {
							JSONArray ja = json.getJSONArray("list");
							JSONObject recode;
							for (int i = 0; i < ja.length(); i++) {
								gameRecordEntity = new GameRecordEntity();
								recode = (JSONObject) ja.getJSONObject(i);
								recode = new JSONObject(recode.toString());
								String pay_time = recode.optString("pay_time");
								String pay_money = recode.optString("pay_amount");
								String pay_type = recode.optString("pay_way");
								String pay_status = recode.optString("pay_status");
								String game_name = recode.optString("props_name");
								gameRecordEntity.setPayName(game_name);
								gameRecordEntity.setPayMoney(pay_money);
								gameRecordEntity.setPayStatus(pay_status);
								gameRecordEntity.setPayTime(pay_time);
								gameRecordEntity.setPayType(pay_type);
								gre.getGameRecordes().add(gameRecordEntity);
							}
							noticeResult(Constant.GAME_RECODE_SUCCESS, gre, "");
						}
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.GAME_RECODE_FAIL, null, msg);
					}

				} catch (JSONException e) {
					noticeResult(Constant.GAME_RECODE_FAIL, null, "解析数据异常");
					Logs.e("fun#post  JSONException:" + e);
				} catch (Exception e) {
					noticeResult(Constant.GAME_RECODE_FAIL, null, "解析数据异常");
					Logs.e("fun#数据解析异常 = " + e);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.GAME_RECODE_FAIL, null, "网络异常");
			}
		});


	}

	private void noticeResult(int type, GameRecordEntity gameRecordEntity, String str) {
		Message msg = new Message();
		msg.what = type;
		if (Constant.GAME_RECODE_SUCCESS == type) {
			msg.obj = gameRecordEntity;
		} else {
			msg.obj = str;
		}
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
