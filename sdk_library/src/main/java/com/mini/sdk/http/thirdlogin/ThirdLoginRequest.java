package com.mini.sdk.http.thirdlogin;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ThirdLoginRequest {

    Handler mHandler;
    public boolean isYKLogin;

    public void setYKLogin(boolean YKLogin) {
        this.isYKLogin = YKLogin;
    }

    public ThirdLoginRequest(Handler mHandler) {
        isYKLogin = false;
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {

        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            return;
        }


        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                Logs.i("游客登陆返回参数 ： "+result);
                UserLogin loginSuccess = new UserLogin();
                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");

                    if (status == 200 || status == 1) {
                        loginSuccess.setCode(1);
                        loginSuccess.setMessage("登录成功");

                        loginSuccess.setAccountId(json.optString("user_id"));
                        loginSuccess.setAccount(json.optString("account"));
                        loginSuccess.setToken(json.optString("token"));
                        loginSuccess.setPassword(json.optString("password"));

                        noticeResult(Constant.LOGIN_SUCCESS, loginSuccess);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        noticeResult(Constant.LOGIN_FAIL, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(IOException e) {
                noticeResult(Constant.LOGIN_FAIL, "登录失败");
            }
        });


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
