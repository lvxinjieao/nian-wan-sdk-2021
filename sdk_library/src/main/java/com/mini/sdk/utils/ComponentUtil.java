package com.mini.sdk.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;

public class ComponentUtil {

    public ComponentUtil() {
        throw new UnsupportedOperationException("ActivitySkipUtil不能实例化");
    }

    /**
     * 功能描述:简单地Activity的跳转(不携带任何数据)
     *
     * @param activity 发起跳转的Activity实例
     * @param cls      目标Activity实例
     * @Time 2016年4月25日
     * @Author lizy18
     */
    public static void skipActivity(Activity activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    /**
     * 开启service
     *
     * @param activity
     * @param cls
     */
    public static void startService(Activity activity, Class<? extends Service> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startService(intent);
    }

    /**
     * 停止service
     *
     * @param activity
     * @param cls
     */
    public static void stopService(Activity activity, Class<? extends Service> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startService(intent);
    }

    public static void startViewView(Activity activity, Class<? extends Activity> cls, String title, String url) {
        Intent i = new Intent(activity, cls);
        i.putExtra("title", title);
        i.putExtra("url", url);
        activity.startActivity(i);
    }




}
