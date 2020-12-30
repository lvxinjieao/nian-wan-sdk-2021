package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UserPtbRemainRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户平台币
 */
public class UserCoinProcess {


    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        new UserPtbRemainRequest(handler).post(Constant.query_user_ptb_url, getParamStr());

    }

}
