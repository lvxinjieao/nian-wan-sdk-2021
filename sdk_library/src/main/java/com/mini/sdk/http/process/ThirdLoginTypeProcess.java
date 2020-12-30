package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.ThirdLoginTypeRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class ThirdLoginTypeProcess {


    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        ThirdLoginTypeRequest request = new ThirdLoginTypeRequest(handler);
        request.post(Constant.third_login_type_url, getParamStr());

    }

}
