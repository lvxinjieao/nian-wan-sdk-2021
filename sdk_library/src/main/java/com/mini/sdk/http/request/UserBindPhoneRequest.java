package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class UserBindPhoneRequest {

    private static final String TAG = "UserBindPhoneRequest";
    Handler mHandler;

    public UserBindPhoneRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.USER_BIND_PHONE_FAIL, "参数异常");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                int status = -1;
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    status = json.optInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 200 || status == 1) {
                    noticeResult(Constant.USER_BIND_PHONE_SUCCESS, "");
                } else {
                    String msg;
                    if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                        msg = json.optString("return_msg");
                    } else {
                        msg = CodeUtils.getErrorMsg(status);
                    }
                    Logs.e("msg:" + msg);
                    noticeResult(Constant.USER_BIND_PHONE_FAIL, msg);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.USER_BIND_PHONE_FAIL, "网络异常");
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
