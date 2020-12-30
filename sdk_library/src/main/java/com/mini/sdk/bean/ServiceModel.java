package com.mini.sdk.bean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.mini.sdk.http.RequestParamUtil;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServiceModel {

    private static ServiceModel instance;

    public static ServiceModel getInstance() {
        if (instance == null) {
            instance = new ServiceModel();
        }
        return instance;
    }

    public void contactService(final Context context, final boolean flag) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("promote_id", ChannelAndGame.getInstance().getChannelId());
        map.put("sdk_version", "0");
        String param = RequestParamUtil.map2str(map);


        com.mini.sdk.utils.HttpUtils.getInstance().httpPost(Constant.service_url, param, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                int status = -1;
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    status = obj.getInt("status");
                    if (status == 200 || status == 1) {
                        String qq = obj.optString("ccustom_service_qq");
                        if (TextUtils.isEmpty(qq)) {
                            Toast.makeText(context, "未设置QQ号!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            String urlQQ = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ));
                            if (flag) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // 如果没有装ＱＱ，检测异常，不然会出问题滴！
                            Toast.makeText(context, "您还未安装QQ!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(obj.optString("return_msg"))) {
                            msg = obj.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure:" + e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
