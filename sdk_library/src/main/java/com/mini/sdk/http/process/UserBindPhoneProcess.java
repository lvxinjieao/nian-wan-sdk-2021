package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UserBindPhoneRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UserBindPhoneProcess {

	private static final String TAG = "UserBindPhoneProcess";

	private String code;
	private String phone;
	private String smcode;

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", PluginApi.userLogin.getAccountId());
		map.put("game_id", ChannelAndGame.getInstance().getGameId());
		map.put("phone", phone);
		map.put("code", code);
		map.put("sms_code", smcode);
		Logs.e("fun#ptb_pay params:" + map.toString());

		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {

			UserBindPhoneRequest request = new UserBindPhoneRequest(handler);
			request.post(Constant.update_user_info_url, getParamStr());

	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSmcode(String smcode) {
		this.smcode = smcode;
	}

}
