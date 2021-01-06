package com.mini.sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.bean.VerifyCode;
import com.mini.sdk.entity.User;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.entity.UserRegister;
import com.mini.sdk.http.process.LoginProcess;
import com.mini.sdk.http.process.RegisterPhoneProcess;
import com.mini.sdk.http.process.RegisterProcess;
import com.mini.sdk.http.process.UserInfoProcess;
import com.mini.sdk.listener.LoginResult;
import com.mini.sdk.listener.RegisterListener;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.DensityUtil;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.RequestParamUtil;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.ScreenUtil;
import com.mini.sdk.utils.SharedPreferenceUtil;
import com.mini.sdk.view.LoginView;
import com.mini.sdk.view.PhoneRegisterView;
import com.mini.sdk.view.UserRegisterView;
import com.mini.sdk.view.util.TimeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 9:// 游客登陆

                    break;

                case 10:// 首页
                    if (LoginActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        DensityUtil.width_height(LoginActivity.this, 0.60f, 265);
                    } else {
                        DensityUtil.width_height(LoginActivity.this, 0.9f, 265);
                    }
                    break;

                case 12:// 用户注册
                    if (LoginActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        DensityUtil.width_height(LoginActivity.this, 0.60f, 255);
                    } else {
                        DensityUtil.width_height(LoginActivity.this, 0.9f, 255);
                    }

                    break;

                case 13:// 手机注册
                    if (LoginActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        DensityUtil.width_height(LoginActivity.this, 0.60f, 255);
                    } else {
                        DensityUtil.width_height(LoginActivity.this, 0.9f, 255);
                    }
                    break;

                case 14: // 返回到首页
                    LoginView login = new LoginView(LoginActivity.this, handler);
                    login.setOnClick(onclick);
                    setContentView(login.getContentView());
                    break;

                case 15:// 关闭
                    LoginActivity.this.finish();
                    break;

                case Constant.LOGIN_SUCCESS:// 登陆成功

                    UserLogin loginSuccess = (UserLogin) msg.obj;
                    PluginApi.userLogin = loginSuccess;
                    saveUser(loginSuccess);// 先保存在更新

                    LoginResult loginResult = new LoginResult();
                    loginResult.setCode(LoginResult.SDK_LOGIN_SUCCESS);
                    loginResult.setMessage("登录成功");
                    loginResult.setAccountId(loginSuccess.getAccountId());
                    loginResult.setAccount(loginSuccess.getAccount());
                    loginResult.setToken(loginSuccess.getToken());

                    PluginApi.isLogin = true;// 登录成功后设置个标记

                    if (PluginApi.loginListener != null) {
                        PluginApi.loginListener.result(loginResult);
                    }

                    quereUser();
                    LoginActivity.this.finish();
                    break;

                case Constant.LOGIN_FAIL:// 登陆失败
                    String message = (String) msg.obj;
                    if (TextUtils.isEmpty(message)) {
                        message = "登录失败";
                    }
                    PluginApi.isLogin = false;// 登录失败后设置个标记

                    LoginResult loginFail = new LoginResult();
                    loginFail.setCode(LoginResult.SDK_LOGIN_FAILED);
                    loginFail.setMessage(message);
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    break;

/////////////////////////////////////////////////////////////////////
                case Constant.REGISTER_SUCCESS:// 注册成功
                    UserRegister registerSuccess = (UserRegister) msg.obj;

                    if (PluginApi.registerListener != null)
                        PluginApi.registerListener.result(RegisterListener.SDK_REGISTER_SUCCESS, registerSuccess.getAccountId());

                    register_login(registerSuccess);
                    break;

                case Constant.REGISTER_FAIL:// 注册失败
                    if (PluginApi.registerListener != null)
                        PluginApi.registerListener.result(RegisterListener.SDK_REGISTER_FAILED, "REGISTER FAILED");

                    String register_failed = (String) msg.obj;
                    if (TextUtils.isEmpty(register_failed)) {
                        register_failed = "注册失败";
                    }

                    Toast.makeText(LoginActivity.this, register_failed, Toast.LENGTH_LONG).show();
                    break;
/////////////////////////////////////////////////////////////////////

                case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得验证码成功
                    VerifyCode verifyCode = (VerifyCode) msg.obj;
                    getPhoneCodeSuccess(verifyCode);
                    break;

                case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得验证码失败
                    String res = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, res, Toast.LENGTH_LONG).show();
                    // 获取验证码失败，更新按钮可点击
                    break;

                case 10000:// 保存图片
                    Bitmap bitmap = ScreenUtil.screenShot(LoginActivity.this);
                    ScreenUtil.save(LoginActivity.this, bitmap);
                    break;

                case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息成功
                    UserLogin info = (UserLogin) msg.obj;
                    if (null != info) {
                        handlerUser(info);
                    }
                    break;
                case Constant.GET_USER_INFO_FAIL:// 获取用户信息失败
                    Toast.makeText(LoginActivity.this, msg.toString(), Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    public FrameLayout content;
    public LoginView loginView;
    public PhoneRegisterView phoneRegister;
    public UserRegisterView userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        content = (FrameLayout) decorView.findViewById(android.R.id.content);

        loginView = new LoginView(LoginActivity.this, handler);
        loginView.setOnClick(onclick);
        setContentView(loginView.getContentView());

        handler.sendEmptyMessage(10);
    }

    private OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == Resources.getId(LoginActivity.this, "phone_register")) {
                phoneRegister = new PhoneRegisterView(LoginActivity.this, handler);
                phoneRegister.setOnClick(onclick);
                setContentView(phoneRegister.getContentView());
                handler.sendEmptyMessage(13);
            } else if (v.getId() == Resources.getId(LoginActivity.this, "user_register")) {
                userRegister = new UserRegisterView(LoginActivity.this, handler);
                userRegister.setOnClick(onclick);
                setContentView(userRegister.getContentView());
                handler.sendEmptyMessage(12);
            } else if (v.getId() == Resources.getId(LoginActivity.this, "phone_back")) {
                content.removeAllViews();
                setContentView(loginView.getContentView());
                handler.sendEmptyMessage(10);
            } else if (v.getId() == Resources.getId(LoginActivity.this, "user_register_back")) {
                content.removeAllViews();
                setContentView(loginView.getContentView());
                handler.sendEmptyMessage(10);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (content.getChildAt(0) == loginView.getContentView()) {
            LoginResult loginResult = new LoginResult();
            loginResult.setCode(LoginResult.SDK_LOGIN_FAILED);
            loginResult.setMessage("用户取消登录");
            PluginApi.getInstance(LoginActivity.this).loginListener.result(loginResult);
            this.finish();
        } else {
            content.removeAllViews();
            setContentView(loginView.getContentView());
            handler.sendEmptyMessage(10);
        }
    }

    /**
     * 开始登陆
     */
    private void startUserLogin(String account, String password) {
        LoginProcess loginprocess = new LoginProcess();
        loginprocess.setAccount(account);
        loginprocess.setPassword(password);
        loginprocess.post(handler);
    }


    /**
     * 更新当前用户
     */
    public void saveUser(UserLogin loginSuccess) {
        String account = loginSuccess.getAccount();
        String password = loginSuccess.getPassword();
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        SharedPreferenceUtil.saveUser(this, user);
    }

//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////

    /**
     * 注册请求
     */
    private void startUserRegist(String string_name, String string_password) {
        RegisterProcess registerProcess = new RegisterProcess();
        registerProcess.setAccount(string_name);
        registerProcess.setPassword(string_password);
        registerProcess.post(handler);
    }

    /**
     * 手机号注册请求
     */
    private void startPhoneUserRegist(String account, String password, String code) {
        RegisterPhoneProcess registerProcess = new RegisterPhoneProcess();
        registerProcess.setAccount(account);
        registerProcess.setPassword(password);
        registerProcess.setCode(code);
        registerProcess.post(handler);
    }

    /**
     * 成功请求手机验证码
     */
    private void getPhoneCodeSuccess(VerifyCode verifyCode) {
        Toast.makeText(LoginActivity.this, "验证码发送成功　请注意查收", Toast.LENGTH_LONG).show();
        TimeFactory.creator(1).Start();
    }

    /**
     * 注册成功
     *
     * @param registerSuccess 返回值
     */
    private void register_login(UserRegister registerSuccess) {
        if (registerSuccess.getCode() == 1) {
            handler.sendEmptyMessage(10000);
            if (!TextUtils.isEmpty(registerSuccess.getUserName()) && !TextUtils.isEmpty(registerSuccess.getPassword())) {
                startUserLogin(registerSuccess.getUserName(), registerSuccess.getPassword());
            }
        } else {
            String message = registerSuccess.getMessage();
            if (!TextUtils.isEmpty(message)) {
                if ("null".equals(message)) {
                    Toast.makeText(LoginActivity.this, "账号已被注册", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

    private void quereUser() {
        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.setType("0");
        userInfoProcess.setAccount("");
        userInfoProcess.post(handler);
    }

    public String age_status;
    public int type = 1;// 标识是否实名认证 -1 该用户ID不存在 0该用户身份未审核 1 该用户身份审核未通过 2该用户身份审核通过成年 3该用户身份审核通过未成年

    protected void handlerUser(UserLogin info) {
        age_status = info.getAge_status();
        if (age_status.equals("0")) {// 未认证
            type = 0;
            startActivity(new Intent(LoginActivity.this, CertificationActivity.class));
            LoginActivity.this.finish();
        } else if (age_status.equals("1")) {// 未通过
            type = 1;
        } else if (age_status.equals("2")) {// 已认证
            type = 2;
            LoginActivity.this.finish();
        } else if (age_status.equals("3")) {// 已认证未满18周岁
            type = 3;
        } else if (age_status.equals("4")) {// 审核中
            type = 4;
        }
    }
}
