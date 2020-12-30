package com.mini.sdk.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private final static String PARAMETER_SEPARATOR = "&";
    private final static String NAME_VALUE_SEPARATOR = "=";

    private static HttpUtils httpUtils;
    private static OkHttpClient client;
    private static Handler handler;

    public HttpUtils() {
        client = new OkHttpClient();
        /**
         * 在这里直接设置连接超时.读取超时，写入超时
         */
        OkHttpClient.Builder builder = client.newBuilder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式 获取OkHttp实例
     */
    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    /**
     * @param url
     * @param json
     * @param httpCallback
     */
    public void httpPost(String url, String json, final HttpCallback httpCallback) {
        Logs.i("请求URL："+url);
        Logs.i("请求参数："+json);
        client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        //String decode = Base64Util.decode(result);
                       String decode =new String(Base64.decode(result,Base64.DEFAULT));
                        Logs.i("响应："+decode);
                        httpCallback.requestSuccess(decode);
                    }
                });
                response.body().close();
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        httpCallback.requestFailure(e);
                    }
                });
            }
        });

    }

    public void post(String url, String json) {
        Logs.i("请求URL："+url);
        Logs.i("请求参数："+json);
        client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

            @Override
            public void onFailure(Call call, final IOException e) {

            }
        });
    }

    /**
     * @param url
     * @param params
     * @param callback
     */
    public void httpPost(String url, HashMap<String, String> params, final HttpCallback callback) {
        if (params == null)
            throw new NullPointerException("params is null");

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = (String) params.get(key);
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder().post(formBody).url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.requestSuccess(result);
                    }
                });
                response.body().close();
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.requestFailure(e);
                    }
                });
            }
        });

    }

    /**
     * @param url
     * @param map
     * @param httpCallback
     * @throws IOException
     */
    public void httpGet(String url, Map<String, Object> map, final HttpCallback httpCallback) throws IOException {
        client = new OkHttpClient();

        String params = "";
        if (params != null) {
            params = urlParamsFormat(map, "UTF-8");
        }
        String Url = url + "?" + params;

        System.out.println("URL : " + Url);

        Request request = new Request.Builder().url(Url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallback.requestSuccess(result);
                    }
                });
                response.body().close();
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallback.requestFailure(e);
                    }
                });
            }
        });
    }

    public static String urlParamsFormat(Map<String, Object> map, String charset) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            final String encodedName = URLEncoder.encode(key, charset);
            final String encodedValue = URLEncoder.encode(map.get(key).toString(), charset);
            if (sb.length() > 0) {
                sb.append(PARAMETER_SEPARATOR);
            }
            sb.append(encodedName).append(NAME_VALUE_SEPARATOR).append(encodedValue);
        }
        return sb.toString();
    }

}
