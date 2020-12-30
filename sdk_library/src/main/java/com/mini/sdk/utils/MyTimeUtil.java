package com.mini.sdk.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.dialog.DialogUtil;
import com.mini.sdk.entity.AntiAddiction;
import com.mini.sdk.listener.AnnounceTimeCallBack;

/**
 * Created by Administrator on 2017/5/4.
 * 防沉迷计时
 */

public class MyTimeUtil {

    private Context context;
    private String account;
    /**
     * 要计时的时常
     */
    public static long time = 0;

    private long hourscover;


    //未实名认证提示内容
    private String contents_off;

    private AntiAddiction antiAddiction;
    //第一次提示时间
    private long firstTime;
    //第二次提示时间
    private long secondTime;

    //游戏时间（分钟）
    private long playTime;
    //下线时间（分钟）
    private long downTime;

    private TimeCount timeCount;
    private TimeCount timeCount_weirenzheng;

    //处于哪个阶段，1，第一阶段
    private int stage;

    private StopTimingBroadCast broadCast;

    //转换成double类型
    private double getDouble(String str) {
        if (!TextUtils.isEmpty(str)) {
            return Double.parseDouble(str) * 60 * 60;
        } else {
            return 0.00;
        }
    }

    private long hours;
    private int age_status;
    //用来判断是否由游客变为账号登陆
    private String age_status_change;

    public MyTimeUtil(Context context, AntiAddiction anti_Addiction) {
        this.context = context;
        this.antiAddiction = anti_Addiction;

        registerBroadcast();

//        inGameTime=System.currentTimeMillis();
        hours = (long) getDouble(anti_Addiction.getHours());
        hourscover = (long) getDouble(antiAddiction.getHours_cover());
        firstTime = (long) getDouble(antiAddiction.getHours_off_one());
        secondTime = (long) getDouble(antiAddiction.getHours_off_two());
//        firstTime = 10;
//        secondTime = 20;

        playTime = (long) antiAddiction.getPlay_time();
//        playTime = 0;
        downTime = (long) antiAddiction.getDown_time();

        age_status = antiAddiction.getAge_status();
//        age_status = 3;
        contents_off = anti_Addiction.getContents_off();
        account = PluginApi.userLogin.getAccount();
        if (age_status == 0) {
            if (!TextUtils.isEmpty(contents_off)) {
                DialogUtil.showCustomMessage(context, "提示", contents_off, "去认证", "取消", true);
            }
        }
        Logs.w( "hours: " + hours);
    }


    /**
     * 根据审核状态判断是否开启防沉迷
     */
    public void startAntiAddiction(AntiAddiction antiAddictions) {

        if (antiAddictions == null) {
            Logs.e("antiAddictions is null");
            return;
        }

        age_status = antiAddictions.getAge_status();
        if (!account.equals(PluginApi.userLogin.getAccount())) {
            if (age_status == 0) {
                if (!TextUtils.isEmpty(contents_off)) {
                    DialogUtil.showCustomMessage(context, "提示", contents_off, "去认证", "取消", true);
                }
            }
        }
//        age_status = 3;
//        if(OffLineAnnounceModel.leaveGameTime!=0)
//        {
//            hours=(long)((OffLineAnnounceModel.leaveGameTime-inGameTime)/1000);
//        }

        Logs.e("age_status:" + age_status);
        if (age_status == 0) {
            PluginApi.isStart = true;
            //未认证不进入防沉迷
            if (timeCount_weirenzheng == null) {
                timeCount_weirenzheng = new TimeCount(hours * 1000, 1000);
            }
            timeCount_weirenzheng.start();// 开始计时
        }
        if (age_status == 3) {
            PluginApi.isStart = true;
            if (timeCount_weirenzheng != null) {
//                Message msg = new Message();
//                msg.what = STOP_TIMING;
//                mHandler.sendMessage(msg);
                timeCount_weirenzheng.cancel();
            }
            if (downTime > hourscover) {
                //下线时间大于回复时间，在线时间，休息时间清零
//                downTime = 0;
//                PreSharedManager.setLong(Constant.GET_REST_TIME, restTime, context);
//                PreSharedManager.setLong(Constant.GET_EXIST_TIME, existTime, context);
                stage = 1;
                time = firstTime;
            } else {
                //判断处于防沉迷哪个阶段，给需要计时time赋值
                if (playTime < firstTime) {
                    //第一阶段
                    stage = 1;
                    time = firstTime - playTime;
                } else if (playTime <= (firstTime + secondTime)) {
                    //第二阶段
                    stage = 2;
                    time = firstTime + secondTime - playTime;
                    if (time == 0) {
                        //到时两次，并且在恢复时间内退出游戏，此时在线时长为<firstTime + secondTime>。
                        // 并且在恢复时间（即为两次到时之后继续玩游戏时间内）再次登录游戏，此时的恢复时间不足以清除在线时长
                        //此时仍算作休息时间
                        stage = 3;
                        time = hourscover - downTime;
                    }
                } else {
                    stage = 3;
                    //第三阶段，两次时间都超 仍在继续,算作休息时间
//                    Toast.makeText(context, "你已进入疲劳游戏，请注意休息！", Toast.LENGTH_SHORT).show();
                    time = hourscover;
                }
            }
            // 构造CountDownTimer对象
            if (timeCount == null) {
                timeCount = new TimeCount(time * 1000, 1000);
            }
            timeCount.start();// 开始计时
        }
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        broadCast = new StopTimingBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("StopTiming");
        context.registerReceiver(broadCast, filter);

    }

    //到时间了
    private static final int TIME_UP = 0x123;
    //停止计时
    private static final int STOP_TIMING = 0x1234;
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //3h=180min=10800s
                //5h=300min=18000s
                case TIME_UP:

                    if (age_status == 0) {
                        if (!TextUtils.isEmpty(contents_off)) {
                            DialogUtil.showCustomMessage(context, "提示", contents_off,  "确定", "取消", false);
//                            Toast.makeText(context, contents_off, Toast.LENGTH_SHORT).show();
                        }
                        if (timeCount_weirenzheng == null) {
                            timeCount_weirenzheng = new TimeCount(hours * 1000, 1000);
                        }
                        timeCount_weirenzheng.start();// 开始计时
                    }
                    if (age_status == 3) {
                        stage = (int) msg.obj;
//                        if(stage==0)
//                        {//说明第一次进入游戏 且第一阶段计时结束
//                            playTime=firstTime;
//                            stage=1;
//                        }
                        if (stage == 1) {
                            if (playTime == 0) {
                                playTime = firstTime;
                            }
                            //第一阶段结束到时间了
                            if (getAnnounceTimeCallBack() != null) {
                                getAnnounceTimeCallBack().callback("1");
                            }
                            if (!TextUtils.isEmpty(antiAddiction.getContents_one())) {
                                DialogUtil.showCustomMessage(context, "提示", antiAddiction.getContents_one(),  "确定", "取消", false);
//                                Toast.makeText(context, antiAddiction.getContents_one() + "", Toast.LENGTH_SHORT).show();
                            }
                            stage = 2;
                            timeCount = new TimeCount(secondTime * 1000, 1000);
                            timeCount.start();// 开始计时
                        } else if (stage == 2) {
                            //第二阶段结束到时间了
                            if (getAnnounceTimeCallBack() != null) {
                                getAnnounceTimeCallBack().callback("2");
                            }
                            if (!TextUtils.isEmpty(antiAddiction.getContents_two())) {
                                DialogUtil.showCustomMessage(context, "提示", antiAddiction.getContents_two(),   "确定", "取消", false);
//                                Toast.makeText(context, antiAddiction.getContents_two() + "", Toast.LENGTH_SHORT).show();
                            }

//                        ToastUtil.show(context, "你已经入疲劳游戏，请注意休息！");
//                            Toast.makeText(context, "你已经进入疲劳游戏，请注意休息！", Toast.LENGTH_SHORT).show();
                            PluginApi.isStart = false;
                            stage = 3;
                            //两次时间到还在继续玩游戏，算作休息时间
                            timeCount = new TimeCount(hourscover * 1000, 1000);
                            timeCount.start();// 开始计时
                        } else if (stage == 3) {
                            //两次时间到还在继续玩游戏，算作休息时间，此时继续玩游戏时间算在恢复时间内，并且此时的恢复时间已到，算刚进入游戏,初始化
                            Logs.i("两次时间到还在继续玩游戏，算作休息时间，休息时间已到，可视为刚进入游戏");
                            playTime = 0;
                            downTime = 0;
                            stage = 1;
                            timeCount = new TimeCount(firstTime * 1000, 1000);
                            timeCount.start();// 开始计时
                            PluginApi.isStart = true;
                        }
                    }
                    break;

                case STOP_TIMING:

                    if (timeCount != null) {
                        timeCount.cancel();
                        timeCount = null;
//                        timeCount.onFinish();
                    }
                    if (timeCount_weirenzheng != null) {
                        timeCount_weirenzheng.cancel();
                        timeCount_weirenzheng = null;
                        Logs.e("停止计时: ");
                    }
                    if (getAnnounceTimeCallBack() != null) {
                        //此时游戏为在后台运行或退出状态
                        getAnnounceTimeCallBack().callback("3");
                    }
                    PluginApi.isStart = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };


    // /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            // 计时过程显示
//            checking.setText(millisUntilFinished / 1000 + "秒");
//            ToastUtil.show(context, millisUntilFinished / 1000 + "秒");
//            Toast.makeText(context, millisUntilFinished / 1000 + "秒", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {// 计时完毕时触发

            //判断处于哪个阶段，继续计时
            Message msg = new Message();
            msg.what = TIME_UP;
            msg.obj = stage;
            mHandler.sendMessage(msg);
        }
    }

    public AnnounceTimeCallBack getAnnounceTimeCallBack() {
        if (PluginApi.announceTimeCallBack != null) {
            return PluginApi.announceTimeCallBack;
        } else {
            return null;
        }

    }

    private class StopTimingBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = new Message();
            msg.what = STOP_TIMING;
            mHandler.sendMessage(msg);
        }
    }


}
