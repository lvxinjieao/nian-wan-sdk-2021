package com.mini.sdk.http.thirdlogin;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;


public class ThirdLoginProcess {

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("game_appid", ChannelAndGame.getInstance().getGameAppId());
        map.put("promote_id", ChannelAndGame.getInstance().getChannelId());
        map.put("promote_account", ChannelAndGame.getInstance().getChannelAccount());
        map.put("login_type", "yk");

//        String device_id = FileUtil.getFileContent(new File("/sdcard/min/android.txt"));
//        Logs.i("游客登录读取 device_id : " + device_id);
//        map.put("device_id", TextUtils.isEmpty(device_id) ? "0" : device_id);

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {
        ThirdLoginRequest request = new ThirdLoginRequest(handler);
        request.setYKLogin(true);
        request.post(Constant.VISITOR_LOGIN, getParamStr());
    }
}
