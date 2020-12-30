package com.mini.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Selection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.ForgetPasswordActivity;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.callback.PopWindowClearCallback;
import com.mini.sdk.dialog.DialogUtil;
import com.mini.sdk.entity.User;
import com.mini.sdk.http.process.LoginProcess;
import com.mini.sdk.http.thirdlogin.ThirdLoginProcess;
import com.mini.sdk.utils.HEtUtils;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class LoginView extends BaseView {

    public Context context;

    public String account = "";
    public String password = "";

    public EditText login_account;
    public EditText login_password;

    public TextView forget_password;

    public Button user_login;

    public LinearLayout phone_register;
    public LinearLayout user_register;
    public LinearLayout visitor_login;

    public RelativeLayout rl_switch_account;
    public ImageView iv_switch_account;
    public RelativeLayout rl_account_clear;

    public RelativeLayout rl_password_eye;
    public ImageView iv_password_eye;
    public RelativeLayout rl_password_clear;

    public SpinerPopWindow spinerPopWindow;
    public LinkedList<User> list;

    public Handler handler;

    /**
     * 防止连续点击
     */
    public boolean flag = true;

    public LoginView(Context context_, Handler handler_) {
        this.context = context_;
        this.handler = handler_;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(Resources.getLayoutId(context, "sdk_login_view"), null);

        initUI();
        initData();
    }

    public void initUI() {

        login_account = (EditText) contentView.findViewById(Resources.getId(context, "login_account"));
        login_password = (EditText) contentView.findViewById(Resources.getId(context, "login_password"));

        forget_password = (TextView) contentView.findViewById(Resources.getId(context, "forget_password"));
        forget_password.setOnClickListener(this);

        user_login = (Button) contentView.findViewById(Resources.getId(context, "user_login"));
        user_login.setOnClickListener(this);

        phone_register = (LinearLayout) contentView.findViewById(Resources.getId(context, "phone_register"));
        phone_register.setOnClickListener(this);

        user_register = (LinearLayout) contentView.findViewById(Resources.getId(context, "user_register"));
        user_register.setOnClickListener(this);

        visitor_login = (LinearLayout) contentView.findViewById(Resources.getId(context, "visitor_login"));
        visitor_login.setOnClickListener(this);
        if (PluginApi.visitor)
            visitor_login.setVisibility(View.VISIBLE);

        rl_switch_account = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_switch_account"));
        iv_switch_account = (ImageView) contentView.findViewById(Resources.getId(context, "iv_switch_account"));
        rl_account_clear = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_account_clear"));

        rl_switch_account.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list = SharedPreferenceUtil.getUsers(context); // 使光标始终在最后位置
                Selection.setSelection(login_account.getText(), login_account.length());

                if (flag) {
                    flag = false;
                    iv_switch_account.setImageResource(Resources.getDrawableId(context, "mch_account_up"));

                    if (null != list && list.size() != 0) {
                        spinerPopWindow = new SpinerPopWindow(context, list, itemClickListener, itemClearListener);
                        spinerPopWindow.setBackgroundDrawable(context.getResources().getDrawable(Resources.getDrawableId(context, "sdk_pop_gray")));
                        spinerPopWindow.setOnDismissListener(dismissListener);
                        spinerPopWindow.setWidth(login_account.getWidth());
                        spinerPopWindow.showAsDropDown(login_account, 0, 10);
                    }

                } else {
                    flag = true;
                    iv_switch_account.setImageResource(Resources.getDrawableId(context, "sdk_account_down"));
                }
            }
        });

        rl_password_eye = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_password_eye"));
        iv_password_eye = (ImageView) contentView.findViewById(Resources.getId(context, "iv_password_eye"));
        rl_password_clear = (RelativeLayout) (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_password_clear"));

        new HEtUtils().etHandle(context, login_account, rl_account_clear, null, null);
        new HEtUtils().etHandle(context, login_password, rl_password_clear, rl_password_eye, iv_password_eye);
    }

    public void initData() {
        if (ChannelAndGame.getInstance().haveRead()) {
            LinkedList<User> users = SharedPreferenceUtil.getUsers(context);
            User user;
            if (null != users && users.size() != 0) {
                user = users.getFirst();
                account = user.getAccount();
                password = user.getPassword();
                login_account.setText(account);
                login_password.setText(password);
            }
        } else {
            Toast.makeText(context, "获取渠道信息异常", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            spinerPopWindow.dismiss();
            if (list != null && list.size() != 0) {
                login_account.setText(list.get(position).getAccount());
                login_password.setText(list.get(position).getPassword());
            }
        }
    };

    public PopWindowClearCallback itemClearListener = new PopWindowClearCallback() {
        @Override
        public void reslut(final int position) {
            DialogUtil.dialog_mch_alert(context, "提示", "确定要删除账号吗？", "确定", "取消", new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (list != null && list.size() != 0) {
                        if (login_account.getText().toString().trim().equals(list.get(position).getAccount())) {
                            list.remove(position);
                            if (list.size() > 0) {
                                login_account.setText(list.get(0).getAccount());
                                login_password.setText(list.get(0).getPassword());
                            } else {
                                login_account.setText("");
                                login_password.setText("");
                            }
                        }
                        spinerPopWindow.dismiss();
                        SharedPreferenceUtil.removeAndSaveUserInfoList(context, position);
                    }
                }
            }).show();
        }
    };
    /**
     * 监听popupwindow取消
     */
    public PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            flag = true;
            iv_switch_account.setImageResource(Resources.getDrawableId(context, "sdk_account_down"));
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == user_login.getId()) {
            account = login_account.getText().toString().trim();
            password = login_password.getText().toString().trim();

            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
            if (account.length() < 6 || account.length() > 16 || pat.matcher(account).find()) {
                Toast.makeText(context, "账号只能由6至16位英文或数字组成", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6 || password.length() > 16 || pat.matcher(password).find()) {
                Toast.makeText(context, "密码只能由6至16位英文或数字组成", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginProcess loginprocess = new LoginProcess();
            loginprocess.setAccount(account);
            loginprocess.setPassword(password);
            loginprocess.post(handler);

        } else if (v.getId() == forget_password.getId()) {
            Intent intent = new Intent();
            intent.setClass(context, ForgetPasswordActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (v.getId() == visitor_login.getId()) {
            startVisitorsLogin();
        }
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    public void setOnClick(OnClickListener onclick) {
        phone_register.setOnClickListener(onclick);
        user_register.setOnClickListener(onclick);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 发起游客登录
     */
    public void startVisitorsLogin() {
        ThirdLoginProcess process = new ThirdLoginProcess();
        process.post(handler);
    }

}
