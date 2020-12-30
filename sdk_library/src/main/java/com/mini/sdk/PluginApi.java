package com.mini.sdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mini.sdk.activity.ChargeActivity;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.activity.LoginActivity;
import com.mini.sdk.activity.QuickLoginActivity;
import com.mini.sdk.bean.AntiAddictionModel;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.bean.Init;
import com.mini.sdk.bean.OffLineAnnounceModel;
import com.mini.sdk.bean.UploadRole;
import com.mini.sdk.dialog.DialogUtil;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.floats.FloatingMagnetView;
import com.mini.sdk.floats.FloatingView;
import com.mini.sdk.floats.MagnetViewListener;
import com.mini.sdk.helpers.DevicesIDsHelper;
import com.mini.sdk.listener.AnnounceTimeCallBack;
import com.mini.sdk.listener.ExitListener;
import com.mini.sdk.listener.InitListener;
import com.mini.sdk.listener.LoginListener;
import com.mini.sdk.listener.LogoutListener;
import com.mini.sdk.listener.OrderInfo;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.listener.RegisterListener;
import com.mini.sdk.listener.UpdateRoleListener;
import com.mini.sdk.utils.ComponentUtil;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.DeviceInfo;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;
import com.mini.sdk.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PluginApi {

    private static PluginApi instance;
    public static Activity activity;

    public static String oaid = "";

    public DevicesIDsHelper devicesIDsHelper;

    public static String getImei() {
        String imei_ = DeviceInfo.deviceId(activity);
        imei_ = TextUtils.isEmpty(imei_) ? "" : imei_;
        Logs.i("imei=" + imei_);
        return imei_;
    }

    public static String getAndroidId() {
        String androidId = DeviceInfo.androidId(activity);
        androidId = TextUtils.isEmpty(androidId) ? "" : androidId;
        Logs.i("androidId=" + androidId);
        return androidId;
    }

    public static String getOaid() {
        Logs.i("oaid=" + PluginApi.oaid);
        return PluginApi.oaid;
    }

    public static String getMac() {
        String mac = DeviceInfo.macAddress();
        mac = TextUtils.isEmpty(mac) ? "" : mac;
        Logs.i("mac=" + mac);
        return mac;
    }

    Handler hand = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            init = true;
            PluginApi.quick_login = SharedPreferenceUtil.getFastLogin(activity);
            PluginApi.initListener.result(InitListener.SDK_INIT_SUCCESS, "init success...");
        }
    };

    /**
     * 是否已登录
     */
    public static boolean isLogin = false;
    /**
     * 一键试玩
     */
    public static boolean visitor = false;
    /**
     * 快捷登陆
     */
    public static boolean quick_login = false;
    /**
     * 用户的信息
     */
    public static UserLogin userLogin = new UserLogin();
    /**
     * 控制多次点击支付按钮
     */
    public static boolean flag = true;


    public FloatingMagnetView view;

    /**
     * 初始化
     */
    public static boolean init = false;

    public static InitListener initListener;
    public static RegisterListener registerListener;
    public static LoginListener loginListener;
    public static LogoutListener logoutListener;
    public static ExitListener exitListener;

    public static AnnounceTimeCallBack announceTimeCallBack;

    public static PluginApi getInstance(Activity activity) {
        if (null == instance) {
            instance = new PluginApi(activity);
        }
        return instance;
    }

    public PluginApi(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }


    public void init(InitListener initListener_, RegisterListener registerListener_, LogoutListener logoutListener_, ExitListener exitListener_, boolean debug_) {
        registerListener = registerListener_;
        init(initListener_, logoutListener_, exitListener_, debug_);
    }

    public void init(InitListener initListener_, LogoutListener logoutListener_, ExitListener exitListener_, boolean debug) {
        Logs.isDebug = debug;
        init(initListener_, logoutListener_, exitListener_);
    }

    public void init(final InitListener initListener_, LogoutListener logoutListener_, ExitListener exitListener_) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermission();
        }

        try {
            devicesIDsHelper = new DevicesIDsHelper(new DevicesIDsHelper.AppIdsUpdater() {

                @Override
                public void OnIdsAvalid(String ids) {
                    PluginApi.oaid = TextUtils.isEmpty(ids) ? "" : ids;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            devicesIDsHelper.getOAID(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initListener = initListener_;
        logoutListener = logoutListener_;
        exitListener = exitListener_;

        Init.getInstance().doInit(activity, hand);

        try {
            FloatingView.getInstance(activity).init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        view = FloatingView.getInstance(activity).getView();
        view.setVisibility(View.GONE);

        FloatingView.getInstance(activity).listener(new MagnetViewListener() {

            @Override
            public void onRemove(FloatingMagnetView paramFloatingMagnetView) {

            }

            @Override
            public void onClick(FloatingMagnetView paramFloatingMagnetView) {
                Intent intent = new Intent(activity, FloatWebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }
    // =====================================================================================

    /**
     * ActionSDK建议获得Manifest.permission.READ_PHONE_STATE权限，上报数据会更准确，不强制要求
     * 本示例在onCreate时获取，开发者可以根据自己的情况去适配动态权限，在GDTAction.logAction之前获取即可
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> permissions = new ArrayList<String>();

        if (!(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissions.size() != 0) {  // 请求所缺少的权限，建议获取，不强制
            String[] requestPermissions = new String[permissions.size()];
            permissions.toArray(requestPermissions);
            activity.requestPermissions(requestPermissions, 1024);
        }
    }


    /**
     * 登录
     */
    public void login(LoginListener loginListener_) {


        if (null == activity) {
            Logs.e("start login context is null");
            return;
        }

        if (null != loginListener_) {
            loginListener = loginListener_;
        }

        if (isLogin) {
            Toast.makeText(activity, "用户已登录", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("promote_id", String.valueOf(ChannelAndGame.getInstance().getChannelId()));
        map.put("imei", PluginApi.getImei());
        map.put("mac", PluginApi.getMac());
        map.put("android_id", PluginApi.getAndroidId());
        map.put("oaid", PluginApi.getOaid());
        String param = RequestParamUtil.getRequestParamString(map);

        HttpUtils.getInstance().post(Constant.IS_QUICK_LOGIN, param);

        if (quick_login && !TextUtils.isEmpty(userLogin.getAccount()) && !TextUtils.isEmpty(userLogin.getPassword())) {
            ComponentUtil.skipActivity(activity, QuickLoginActivity.class);
        } else {
            ComponentUtil.skipActivity(activity, LoginActivity.class);
        }
    }


    // =====================================================================================
    // =====================================================================================

    /**
     * 退出接口
     */
    public void exit() {
        if (null == activity) {
            Logs.e(" context is null");
            return;
        }
        DialogUtil.exit(activity);
    }

    // =====================================================================================
    // =====================================================================================

    /**
     * 区服名 角色名
     */
    private String serverName;
    private String roleName;

    public String getServerName() {
        return serverName;
    }

    public String getRoleName() {
        return roleName;
    }

    /**
     * @param serverId     服务器id
     * @param serverName   服务器名称
     * @param roleName     角色名称
     * @param roleLevel    角色等级
     * @param roleCallBack 回调
     */
    public void uploadRole(String serverId, String serverName, String roleName, String roleLevel, UpdateRoleListener roleCallBack) {

        if (!isLogin) {
            Toast.makeText(activity, "用户未登录", Toast.LENGTH_LONG).show();
            return;
        }

        if (null == roleLevel || null == serverName) {
            this.serverName = serverName;
            this.roleName = roleName;
            return;
        }

        UploadRole uploadRole = new UploadRole(Constant.getInstance().getGameId() + "", serverId, serverName, roleName, roleLevel, roleCallBack);
        uploadRole.upload();
    }

    // =====================================================================================
    // =====================================================================================

    public void logout() {
        view.setVisibility(View.GONE);

        if (isLogin) {
            logoutListener.result(LogoutListener.SDK_LOGOUT_SUCCESS, "登出成功.");
            userLogin = null;
            isLogin = false;
            quick_login = false;
        } else {
            logoutListener.result(LogoutListener.SDK_LOGOUT_FAILED, "登出失败.用户未登录");
            Toast.makeText(activity, "用户未登录", Toast.LENGTH_LONG).show();
        }
    }

    // =====================================================================================
    // =====================================================================================

    /**
     * 开始支付
     *
     * @param context  上下文
     * @param order    商品信息
     * @param callback 支付回调
     */

    public static PayListener payListener;
    public static OrderInfo orderInfo;

    public void pay(OrderInfo order, PayListener listener) {

        if (!ChannelAndGame.getInstance().haveRead()) {
            Toast.makeText(activity, "渠道信息异常", Toast.LENGTH_LONG).show();
            return;
        }

        if (!PluginApi.isLogin) {
            Toast.makeText(activity, "用户未登录", Toast.LENGTH_LONG).show();
            return;
        }

        orderInfo = order;
        payListener = listener;

        Intent intent = new Intent(activity, ChargeActivity.class);
        activity.startActivity(intent);
    }
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    public void onStart() {
        FloatingView.getInstance(activity).attach(activity);
    }

    public void onResume() {
        if (isLogin) {
            view.setVisibility(View.VISIBLE);
            requestAntiAddiction();
        }
    }

    public void onPause() {

    }

    public void onStop() {
        FloatingView.getInstance(activity).detach(activity);
        offLineAnnounce(activity);
    }

    public void onDestroy() {
        FloatingView.getInstance(activity).remove();
    }



    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////


    /**
     * 请求防沉迷
     */
    //标记防沉迷是否开启,默认不开启
    public static boolean isStart = false;

    public void requestAntiAddiction() {
        if (isLogin && !isStart) {
            AntiAddictionModel.getInstance().requestAntiAddictionInfo();
        }
    }

    /**
     * 下线通知
     */
    public void offLineAnnounce(Context context) {
        if (isLogin) {
            OffLineAnnounceModel offLineAnnounceModel = new OffLineAnnounceModel(context);
            offLineAnnounceModel.offLineAnnouce();
        }
    }
//======================================================================================================================

}
