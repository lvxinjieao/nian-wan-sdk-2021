package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.RegisterRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterProcess {


    private String account;
    private String password;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void post(Handler handler) {
        if (null == handler) {
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("password", password);

        map.put("game_id", String.valueOf(Constant.getInstance().getGameId()));
        map.put("game_name", Constant.getInstance().getGameName());
        map.put("game_appid", Constant.getInstance().getGameAppId());

        map.put("promote_id", String.valueOf(ChannelAndGame.getInstance().getChannelId()));
        map.put("promote_account", ChannelAndGame.getInstance().getChannelAccount());


        map.put("imei", TextUtils.isEmpty(PluginApi.getImei()) ? "" : PluginApi.getImei());
        map.put("mac", TextUtils.isEmpty(PluginApi.getMac()) ? "" : PluginApi.getMac());
        map.put("android_id", TextUtils.isEmpty(PluginApi.getAndroidId()) ? "" : PluginApi.getAndroidId());
        map.put("oaid", TextUtils.isEmpty(PluginApi.getOaid()) ? "" : PluginApi.getOaid());

        String param = RequestParamUtil.getRequestParamString(map);
        RegisterRequest registerRequest = new RegisterRequest(handler, account, password);
        registerRequest.post(Constant.USER_REGISTER, param);
    }
}
