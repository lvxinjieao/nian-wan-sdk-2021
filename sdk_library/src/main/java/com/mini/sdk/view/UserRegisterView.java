package com.mini.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mini.sdk.activity.WebViewActivity;
import com.mini.sdk.http.process.RegisterProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HEtUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegisterView extends BaseView {

    /**
     * 上下文
     */
    private Context context;
    private Handler handler;

    /**
     * 用户名
     **/
    protected static final String KEY_ACCOUNT = "mc_account";
    /**
     * 密码
     **/
    protected static final String KEY_PASSWORD = "mc_password";

    /**
     * 是否阅读
     */
    boolean isRead = true;

    private ImageView user_register_back;

    /**
     * 用户名输入框
     */
    EditText user_register_account;

    /**
     * 密码输入框
     */
    EditText user_register_password1;

    /**
     * 重复密码
     */
    EditText user_register_password2;

    /**
     * 条款按钮
     */
    Button btn_read;

    /**
     * 注册
     */
    Button user_register;

    public UserRegisterView(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(Resources.getLayoutId(context, "sdk_user_register"), null);

        initView();
    }

    public void initView() {

        user_register_back = (ImageView) contentView.findViewById(Resources.getId(context, "user_register_back"));
        user_register_back.setOnClickListener(this);

        user_register_account = (EditText) contentView.findViewById(Resources.getId(context, "user_register_account"));
        user_register_password1 = (EditText) contentView.findViewById(Resources.getId(context, "user_register_password1"));
        user_register_password2 = (EditText) contentView.findViewById(Resources.getId(context, "user_register_password2"));

        btn_read = (Button) contentView.findViewById(Resources.getId(context, "btn_read"));
        btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_select"));
        btn_read.setOnClickListener(readClick);

        contentView.findViewById(Resources.getId(context, "txt_read")).setOnClickListener(readClick);
        contentView.findViewById(Resources.getId(context, "txt_agreement")).setOnClickListener(readClick);

        RelativeLayout rlPwdEye = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_user_register_account_pwd_eye"));
        RelativeLayout rlRePwdEye = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_user_register_account_repwd_eye"));
        ImageView ivPwdEye = (ImageView) contentView.findViewById(Resources.getId(context, "iv_user_register_account_pwd_eye"));
        ImageView ivRePwdEye = (ImageView) contentView.findViewById(Resources.getId(context, "iv_user_register_account_repwd_eye"));

        new HEtUtils().etHandle(context, user_register_password1, null, rlPwdEye, ivPwdEye);
        new HEtUtils().etHandle(context, user_register_password2, null, rlRePwdEye, ivRePwdEye);

        user_register = (Button) (Button) contentView.findViewById(Resources.getId(context, "user_register"));
        user_register.setOnClickListener(this);

    }

    OnClickListener readClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            isRead = !isRead;
            if (Resources.getId(context, "txt_agreement") == v.getId()) {// 点击的是协议连接
                isRead = true;
                Logs.i("用户协议 " + Constant.user_agreement_url);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "用户协议");
                intent.putExtra("url", Constant.user_agreement_url);
                context.startActivity(intent);


            }
            if (isRead) {
                btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_select"));
            } else {
                btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_unselect"));
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == user_register.getId()) {
            String account = user_register_account.getText().toString();
            String password1 = user_register_password1.getText().toString().trim();
            String password2 = user_register_password2.getText().toString().trim();

            if (TextUtils.isEmpty(account)) {
                Toast.makeText(context, "请输入账号", Toast.LENGTH_LONG).show();
                return;
            }
            if (account.length() < 6) {
                Toast.makeText(context, "账号格式不正确", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password1)) {
                Toast.makeText(context, "请输入密码", Toast.LENGTH_LONG).show();
                return;
            }
            if (!isRegularAcc(password1)) {
                Toast.makeText(context, "密码格式不正确", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password2)) {
                Toast.makeText(context, "请再次输入密码", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password1.equals(password2)) {
                Toast.makeText(context, "两次输入密码不一致", Toast.LENGTH_LONG).show();
                return;
            }
            if (!isRead) {
                Toast.makeText(context, "请阅读并同意用户注册协议", Toast.LENGTH_LONG).show();
                return;
            }
            startUserRegist(account, password2);
        }
    }

    /**
     * 注册请求
     */
    private void startUserRegist(String account, String password) {
        RegisterProcess registerProcess = new RegisterProcess();
        registerProcess.setAccount(account);
        registerProcess.setPassword(password);
        registerProcess.post(handler);
    }

    public void setOnClick(OnClickListener onclick) {
        user_register_back.setOnClickListener(onclick);
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    private boolean isRegularAcc(String account) {
        String reg = Constant.REGULAR_ACCOUNT;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher_acc = pattern.matcher(account);
        boolean b_acc = matcher_acc.matches();

        if (!b_acc) {
            return false;
        }
        return true;
    }
}
