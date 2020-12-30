package com.mini.sdk.http;

import android.util.Base64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.SignUtil;

/**
 * 将参数排序，加密
 */
public class RequestParamUtil {

    /**
     * 拼接请求参数 将参数排序，加密
     */
    public static String map2str(Map<String, String> paramMap) {

        if (null == paramMap || paramMap.size() < 1) {
            return "";
        }

        // 将map按照键值排序
        List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(paramMap.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });
        // 将map和验签的sign添加到请求参数里面
        JSONObject json = new JSONObject();
        String values = "";
        try {
            for (Map.Entry<String, String> mapping : mappingList) {
                String key = mapping.getKey().trim();
                String value = mapping.getValue();
                // 向JSONObject对象中添加请求参数
                json.put(key, value);
                // 拼接排序后的value值
                values += mapping.getValue();
            }
            // 生成验签字符串
            String md5key = SignUtil.md5(values.trim() + Constant.getInstance().getSignKey());
            // 将验签字符串添加到参数中
            json.put("md5_sign", md5key);
            String string = json.toString();
            // 对参数base64处理防止中文乱码
            // return Base64Util.encode(string);
            return new String(Base64.encode(string.getBytes(),Base64.DEFAULT));
        } catch (JSONException e) {
            return "";
        }
    }

}
