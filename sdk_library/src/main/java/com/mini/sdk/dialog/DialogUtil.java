package com.mini.sdk.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Point;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.CertificationActivity;
import com.mini.sdk.listener.ExitListener;
import com.mini.sdk.utils.Resources;

public final class DialogUtil {

    public static Dialog dialog;

    private static DialogUtil instance;

    private DialogUtil() {
    }

    public static DialogUtil getInstance() {
        if (instance == null) {
            instance = new DialogUtil();
        }
        return instance;
    }


    private static Dialog lDialog;

    public static void showCustomMessage(final Context context, String title, final String message, String ok, String cancel, final boolean bool) {

        if (lDialog != null) {
            lDialog.dismiss();
        }

        lDialog = new Dialog(context, Resources.getStyleId(context, "MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        lDialog.setContentView(getIdByName(context, "layout", "dialog_mch_alert_exit_main_light"));

        ((TextView) lDialog.findViewById(getIdByName(context, "id", "dialog_title"))).setText(title);
        ((TextView) lDialog.findViewById(getIdByName(context, "id", "dialog_message"))).setText(message);
        ((Button) lDialog.findViewById(getIdByName(context, "id", "ok"))).setText(ok);
        ((Button) lDialog.findViewById(getIdByName(context, "id", "cancel"))).setText(cancel);
        lDialog.show();

        ((Button) lDialog.findViewById(getIdByName(context, "id", "ok"))).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                lDialog.dismiss();
                if (bool) {
                    Intent intent = new Intent(context, CertificationActivity.class);
                    intent.putExtra("type", "0");
                    context.startActivity(intent);
                    lDialog.dismiss();
                }
            }
        });



        ((Button) lDialog.findViewById(getIdByName(context, "id", "cancel"))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog.dismiss();
                return;
            }
        });


    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 游戏退出
     *
     * @param context
     */
    public static void exit(Context context) {
        DialogUtil.exit(context, "提示", "确定要退出吗？", "确定", "取消");
    }

    /**
     * 游戏退出
     *
     * @param context
     * @param title
     * @param message
     * @param ok
     * @param cancel
     */
    public static void exit(final Context context, String title, final String message, String ok, String cancel) {
        final Dialog lDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(Resources.getLayoutId(context, "dialog_mch_alert_exit_main_light"));
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_title"))).setText(title);
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_message"))).setText(message);
        ((Button) lDialog.findViewById(Resources.getId(context, "ok"))).setText(ok);
        ((Button) lDialog.findViewById(Resources.getId(context, "cancel"))).setText(cancel);
        lDialog.show();

        ((Button) lDialog.findViewById(Resources.getId(context, "ok"))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog.dismiss();
                if (null != PluginApi.exitListener) {
                    PluginApi.exitListener.result(ExitListener.SDK_EXIT_SUCCESS, "退出游戏");
                    ((Activity) context).finish();
                    System.exit(0);
                }
            }
        });

        ((Button) lDialog.findViewById(Resources.getId(context, "cancel"))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog.dismiss();
                if (null != PluginApi.exitListener) {
                    PluginApi.exitListener.result(ExitListener.SDK_EXIT_FAILED, "取消");
                }
            }
        });
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    public static Dialog dialog_mch_alert(final Context context, String title, String message, String substr, String clestr, final OnClickListener onClickListener) {
        final Dialog lDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(Resources.getLayoutId(context, "dialog_mch_alert_exit_main_light"));
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_title"))).setText(title);
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_message"))).setText(message);
        ((Button) lDialog.findViewById(Resources.getId(context, "ok"))).setText(substr);
        ((Button) lDialog.findViewById(Resources.getId(context, "cancel"))).setText(clestr);

        ((Button) lDialog.findViewById(Resources.getId(context, "ok"))).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                lDialog.dismiss();
            }
        });

        ((Button) lDialog.findViewById(Resources.getId(context, "cancel"))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog.dismiss();
            }
        });

        return lDialog;
    }

    /**
     * 白色提示弹窗
     */
    public static void showAlert(final Context context, String title, final String message, String ok) {
        final Dialog lDialog = new Dialog(context, Resources.getStyleId(context, "MCSelectPTBTypeDialog"));
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(getIdByName(context, "layout", "sdk_dialog_alert"));
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_title"))).setText(title);
        ((TextView) lDialog.findViewById(Resources.getId(context, "dialog_message"))).setText(message);
        ((Button) lDialog.findViewById(Resources.getId(context, "ok")))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lDialog.dismiss();
                        return;
                    }
                });
        ((Button) lDialog.findViewById(Resources.getId(context, "ok"))).setText(ok);
        lDialog.show();
    }

    /**
     * 跳转弹窗样式
     */
    public static void showRoundProcessDialog(Context context, int layout) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };

        dialog = new AlertDialog.Builder(context).create();
        dialog.setOnKeyListener(keyListener);
        dialog.show();
        // 注意此处要放在show之后 否则会报异常
        dialog.setContentView(layout);
    }

    /**
     * 跳转弹窗样式
     */
    public static Dialog RoundProcessDialog(Context mContext, int layout) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setOnKeyListener(keyListener);
        return dialog;
    }

    /**
     * 设置dialog 位置和大小
     */

    public static Dialog setDialog(Dialog dialog) {
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移. 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
         * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
//        lp.width = 300; // 宽度
//        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
//        dialogWindow.setAttributes(lp);

        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        WindowManager m = dialogWindow.getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.x = 100; // 新位置X坐标
//        p.y = 100; // 新位置Y坐标
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);

        Window window = dialog.getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size;
        int width = windowSize.x;
        int height = windowSize.y;
        // 横屏
        if (width >= height) {
            size = 0.5f;
        } else {
            size = 0.8f;
        }
        window.getAttributes().width = (int) (windowSize.x * size);
//		window.getAttributes().width = (int) 400;
        window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // window.getAttributes().width = windowSize.x;
//		window.getAttributes().height = 500;
        window.setGravity(Gravity.CENTER);
        return dialog;
    }

    /**
     * Refer to external project resources
     *
     * @param context
     * @param className
     * @param name
     * @return
     */
    private static int getIdByName(Context context, String className, String name) {
        if (null == context) {
            return -1;
        }
        int id = getIdByContext(context, className, name);
        if (id > 0) {
            return id;
        }
        String packageName = context.getPackageName();
        Class r = null;
        id = 0;
        try {
            r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static int getIdByContext(Context context, String className, String name) {

        android.content.res.Resources res = null;
        int id = 0;
        try {
            res = context.getResources();
            id = res.getIdentifier(name, className, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

}
