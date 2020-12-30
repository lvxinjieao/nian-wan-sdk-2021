package com.mini.sdk.http.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.entity.PackCodeEntity;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

public class PacksCodeRequest {

    private static final String TAG = "PacksCodeRequest";

    Handler mHandler;

    public PacksCodeRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params, final String packName) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.PACKS_CODE_FAIL, "参数异常");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                PackCodeEntity packCode = new PackCodeEntity();
                packCode.setPackName(packName);
                int status;
                try {
                    JSONObject json = new JSONObject(result);
                    Logs.e("onSuccess retrn_msg=" + json.optString("return_msg"));
                    status = json.optInt("status");
                    if (status == -1) {// uc用户不支持礼包码
                        noticeResult(Constant.PACKS_CODE_FAIL, json.opt("return_msg"));
                        return;
                    }
                    if (status == 200 || status == 1) {
                        packCode.setReceiveStatus(json.optString("receive_status"));
                        packCode.setNovice(json.optString("novice"));

                        noticeResult(Constant.PACKS_CODE_SUCCESS, packCode);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                            msg = json.optString("return_msg");
                        } else {
                            msg = CodeUtils.getErrorMsg(status);
                        }
                        Logs.e("msg:" + msg);
                        noticeResult(Constant.PACKS_CODE_FAIL, msg);
                    }

                } catch (JSONException e) {
                    noticeResult(Constant.PACKS_CODE_FAIL, "解析参数异常");
                    Logs.e("fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.PACKS_CODE_FAIL, "网络异常");
                    Logs.e("fun#post JSONException:" + e);
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.PACKS_CODE_FAIL, "网络异常");
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
