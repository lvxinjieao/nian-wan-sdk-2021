package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UpdateVisitorInfoRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujinzhujin on 2017/1/17.
 */

public class UpdateVisitorInfoProcess {

    private static final String TAG = "UpdateVisitorInfoProcess";

    private String account;
    private String pwd;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("password", pwd);
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("code", "account");
        map.put("user_id", PluginApi.userLogin.getAccountId());
        Logs.w("fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {
        UpdateVisitorInfoRequest request = new UpdateVisitorInfoRequest(handler);
        request.post(Constant.update_user_info_url, getParamStr());
    }

}
