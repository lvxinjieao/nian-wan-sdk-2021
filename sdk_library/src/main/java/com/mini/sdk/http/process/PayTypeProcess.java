package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.PayTypeRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class PayTypeProcess {

	private String getParamStr() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", ChannelAndGame.getInstance().getGameId());
		return RequestParamUtil.getRequestParamString(map);
	}

	public void post(Handler handler) {

			PayTypeRequest request = new PayTypeRequest(handler);
			request.post(Constant.payType_list, getParamStr());

	}

}
