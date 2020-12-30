package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.WXOrderInfo;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WXOrderRequest {

    Handler mHandler;

    /**
     * 商品类型：标示微信支付
     */
    public String goodsType;

    public WXOrderRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("微信支付 post url is null add params is null");
            noticeResult(Constant.WFT_ORDERINFO_FAIL, "参数异常");
            return;
        }
        Logs.i("微信支付 post url = " + url);

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");
                    if (status == 200 || status == 1) {
                        WXOrderInfo wxOrderInfo = new WXOrderInfo();
                        if (json.optString("paytype").equals("wx")) {// 微信官方
                            wxOrderInfo.setTag("wx");
                            wxOrderInfo.setOrderNo(json.optString("orderno"));
                            wxOrderInfo.setUrl(json.optString("url"));
                            wxOrderInfo.setCal_url(json.optString("cal_url"));
                        }
                        noticeResult(Constant.WFT_ORDERINFO_SUCCESS, wxOrderInfo);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Logs.e("msg:" + msg);
                        noticeResult(Constant.WFT_ORDERINFO_FAIL, "服务器" + msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.WFT_ORDERINFO_FAIL, "数据解析异常");
                    Logs.e("fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.WFT_ORDERINFO_FAIL, "其它程序异常");
                    Logs.e("fun#post JSONException: " + e);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.WFT_ORDERINFO_FAIL, "网络异常");
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
