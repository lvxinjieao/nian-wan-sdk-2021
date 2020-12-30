package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.GameRecordListRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏充值记录
 *
 * @author Administrator
 */
public class GameRecordListProcess {

    private static final String TAG = "GameRecordListProcess";

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        Logs.e("游戏充值记录 发送参数map : " + map.toString());
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        Logs.i("游戏充值记录 URL : " + Constant.game_packages_code_url);
        new GameRecordListRequest(handler).post(Constant.game_packages_code_url, getParamStr());

    }
}
