package com.mini.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置宽高
     *
     * @param width  dp-px
     * @param height %
     */
    public static void width_height(Activity activity, float width, float height) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = activity.getWindow().getAttributes(); // 获取对话框当前的参数值
        params.width = (int) (display.getWidth() * width); // 宽度设置为屏幕的
        params.height = dip2px(activity, height); // 高度设置为屏幕的
        activity.getWindow().setAttributes(params); // 设置生效
    }
}
