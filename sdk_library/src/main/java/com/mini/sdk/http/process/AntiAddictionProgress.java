package com.mini.sdk.http.process;

import android.os.Handler;


import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.RequestParamUtil;
import com.mini.sdk.http.request.AntiAddictionRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/4.
 * 防沉迷
 */
public class AntiAddictionProgress {
    private static final String TAG = "AntiAddictionProgress";

    private String account = PluginApi.userLogin.getAccount();
    private String game_id = ChannelAndGame.getInstance().getGameId() + "";
    private String promote_id = ChannelAndGame.getInstance().getChannelId() + "";

    private String sdk_version = "0";

//    private String serverName = PluginApi.getInstance()getServerName();
//    private String roleName = PluginApi.getInstance().getRoleName();

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("fun#post handler is null");
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("game_id", game_id);
        map.put("promote_id", promote_id);
        map.put("sdk_version", sdk_version);

//        if (!TextUtils.isEmpty(serverName) && !TextUtils.isEmpty(roleName)) {
//            map.put("server_name", serverName);
//            map.put("game_player_name", roleName);
//        }
        String param = RequestParamUtil.map2str(map);

        AntiAddictionRequest antiAddictionRequest = new AntiAddictionRequest(handler);
        antiAddictionRequest.post(Constant.ANTIADDICTION_URL, param);
    }

}
