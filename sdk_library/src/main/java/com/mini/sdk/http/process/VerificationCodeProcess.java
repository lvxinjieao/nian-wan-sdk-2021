package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.VerificationCodeRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class VerificationCodeProcess {

    private String phone;
    private String type = ""; // 发送验证码时后端用于判断此手机号是否已经被绑定过

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String getParamStr() {

        Map<String, String> map = new HashMap<String, String>();
        if (!TextUtils.isEmpty(type)) {
            map.put("type", "1");
        }
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("phone", phone);
        Logs.e(" params:" + map.toString());
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {

        new VerificationCodeRequest(handler).post(Constant.verify_Phone_Code, getParamStr());

    }

}
