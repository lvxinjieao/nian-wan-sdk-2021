package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UpdateNikeNameRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateNikeNameProcess {

    private static final String TAG = "UpdateNikeNameProcess";

    private String nikeName;

    private String code;

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("nickname", nikeName);
        map.put("code", code);
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        Logs.e("fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {


        UpdateNikeNameRequest updateNickName = new UpdateNikeNameRequest(handler);
        updateNickName.post(Constant.update_user_info_url, getParamStr());

    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
