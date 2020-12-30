package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class ThirdLoginTypeRequest {

    private static final String TAG = "ThirdLoginTypeRequest";

    Handler mHandler;

    public ThirdLoginTypeRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, "参数异常");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                String config = "";
                try {
                    JSONObject json = new JSONObject(result);
                    config = json.optString("config");
                } catch (JSONException e) {
                    config = "";
                }

                if (!TextUtils.isEmpty(config)) {
                    noticeResult(Constant.THIRD_LOGIN_TYPE_SUCCESS, config);
                } else {
                    noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, "未开启第三方登录");
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.THIRD_LOGIN_TYPE_FAIL, "网络异常");
            }
        });
    }

    private void noticeResult(int type, String str) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = str;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
