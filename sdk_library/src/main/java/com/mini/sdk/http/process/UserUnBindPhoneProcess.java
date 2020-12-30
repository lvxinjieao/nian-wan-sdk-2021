package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UserUnBindPhoneRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UserUnBindPhoneProcess {

    private String code;
    private String phone;
    private String smcode;
    private String account;

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("phone", PluginApi.userLogin.getPhoneNumber());
        map.put("code", code);
        map.put("user_id", PluginApi.userLogin.getAccountId());
        Logs.e("解绑手机 params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        UserUnBindPhoneRequest request = new UserUnBindPhoneRequest(handler);
        request.post(Constant.user_unbind_phone_url, getParamStr());

    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSmcode(String smcode) {
        this.smcode = smcode;
    }

}
