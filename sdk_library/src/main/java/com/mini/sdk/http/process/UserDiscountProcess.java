package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UserDiscountRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UserDiscountProcess {

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("promote_id", ChannelAndGame.getInstance().getChannelId());
		map.put("user_id", PluginApi.userLogin.getAccountId());
		map.put("game_id", ChannelAndGame.getInstance().getGameId());
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {

			UserDiscountRequest request = new UserDiscountRequest(handler);
			request.post(Constant.user_discount_url, getParamStr());

	}
}
