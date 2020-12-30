package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.CertificateRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class CertificateProcess {

    private static final String TAG = "CertificateProcess";

    private String idcard;
    private String real_name;

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("fun#post handler is null");
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("idcard", idcard);
        map.put("real_name", real_name);
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());

        String param = RequestParamUtil.getRequestParamString(map);
        if (TextUtils.isEmpty(param)) {
            Logs.e("fun#post param is null");
            return;
        }

        CertificateRequest certificateRequest = new CertificateRequest(handler);
        certificateRequest.post(Constant.certificate, param);

    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
