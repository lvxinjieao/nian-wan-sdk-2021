package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.entity.GamePayTypeEntity;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by zhujinzhujin on 2017/1/11.
 */

public class PayTypeRequest {

    private static final String TAG = "PayTypeRequest";

    Handler mHandler;

    public PayTypeRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.GAME_PAY_TYPE_FAIL, "参数异常");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                int status;
                String msg = "";

                Logs.w("可选支付方式:" + result);

                String isHaveZFB = "0", isHaveWX = "0", isHaveJBY = "0", isHaveHFB = "0", isHaveJFT = "0";

                try {
                    JSONObject json = new JSONObject(result);
                    status = json.optInt("status");
                    if (status == 200 || status == 1) {
                        isHaveZFB = json.optString("zfb_game", "0");
                        isHaveWX = json.optString("wx_game", "0");
                        isHaveJBY = json.optString("jby_game", "0");
                        isHaveHFB = json.optString("hfb_game", "0");
                        isHaveJFT = json.optString("jft_game", "0");
                    } else {
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Logs.e("msg:" + msg);
                    }
                } catch (JSONException e) {
                    status = -1;
                } catch (Exception e) {
                    status = -1;
                }

                if (status == 200 || status == 1) {
                    GamePayTypeEntity gamePayTypeEntity = new GamePayTypeEntity();
                    gamePayTypeEntity.setHaveZFB("1".equals(isHaveZFB));
                    gamePayTypeEntity.setHaveWX("1".equals(isHaveWX));
                    gamePayTypeEntity.setHaveJBY("1".equals(isHaveJBY));
                    gamePayTypeEntity.setHaveHFB("1".equals(isHaveHFB));
                    gamePayTypeEntity.setHaveJFT("1".equals(isHaveJFT));

                    noticeResult(Constant.GAME_PAY_TYPE_SUCCESS, gamePayTypeEntity);
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        msg = "服务器异常";
                    }
                    noticeResult(Constant.GAME_PAY_TYPE_FAIL, msg);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.GAME_PAY_TYPE_FAIL, "网络异常");
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
