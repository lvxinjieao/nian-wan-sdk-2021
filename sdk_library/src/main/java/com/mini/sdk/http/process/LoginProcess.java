package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.LoginRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginProcess {

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
            Logs.e("login post handler is null");
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("password", password);
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("game_appid", ChannelAndGame.getInstance().getGameAppId());

        String param = RequestParamUtil.getRequestParamString(map);

        if (TextUtils.isEmpty(param)) {
            Logs.e("login post param is null");
            return;
        }


        LoginRequest loginRequest = new LoginRequest(handler, account, password);
        loginRequest.post(Constant.USER_LOGIN, param);

    }

}
