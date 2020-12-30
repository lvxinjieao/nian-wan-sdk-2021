package com.mini.sdk.http.hfbpay;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

public class HFBPayRequest {

    private static final String TAG = "HFBPayRequest";

    Handler mHandler;

    public HFBPayRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.PTB_PAY_FAIL, "参数异常");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {

            @Override
            public void requestSuccess(String result) {
                try {
                    PaymentInfo _paymentInfo;
                    // {"status":1,"return_code":"success","return_msg":"\u4e0b\u5355\u6210\u529f","appid":"2594424001","out_trade_no":"SP_20160812185243vxhT"}

                    //String res = new String(Base64.decode(result), "utf-8");
                    //String res = new String(Base64Util.decode(result));
                    String res = new String(Base64.decode(result, Base64.DEFAULT));
                    JSONObject json = new JSONObject(res);

                    String status = json.optString("status");
                    String return_msg = json.optString("return_msg");

                    if ("1".equals(status)) {
                        String token_id = json.optString("token_id");
                        String out_trad_no = json.optString("out_trade_no");
                        String agent_id = json.optString("agent_id");
                        _paymentInfo = new PaymentInfo();
                        _paymentInfo.setAgentId(agent_id);
                        _paymentInfo.setBillNo(out_trad_no);
                        _paymentInfo.setTokenID(token_id);
                        noticeResult(Constant.HFB_PAY_REQUEST_SUCCESS, _paymentInfo);
                    } else {
                        _paymentInfo = new PaymentInfo();
                        _paymentInfo.setMessage(return_msg);
                        noticeResult(Constant.HFB_PAY_REQUEST_FAIL, _paymentInfo);
                    }
                } catch (JSONException e) {
                    PaymentInfo _paymentInfo = new PaymentInfo();
                    _paymentInfo.setMessage("参数解析异常");
                    noticeResult(Constant.HFB_PAY_REQUEST_FAIL, "解析参数异常");
                    Logs.e("fun#post JSONException:解析参数异常" + e);
                } catch (Exception e) {
                    PaymentInfo _paymentInfo = new PaymentInfo();
                    _paymentInfo.setMessage("其他异常");
                    noticeResult(Constant.HFB_PAY_REQUEST_FAIL, _paymentInfo);
                    Logs.e("fun#post Exception:异常" + e);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                String fun = " fun # onFailure ";
                PaymentInfo _paymentInfo = new PaymentInfo();
                _paymentInfo.setMessage(fun);
                noticeResult(Constant.HFB_PAY_REQUEST_FAIL, "网络异常");
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
