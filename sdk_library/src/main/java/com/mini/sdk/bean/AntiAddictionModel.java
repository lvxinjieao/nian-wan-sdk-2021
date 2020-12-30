package com.mini.sdk.bean;

import android.os.Handler;
import android.os.Message;

import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.AntiAddiction;
import com.mini.sdk.http.process.AntiAddictionProgress;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.MyTimeUtil;


/**
 * Created by Administrator on 2017/5/5.
 * 防沉迷
 */
public class AntiAddictionModel {

    private static MyTimeUtil myTimeUtil;

    private static AntiAddictionModel instance;

    public AntiAddictionModel() {
    }

    public static AntiAddictionModel getInstance() {
        if (instance == null) {
            instance = new AntiAddictionModel();
        }
        return instance;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 请求防沉迷信息handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.REQUEST_ANTI_ADDICTION_SUCCESS:
                    Logs.i("请求防沉迷信息成功 ");
                    //请求防沉迷信息成功
                    AntiAddiction antiAddiction = (AntiAddiction) msg.obj;
                    if (antiAddiction == null) {
                        Logs.e("msg.obj is null");
                        return;
                    }
                    if (antiAddiction.getOnoff().equals("1")) {
                        Logs.w( "防沉迷开关关闭");
                        return;
                    }
                    if (antiAddiction.getOnoff().equals("0")) {
                        Logs.w( "防沉迷开关打开");
                        if (antiAddiction.getAge_status()!=2) {

                            if (myTimeUtil == null) {
                                myTimeUtil= new MyTimeUtil(PluginApi.activity, antiAddiction);
                                myTimeUtil.startAntiAddiction(antiAddiction);
                            }
                            else {
                                myTimeUtil.startAntiAddiction(antiAddiction);
                            }
                        }
                    }

                    break;

                case Constant.REQUEST_ANTI_ADDICTION_FAIL:
                    //请求防沉迷信息失败
                    Logs.e("请求防沉迷信息失败");
                    break;
            }
        }
    };



    /**
     * 请求防沉迷信息
     */
    public void requestAntiAddictionInfo() {
        AntiAddictionProgress antiAddictionProgress = new AntiAddictionProgress();
        antiAddictionProgress.post(mHandler);
    }


}
