package com.mini.sdk.http.request;

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

public class LoginRequest {

    Handler handler;
    String username;
    String password;

    public LoginRequest(Handler handler, String username, String password) {

        if (null != handler) {
            this.handler = handler;
        }

        if (!TextUtils.isEmpty(username)) {
            this.username = username;
        } else {
            this.username = "";
        }

        if (!TextUtils.isEmpty(password)) {
            this.password = password;
        } else {
            this.password = "";
        }

    }

    public void post(String url, String params) {

        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e(" post url is null add params is null");
            noticeResult(Constant.LOGIN_FAIL, "参数为空");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {

            @Override
            public void requestSuccess(String result) {
                UserLogin loginSuccess = new UserLogin();
                loginSuccess.setAccount(username);
                loginSuccess.setPassword(password);

                int status;
                try {
                    JSONObject json = new JSONObject(result);
                    status = json.optInt("status");

                    if (status == 200 || status == 1) {
                        loginSuccess.setCode(1);
                        loginSuccess.setMessage("登录成功");
                        loginSuccess.setAccount(username);
                        loginSuccess.setAccountId(json.optString("user_id"));
                        loginSuccess.setToken(json.optString("token"));
                        noticeResult(Constant.LOGIN_SUCCESS, loginSuccess);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Logs.e("msg:" + msg);
                        noticeResult(Constant.LOGIN_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.LOGIN_FAIL, "解析数据异常");
                } catch (Exception e) {
                    noticeResult(Constant.LOGIN_FAIL, "解析数据异常");
                }
            }

            @Override
            public void requestFailure(IOException e) {
                noticeResult(Constant.LOGIN_FAIL, "网络异常");
            }
        });


    }

    private void noticeResult(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        if (null != handler) {
            handler.sendMessage(msg);
        }
    }
}
