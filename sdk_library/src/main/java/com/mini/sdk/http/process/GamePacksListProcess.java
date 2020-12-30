package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.GamePacksListRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class GamePacksListProcess {

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());

        Logs.e("礼包 params:" + map.toString());
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        new GamePacksListRequest(handler).post(Constant.game_packages_list, getParamStr());

    }

}
