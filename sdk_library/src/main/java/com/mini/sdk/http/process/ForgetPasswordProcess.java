package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.http.request.ForgmentPasswordRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordProcess {

    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证码
     */
    private String code;
    /**
     * 新密码
     */
    private String password;
    private String id;
    private int codeType; // 1手机短信


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCodeType(int code_type) {
        this.codeType = code_type;
    }

    private String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", id);
        map.put("game_id", String.valueOf(Constant.getInstance().getGameId()));
        map.put("password", password);
        map.put("code", code);
        map.put("phone", phone);
        map.put("code_type", codeType + "");
        return RequestParamUtil.getRequestParamString(map);
    }

    public void post(Handler handler) {
        ForgmentPasswordRequest request = new ForgmentPasswordRequest(handler);
        request.post(Constant.FORGET_PASSWORD, getParamStr());
    }


}
