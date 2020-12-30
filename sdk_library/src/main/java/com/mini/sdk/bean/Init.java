package com.mini.sdk.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.User;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;
import com.mini.sdk.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Init {

    private static Init instance;
    public Context context;

    public synchronized static Init getInstance() {
        if (null == instance) {
            instance = new Init();
        }
        return instance;
    }

    public void doInit(Context context, Handler handler) {
        this.context = context;
        if (!PluginApi.init)
            new InitTask().execute(handler);
    }

    class InitTask extends AsyncTask<Handler, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constant.getInstance().setGameId(getIntValue(context, "game_id"));
            Constant.getInstance().setGameName(getStringValue(context, "game_name"));
            Constant.getInstance().setGameAppId(getStringValue(context, "game_appid"));
            Constant.getInstance().setSignKey(getStringValue(context, "sign_key"));
            ChannelAndGame.getInstance();
        }


        @Override
        protected Void doInBackground(final Handler... handler) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("game_id", ChannelAndGame.getInstance().getGameId());
            map.put("promote_id", String.valueOf(ChannelAndGame.getInstance().getChannelId()));

            String param = RequestParamUtil.getRequestParamString(map);

            HttpUtils.getInstance().httpPost(Constant.IS_QUICK_LOGIN, param, new HttpCallback() {
                @Override
                public void requestSuccess(String result) {
                    Logs.i("快捷登陆 : " + result);
                    int status = -1;
                    int data = 0;
                    try {
                        JSONObject json = new JSONObject(result);
                        status = json.optInt("status");
                        data = json.optInt("data");
                        if (status == 200 && data == 1) {
                            PluginApi.visitor = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    LinkedList<User> users = SharedPreferenceUtil.getUsers(context);
                    if (users != null) {
                        User user = users.getFirst();
                        String account = user.getAccount();
                        String password = user.getPassword();
                        PluginApi.userLogin.setAccount(TextUtils.isEmpty(account) ? "" : account);
                        PluginApi.userLogin.setPassword(TextUtils.isEmpty(password) ? "" : password);
                    }
                    handler[0].sendEmptyMessage(0);
                }

                @Override
                public void requestFailure(IOException e) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private String getStringValue(Context context, String key) {
        String value = "";
        try {
            value = getAppInfo(context).metaData.getString(key, "0");
        } catch (Exception e) {
            value = "";
        }
        Logs.i(key + " : " + value);
        return value;
    }

    private Integer getIntValue(Context context, String key) {
        Integer value;
        try {
            value = getAppInfo(context).metaData.getInt(key, 0);
        } catch (Exception e) {
            value = -1;
        }
        Logs.i(key + " : " + value);
        return value;
    }

    private ApplicationInfo getAppInfo(Context context) {
        if (null == context) {
            return null;
        }
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
