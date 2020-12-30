package com.mini.sdk.utils;

import android.util.Base64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestParamUtil {

    /**
     * json字符串Base64
     */
    public static String getRequestParamString(Map<String, String> param) {

        if (null == param || param.size() < 1) {
            return "";
        }

        param.put("sdk_version", "1");// 1 安卓 2苹果
        List<Map.Entry<String, String>> lists = new ArrayList<Map.Entry<String, String>>(param.entrySet());

        Collections.sort(lists, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });

        JSONObject json = new JSONObject();
        String values = "";

        try {

            for (Map.Entry<String, String> mapping : lists) {
                String key = mapping.getKey().trim();
                String value = mapping.getValue();
                json.put(key, value);
                values += mapping.getValue();
            }

            String md5_sign = SignUtil.md5(values.trim() + Constant.getInstance().getSignKey());
            Logs.i("md5_sign : " + md5_sign);

            json.put("md5_sign", md5_sign);
            String string = json.toString();
            Logs.i("请求参数 : " + string);
            //String encode = Base64Util.encode(string);
            String encode = new String(Base64.encode(string.getBytes(), Base64.DEFAULT));
            Logs.i("请求参数base64 : " + encode);
            return encode;
        } catch (JSONException e) {
        }
        return "";
    }
}
