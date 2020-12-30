package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

import com.mini.sdk.entity.AlipayVerifyResult;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.SignUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AlipayOrderRequest {

    public Handler handler;

    public AlipayOrderRequest(Handler handler) {
        if (null != handler) {
            this.handler = handler;
        }
    }

    public void post(String url, String params) {
        Logs.i("支付宝 post url = " + url);

        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("支付宝 post url is null or params is null");
            noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, "客户端的支付宝请求参数为空");
            return;
        }


        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {

                AlipayVerifyResult zfbPayResult = new AlipayVerifyResult();

                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");

                    if (status == 1 || status == 200) {

                        String orderInfo = json.optString("orderInfo");
                        Logs.i(">>>>>>>>>>>>>  orderInfo : " + orderInfo);

                        String md5_sign = json.optString("md5_sign");
                        Logs.i(">>>>>>>>>>>>>>  md5_sign : " + md5_sign);

                        String order_sign = json.optString("order_sign");
                        Logs.i(">>>>>>>>>>>>> order_sign : " + order_sign);

                        String out_trade_no = json.optString("out_trade_no");
                        Logs.i(">>>>>>>>>>  out_trade_no : " + out_trade_no);

                        zfbPayResult.setOrderInfo(orderInfo);
                        zfbPayResult.setZfbMd5Key(md5_sign);
                        zfbPayResult.setSign(order_sign);
                        zfbPayResult.setOrderNumber(out_trade_no);

                        String temp_md5 = SignUtil.md5(orderInfo + Constant.getInstance().getSignKey());
                        Logs.i("---------temp_md5 = " + temp_md5);
                        Logs.i("---------md5_sign = " + md5_sign);

                        if (temp_md5.equals(md5_sign)) {
//                            String order = Base64.decode(orderInfo.getBytes(),Base64.DEFAULT);
                            byte[] decode = Base64.decode(orderInfo.getBytes(), Base64.DEFAULT);

                            String order = "";
                            try {
                                order = new String(decode);
                            } catch (UnsupportedOperationException e) {
                                Logs.e("decode:" + e);
                                order = "";
                            }
                            Logs.i("---------order = " + order);

                            zfbPayResult.setOrderInfo(order);
                            noticeResult(Constant.ZFB_PAY_VALIDATE_SUCCESS, zfbPayResult);// lvxinmin zhifubao
                        } else {
                            noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, "订单验证失败");
                        }

                    } else {
                        noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, json.opt("return_msg"));
                        return;
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, "服务器异常");
                    Logs.e("fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, "服务器异常：" + e);
                }

            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.ZFB_PAY_VALIDATE_FAIL, "网络异常");
            }

        });


    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != handler) {
            handler.sendMessage(msg);
        }
    }
}
