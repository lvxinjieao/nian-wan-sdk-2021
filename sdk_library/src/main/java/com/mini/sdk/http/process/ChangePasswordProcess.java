package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.ChangePasswordRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordProcess {

    private static final String TAG = "ChangePasswordProcess";

    private String code;
    private String pwd;
    private String repwd;

    /**
     * 修改密码请求需要的参数
     *
     * @return 返回一个String
     */
    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("password", repwd);
        map.put("code", code);
        map.put("old_password", pwd);
        return RequestParamUtil.getRequestParamString(map);
    }

    /**
     * 掉用此方法启动请求
     */
    public void post(Handler handler) {
        ChangePasswordRequest request = new ChangePasswordRequest(handler);
        request.post(Constant.update_user_info_url, getParamStr());
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setRepwd(String repwd) {
        this.repwd = repwd;
    }

}
