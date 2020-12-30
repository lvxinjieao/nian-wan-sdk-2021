package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UserInfoRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UserInfoProcess {

    private String account = "";
    private String type;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setType(String type) {
        this.type = type;
    }




    private String getParams() {
        Map<String, String> map = new HashMap<String, String>();

        if ("0".equals(type)) {// 正常登陆
            map.put("user_id", PluginApi.userLogin.getAccountId());
        } else if ("1".equals(type)) {//忘记密码
            map.put("user_id", account);
        }

        String account2 = PluginApi.userLogin.getAccount();
        if (TextUtils.isEmpty(account2)) {
            map.put("account", "");
        } else {
            map.put("account", account2);
        }

        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("type", type);
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {
        new UserInfoRequest(handler).post(Constant.USER_INFO_URL, getParams());
    }


}
