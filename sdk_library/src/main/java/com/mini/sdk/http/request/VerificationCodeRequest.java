package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.bean.VerifyCode;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VerificationCodeRequest {

    Handler mHandler;

    public VerificationCodeRequest(Handler mHandler) {

        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {

        if (TextUtils.isEmpty(url) || null == params) {
            noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, "参数错误");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                VerifyCode verifyCode = new VerifyCode();
                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");
                    if (status == 200 || status == 1) {
                        verifyCode.setCode("0");
                        noticeResult(Constant.VERIFYCODE_REQUEST_SUCCESS, verifyCode);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Logs.e("msg:" + msg);
                        noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, "解析验证码异常");
                    Logs.e("fun#post JSONException:" + e);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.VERIFYCODE_REQUEST_FAIL, "网络异常");
            }
        });
    }

    protected String getCodeByRes(String optString) {
        String code = "";
        JSONObject json;
        try {
            json = new JSONObject(optString);
            code = json.optString("code");
        } catch (JSONException e) {
            code = "";
            Logs.e("fun#getCodeByRes JSONException:" + e);
        }
        return code;
    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
