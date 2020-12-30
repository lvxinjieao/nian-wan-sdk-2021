package com.mini.sdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.entity.UserDiscountEntity;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by zhujinzhujin
 * on 2017/1/12.
 */

public class UserDiscountRequest {

    Handler mHandler;

    public UserDiscountRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "参数为空");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                if (TextUtils.isEmpty(result)) {
                    noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "参数为空");
                } else {
                    UserDiscountEntity userDiscount = new UserDiscountEntity();
                    try {
                        JSONObject json = new JSONObject(result);
                        userDiscount.setDiscountType(json.optInt("discount_type", 0));
                        userDiscount.setDiscountNum(json.optString("discount", "10"));
                    } catch (JSONException e) {
                        Logs.e("JSONException:" + e.toString());
                    }
                    noticeResult(Constant.GET_USER_DISCOUNT_SUCCESS, userDiscount);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                noticeResult(Constant.GET_USER_DISCOUNT_FAIL, "网络异常");
            }
        });



    }


    private void noticeResult(int type, Object object) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = object;

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
