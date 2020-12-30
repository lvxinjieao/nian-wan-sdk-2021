package com.mini.sdk.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini.sdk.PluginApi;
import com.mini.sdk.callback.SelectPTBTypeCallback;
import com.mini.sdk.entity.DiscountPrice;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

public class SelectCoinDialog extends DialogFragment {

    public static final String TAG = "SelectCoinDialog";
    /**
     * 上下文
     */
    private Context con;
    /**
     * 对话框标题
     **/
    protected static final String KEY_TITLE = "s_title";
    /**
     * 账户平台币数量
     **/
    protected static final String KEY_USER_PTB = "s_user_ptb";
    /**
     * 游戏平台币数量
     **/
    protected static final String KEY_GAME_PTB = "s_game_ptb";
    /**
     * 游戏平台币数量
     **/
    protected static final String KEY_PAY_PTB = "s_pay_ptb";
    /**
     * 是否能够取消
     **/
    protected static final String KEY_CANCELABLE = "cancelable";

    private Boolean isGamePtb;
    /**
     * 支付
     **/
    private SelectPTBTypeCallback mSelectPTBTypeCallback;
    /**
     * 关闭平台币弹窗监听
     */
    private OnClickListener mCloseListener;
    View containerView = null;
    ImageView img_ptb;
    ImageView img_ptb_bind;
    ImageView img_ptb_un;
    ImageView img_ptb_bind_un;
    LinearLayout ll_ptb;
    LinearLayout ll_ptb_bind;
    ImageView iv_mch_pay_close;

    private TextView tv_money;
    private static final int TYPE_IS_PTB = 0x1001;
    private static final int TYPE_IS_BIND = 0x1002;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_IS_PTB:
                    tv_money.setText(mDiscountPrice.getDiscountPrice());
                    break;
                case TYPE_IS_BIND:
                    tv_money.setText(mDiscountPrice.getPrice());
                    break;
            }
        }
    };

    public SelectCoinDialog() {

    }

    @SuppressLint("ValidFragment")
    public SelectCoinDialog(Context context) {
        this.con = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            containerView = inflater.inflate(Resources.getLayoutId(con, "dialog_coin_select_type"), container, false);
            // 如果有信息,显示加载信息
            Bundle bundle = getArguments();
            if (bundle == null) {
                return containerView;
            }
            isGamePtb = false;

            CharSequence message = bundle.getCharSequence(KEY_TITLE);
            TextView contentView = (TextView) containerView.findViewById(Resources.getId(con, "tx_mch_select_ptb_title"));
            if (TextUtils.isEmpty(message)) {
                contentView.setVisibility(View.GONE);
            } else {
                contentView.setVisibility(View.VISIBLE);
                contentView.setText(message);
            }

            // 平台币
            CharSequence userptb = bundle.getCharSequence(KEY_USER_PTB);
            TextView txtUserPtb = (TextView) containerView.findViewById(Resources.getId(con, "tx_mch_select_ptb_userptb"));
            if (TextUtils.isEmpty(userptb)) {
                txtUserPtb.setVisibility(View.GONE);
            } else {
                SpannableString spastr = new SpannableString(userptb);
                txtUserPtb.setVisibility(View.VISIBLE);
                txtUserPtb.setText(spastr);
            }

            // 绑定平台币
            CharSequence gameptb = bundle.getCharSequence(KEY_GAME_PTB);
            TextView txtGamePtb = (TextView) containerView
                    .findViewById(Resources.getId(con, "tx_mch_select_ptb_bindptb"));
            if (TextUtils.isEmpty(gameptb)) {
                txtGamePtb.setVisibility(View.GONE);
            } else {
                SpannableString spastr = new SpannableString(gameptb);
                txtGamePtb.setVisibility(View.VISIBLE);
                txtGamePtb.setText(spastr);
            }

            // 应付平台币
            CharSequence payptb = bundle.getCharSequence(KEY_PAY_PTB);
            String str = payptb.toString().trim().substring(0, 9);
            String money = payptb.toString().trim().substring(9);
            TextView txtPayPtb = (TextView) containerView
                    .findViewById(Resources.getId(con, "tx_mch_select_ptb_gameptb"));
            if (TextUtils.isEmpty(payptb)) {
                txtPayPtb.setVisibility(View.GONE);
            } else {
                txtPayPtb.setVisibility(View.VISIBLE);
                txtPayPtb.setText(str);
            }
            tv_money = (TextView) containerView.findViewById(Resources.getId(con, "tv_money"));
            tv_money.setText(money);
            iv_mch_pay_close = (ImageView) containerView.findViewById(Resources.getId(con, "iv_mch_pay_close"));
            ll_ptb = (LinearLayout) containerView.findViewById(Resources.getId(con, "ll_ptb"));
            ll_ptb_bind = (LinearLayout) containerView.findViewById(Resources.getId(con, "ll_ptb_bind"));
            img_ptb = (ImageView) containerView.findViewById(Resources.getId(con, "img_ptb"));
            img_ptb_bind = (ImageView) containerView.findViewById(Resources.getId(con, "img_ptb_bind"));
            img_ptb_un = (ImageView) containerView.findViewById(Resources.getId(con, "img_ptb_un"));
            img_ptb_bind_un = (ImageView) containerView.findViewById(Resources.getId(con, "img_ptb_bind_un"));

            ll_ptb.setOnClickListener(selGamePtbClick);
            ll_ptb_bind.setOnClickListener(selGamePtbClick);
            iv_mch_pay_close.setOnClickListener(selGamePtbClick);

            final Button btnPtbPay = (Button) containerView.findViewById(Resources.getId(con, "btn_mch_dialog_ptbpay"));
            btnPtbPay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (PluginApi.flag) {
                        if (null != mSelectPTBTypeCallback) {
                            mSelectPTBTypeCallback.selectPTBPayType(v, isGamePtb);
                        }
                        dismissAllowingStateLoss();
                        PluginApi.flag = false;
                    }
                }
            });

            // 5,是否可以取消, 默认可以取消
            setCancelable(bundle.getBoolean(KEY_CANCELABLE, false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return containerView;
    }

    OnClickListener selGamePtbClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == iv_mch_pay_close.getId()) {
                mCloseListener.onClick(v);
            }
            if (v.getId() == ll_ptb.getId()) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        img_ptb.setVisibility(View.VISIBLE);
                        img_ptb_un.setVisibility(View.GONE);
                        img_ptb_bind.setVisibility(View.GONE);
                        img_ptb_bind_un.setVisibility(View.VISIBLE);
                        noticeResult(TYPE_IS_PTB);
                    }
                });
                isGamePtb = false;
                return;
            }
            if (v.getId() == ll_ptb_bind.getId()) {
                isGamePtb = true;
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        img_ptb.setVisibility(View.GONE);
                        img_ptb_un.setVisibility(View.VISIBLE);
                        img_ptb_bind.setVisibility(View.VISIBLE);
                        img_ptb_bind_un.setVisibility(View.GONE);
                        noticeResult(TYPE_IS_BIND);
                    }
                });
                return;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的样式
        setStyle(STYLE_NO_FRAME, Resources.getStyleId(con, "MCSelectPTBTypeDialog"));
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        PluginApi.flag = true;
    }

    @Override
    public void onStart() {
        // 1, 设置对话框的大小
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size_x = 0;
        float size_y = 0;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// 横屏
            size_x = 1.0f;
            size_y = 0.7f;
            window.getAttributes().width = (int) (windowSize.y * size_x);
            window.getAttributes().height = (int) (windowSize.y * size_y);
        } else {// 竖屏
            size_x = 0.9f;
            size_y = 0.7f;
            window.getAttributes().width = (int) (windowSize.x * size_x);
            window.getAttributes().height = (int) (windowSize.x * size_y);
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public void setmSelectPTBTypeCallback(SelectPTBTypeCallback mSelectPTBTypeCallback) {
        this.mSelectPTBTypeCallback = mSelectPTBTypeCallback;
    }

    public void setmCloseListener(OnClickListener mCloseListener) {
        this.mCloseListener = mCloseListener;
    }


    public static class Builder {
        /**
         * 存放数据的容器
         **/
        private Bundle mmBundle;

        /**
         * 支付
         **/
        private SelectPTBTypeCallback mmSelectPTBTypeCallback;

        /**
         * 关闭平台币支付弹窗
         */
        private OnClickListener mmCloseListener;

        public Builder() {
            mmBundle = new Bundle();
        }

        public Builder setTitle(CharSequence title) {
            mmBundle.putCharSequence(KEY_TITLE, title);
            return this;
        }

        public Builder setPTB(CharSequence ptb) {
            mmBundle.putCharSequence(KEY_USER_PTB, ptb);
            return this;
        }

        public Builder setBindPTB(CharSequence bindptb) {
            mmBundle.putCharSequence(KEY_GAME_PTB, bindptb);
            return this;
        }

        public Builder setPayPTB(CharSequence payptb) {
            mmBundle.putCharSequence(KEY_PAY_PTB, payptb);
            return this;
        }

        public Builder setmmSelectPTBTypeCallback(SelectPTBTypeCallback mmSelectPTBTypeCallback) {
            this.mmSelectPTBTypeCallback = mmSelectPTBTypeCallback;
            return this;
        }

        public Builder setmmCloseListener(OnClickListener mmCloseListener) {
            this.mmCloseListener = mmCloseListener;
            return this;
        }

        private SelectCoinDialog create(Context con) {
            final SelectCoinDialog dialog = new SelectCoinDialog(con);
            // 1,设置显示内容
            dialog.setArguments(mmBundle);
            dialog.setmSelectPTBTypeCallback(mmSelectPTBTypeCallback);
            dialog.setmCloseListener(mmCloseListener);
            return dialog;
        }

        public SelectCoinDialog show(Context con, FragmentManager fm, DiscountPrice discountPrice) {
            if (fm == null) {
                Logs.e("show error : fragment manager is null.");
                return null;
            }
            if (discountPrice == null) {
                Logs.e("DiscountPrice is null");
                return null;
            }
            mDiscountPrice = discountPrice;
            SelectCoinDialog dialog = create(con);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.commitAllowingStateLoss();
            return dialog;
        }
    }

    private static DiscountPrice mDiscountPrice;

    private void noticeResult(int type) {
        if (mDiscountPrice != null) {
            mHandler.sendEmptyMessage(type);
        }
    }
}
