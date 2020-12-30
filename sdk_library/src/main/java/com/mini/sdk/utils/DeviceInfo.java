package com.mini.sdk.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class DeviceInfo {


    public static String deviceId(Context context) {
        String str = "";
        try {
            TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_PHONE_STATE")) {
                        Toast.makeText(context, "请开通相关权限,正常使用本程序！", Toast.LENGTH_LONG).show();
                    }
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_PHONE_STATE"}, 11);
                } else {
                    str = localTelephonyManager.getDeviceId();
                }
            } else {
                str = localTelephonyManager.getDeviceId();
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return str;
    }

    public static String androidId(Context context) {
        if (context == null) {
            return "";
        }
        String str = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(str) ? "" : str;
    }

    // 有兴趣的朋友可以看下NetworkInterface在Android FrameWork中怎么实现的
    public static String macAddress() {
        String address = null;
        try {
            // 把当前机器上的访问网络接口的存入 Enumeration集合中
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                //Logs.d("interfaceName=" + netWork.getName() + ", mac=" + mac);
                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                if (netWork.getName().equals("wlan0")) {
                    //Logs.d(" interfaceName =" + netWork.getName() + ", mac=" + mac);
                    address = mac;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }


    /**
     * 获取手机状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        // 获取手机上部菜单高度
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Logs.v("fun#getStatusBarHeight height:" + height);
        return height;
    }

    /**
     * 手机屏幕高度
     */
    public static int getScreenHeight(Context mContext) {
        return getWindowManager(mContext).getDefaultDisplay().getHeight();
    }

    /**
     * 手机屏幕宽度
     */
    public static int getScreenWidth(Context mContext) {
        return getWindowManager(mContext).getDefaultDisplay().getWidth();
    }

    /**
     * 或得WindowManager
     */
    public static WindowManager getWindowManager(Context mContext) {
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 判断当前手机屏幕是否全屏
     */
    public static boolean isFullScream(Activity mContext) {
        int v = mContext.getWindow().getAttributes().flags;
        Logs.v(mContext.getClass().getSimpleName() + ":" + v + "// 全屏 66816 - 非全屏 65792");
        if (66816 == v) {
            return true;
        } else if (65792 == v) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * 判断当前手机屏幕是否全屏，可在service中使用
     */
    public static boolean isFullScream(Context mContext) {
        View mCheckFullScreenView = new View(mContext);
        mCheckFullScreenView.setBackgroundColor(Color.parseColor("#00000000"));
        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 创建非模态、不可碰触
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // 放在左上角
        params.gravity = Gravity.START | Gravity.TOP;
        params.height = 1;
        params.width = 1;
        // 设置弹出View类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        windowManager.addView(mCheckFullScreenView, params);
        return isFullscreen(mCheckFullScreenView);
    }

    /**
     * Check if fullscreen is activated by a position of a top left View
     *
     * @param topLeftView View which position will be compared with 0,0
     * @return
     */
    public static boolean isFullscreen(View topLeftView) {
        int location[] = new int[2];
        topLeftView.getLocationOnScreen(location);
        return location[0] == 0 && location[1] == 0;
    }

    /**
     * 判断当前屏幕是否是横屏
     */
    public static boolean isVerticalScreen(Activity activity) {
        int flag = activity.getResources().getConfiguration().orientation;
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置全屏
     */
    public static void setFullScream(Activity mContext) {
        mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 取消全屏
     */
    public static void cancelFullScream(Activity mContext) {
        mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkApkExist(Context con, String packname) {
        if (TextUtils.isEmpty(packname)) {
            return false;
        }
        try {
            con.getPackageManager().getApplicationInfo(packname, PackageManager.GET_UNINSTALLED_PACKAGES);
            Logs.w("fun#checkApkExist app " + packname + " exist");
            return true;
        } catch (NameNotFoundException e) {
            Logs.e("fun#checkApkExist NameNotFoundException:" + e);
        }
        return false;
    }

    // public boolean checkApkExist(Context context, String packageName) {
    // if (packageName == null || “”.equals(packageName))
    // return false;
    // try {
    // ApplicationInfo info = context.getPackageManager()
    // .getApplicationInfo(packageName,
    // PackageManager.GET_UNINSTALLED_PACKAGES);
    // return true;
    // } catch (NameNotFoundException e) {
    // return false;
    // }
    // }
}
