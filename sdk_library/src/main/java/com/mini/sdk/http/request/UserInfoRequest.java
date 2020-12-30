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

public class UserInfoRequest {

    Handler handler;

    public UserInfoRequest(Handler handler) {
        if (null != handler) {
            this.handler = handler;
        }
    }

    public void post(String url, String params) {

        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("查询用户信息 url is null add params is null");
            noticeResult(Constant.GET_USER_INFO_FAIL, null, "参数为空");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {

            @Override
            public void requestSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");

                    if (status == 200 || status == 1) {

                        String gamename = json.optString("game_name", "");
                        if (TextUtils.isEmpty(gamename) || "null".equals(gamename)) {
                            gamename = "";
                        }

                        String phone = json.optString("phone", "").trim();
                        if (TextUtils.isEmpty(phone) || "null".equals(phone)) {
                            phone = "";
                        }

                        float bindPtb = stringToFloat(json.optString("bind_balance", ""));//绑定平台币
                        float ptb = stringToFloat(json.optString("balance", ""));//平台币

                        UserLogin info = new UserLogin();
                        info.setAccount(json.optString("account", ""));
                        info.setNikeName(json.optString("nickname", ""));
                        info.setAge_status(json.optString("age_status", "0"));
                        info.setReal_name(json.optString("real_name", ""));
                        info.setIdcard(json.optString("idcard", ""));
                        info.setUserRegisteType(json.optInt("register_type", -1));

                        info.setPlatformMoney(ptb);
                        info.setBindPtbMoney(bindPtb);
                        info.setPhoneNumber(phone);
                        info.setId(json.optString("id", ""));
                        noticeResult(Constant.GET_USER_INFO_SUCCESS, info, "");
                    } else {
                        String msg = CodeUtils.getErrorMsg(status);
                        Logs.e("msg:" + msg);
                        if (TextUtils.isEmpty(msg)) {
                            msg = "服务器异常";
                        }
                        noticeResult(Constant.GET_USER_INFO_FAIL, null, msg);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.GET_USER_INFO_FAIL, null, "数据解析异常");
                    Logs.e("fun#get json e = " + e);
                } catch (Exception e) {
                    noticeResult(Constant.GET_USER_INFO_FAIL, null, "数据解析异常");
                    Logs.e("fun#get json e = " + e);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.GET_USER_INFO_FAIL, null, "网络异常");
            }
        });


    }

    protected float stringToFloat(String optString) {
        if (TextUtils.isEmpty(optString)) {
            return 0;
        }
        float money;
        try {
            money = Float.parseFloat(optString);
        } catch (NumberFormatException e) {
            money = 0;
        } catch (Exception e) {
            money = 0;
        }

        return money;
    }

    private void noticeResult(int what, UserLogin info, String res) {
        Message msg = new Message();
        msg.what = what;
        if (what == Constant.GET_USER_INFO_FAIL) {
            msg.obj = res;
        } else {
            msg.obj = info;
        }

        if (null != handler) {
            handler.sendMessage(msg);
        }
    }
}
