package com.mini.sdk.bean;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.mini.sdk.PluginApi;
import com.mini.sdk.http.process.OffLineProgress;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

/**
 * Created by Administrator on 2017/5/5.
 * 下线通知
 */

public class OffLineAnnounceModel {

    public static final String TAG = "OffLineAnnounceModel";
    private Context context;

    public OffLineAnnounceModel(Context context) {
        this.context = context;
    }

    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Constant.OFFLINE_SUCCESS:
                    closeAntiAddiction();
//                    PersonalCenterModel.getInstance().channelAndGame=new ChannelAndGameinfo();
                    PluginApi.isStart=false;
//                    FlagControl.isLogin=false;
                    Logs.w("请求下线成功");
                    break;
                case Constant.OFFLINE_FAIL:
                    leaveGameTime= System.currentTimeMillis();
                    Logs.e("请求下线失败");
                    break;
            }
        }
    };

    //离开游戏时间
    public static long leaveGameTime=0;
    /**
     * 关闭防沉迷
     */
    public  void closeAntiAddiction() {

//        leaveGameTime=System.currentTimeMillis();

        Intent intent=new Intent();
        intent.setAction("StopTiming");
        context.sendBroadcast(intent);
    }

    /**
     * 下线通知
     */
    public void offLineAnnouce() {

        OffLineProgress offLine=new OffLineProgress();
        offLine.post(mHandler);

    }

}
