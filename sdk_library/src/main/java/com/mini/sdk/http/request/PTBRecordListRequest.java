package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.AddPtbEntity;
import com.mini.sdk.entity.AddPtbRecordEntity;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PTBRecordListRequest {

	private static final String TAG = "PTBRecordListRequest";

	Handler mHandler;

	public PTBRecordListRequest(Handler mHandler) {
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, String params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Logs.e("fun#post url is null");
			noticeResult(Constant.RECORD_LIST_FAIL, null, "参数异常");
			return;
		}
		HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
			@Override
			public void requestSuccess(String result) {
				AddPtbRecordEntity addPtbRecordEntity = new AddPtbRecordEntity();
				int status;
				try {
					JSONObject json = new JSONObject(result);
					status = json.optInt("status");

//					addPtbRecordEntity.setStatus(status);
					if (status == 200 || status == 1) {// 支付宝充平台币成功
						addPtbRecordEntity.setTotal(json.optString("total", "*"));
						addPtbRecordEntity.setAddPtbList(getGamePackList(json));
						noticeResult(Constant.RECORD_LIST_SUCCESS, addPtbRecordEntity, "");
					} else {
						String msg;
						if (!TextUtils.isEmpty(json.optString("return_msg"))) {
							msg = json.optString("return_msg");
						} else {
							msg = CodeUtils.getErrorMsg(status);
						}
						Logs.e("msg:" + msg);
						noticeResult(Constant.RECORD_LIST_FAIL, null, msg);
					}
				} catch (JSONException e) {
					noticeResult(Constant.RECORD_LIST_FAIL, null, "解析数据异常");
					Logs.e("fun#post  JSONException:" + e);
				} catch (Exception e) {
					noticeResult(Constant.LOGIN_FAIL, null, "解析数据异常");
					Logs.e("fun#数据解析异常 = " + e);
				}
			}

			@Override
			public void requestFailure(IOException e) {
				Logs.e("onFailure" + e);
				noticeResult(Constant.RECORD_LIST_FAIL, null, "网络异常");
			}
		});


	}

	/**
	 * 0:平台币,1:支付宝,2:微信(扫码)3微信app 4 威富通 5聚宝云
	 */
	private List<AddPtbEntity> getGamePackList(JSONObject json) {
		if (null == json) {
			return null;
		}
		List<AddPtbEntity> addPtbList = new ArrayList<AddPtbEntity>();
		try {
			JSONArray jsonArray = json.getJSONArray("data");
			AddPtbEntity addPtb;
			JSONObject jsonPack;
			for (int i = 0; i < jsonArray.length(); i++) {// 遍历JSONArray
				addPtb = new AddPtbEntity();
				jsonPack = jsonArray.getJSONObject(i);
				addPtb.setBlannce(jsonPack.optString("pay_amount", ""));
				addPtb.setAddPtbTime(jsonPack.optString("create_time", ""));
				String payType = jsonPack.optString("pay_way", "");
				String payStr = "未知";
				if ("0".equals(payType)) {
					payStr = "平台币";
				} else if ("1".equals(payType)) {
					payStr = "支付宝";
				} else if ("2".equals(payType)) {
					payStr = "微信(扫码)";
				} else if ("3".equals(payType)) {
					payStr = "微信";
				} else if ("4".equals(payType)) {
					payStr = "威富通";
				} else if ("5".equals(payType)) {
					payStr = "聚宝云";
				}
				addPtb.setBuyPTBType(payStr);
				addPtbList.add(addPtb);
//                if("1".equals(jsonPack.optString("pay_status", ""))){//如过支付成功，显示在充值记录界面上
//					addPtbList.add(addPtb);
//                }
			}
		} catch (JSONException e) {
			addPtbList = null;
			Logs.e("fun#getGamePackList  JSONException:" + e);
		}

		return addPtbList;
	}

	private void noticeResult(int type, AddPtbRecordEntity addPtbRecordEntity, String str) {
		Message msg = new Message();
		msg.what = type;
		if (Constant.RECORD_LIST_SUCCESS == type) {
			msg.obj = addPtbRecordEntity;
		} else {
			msg.obj = str;
		}
		if (null != mHandler) {
			mHandler.sendMessage(msg);
		}
	}
}
