package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.PTBRecordListRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class PTBRecordListProcess {

	private static final String TAG = "AddPTBRecordProcess";
 
	private int index;

	public void setIndex(int index) {
		this.index = index;
	}

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", ChannelAndGame.getInstance().getGameId());
		map.put("account", PluginApi.userLogin.getAccount());
		map.put("user_id", PluginApi.userLogin.getAccountId());
		map.put("index", index + "");
		Logs.e("fun#ptb_pay params:" + map.toString());
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {

			new PTBRecordListRequest(handler).post(Constant.add_ptb_record_url, getParamStr());

	}
}
