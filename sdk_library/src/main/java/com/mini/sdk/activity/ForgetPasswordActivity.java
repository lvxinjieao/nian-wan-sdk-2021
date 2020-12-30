package com.mini.sdk.activity;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ServiceModel;
import com.mini.sdk.entity.User;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.http.check.CheckCodeRequest;
import com.mini.sdk.http.process.ForgetPasswordProcess;
import com.mini.sdk.http.process.UserInfoProcess;
import com.mini.sdk.http.process.VerificationCodeProcess;
import com.mini.sdk.observer.SecondsWatcher;
import com.mini.sdk.utils.CodeUtils;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;
import com.mini.sdk.view.util.TimeFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {

    private ImageView iv_step1, iv_step2, iv_step3, iv_step4;
    private TextView tv_step1, tv_step2, tv_step3, tv_step4;

    // -step 1
    private RelativeLayout step_first;
    private EditText step_first_username;
    private Button step_first_submit;

    // -step 2
    ScrollView step_second;
    TextView step_second_username;
    TextView step_second_phonenumber;

    EditText step_second_et_phone_code;//输入验证码
    Button step_second_btn_phone_code;//获取验证码

    Button step_second_btn_back;
    Button step_second_btn_next;
    private TextView step_second_service;

    // -step 3
    ScrollView step_three;
    TextView step_three_tv_account;
    EditText step_three_et_password1; // 新密码
    EditText step_three_et_password2; // 重新输入密码
    Button step_three_btn_back; // 返回上一步
    Button step_three_btn_submit; // 验证验证码

    // -step 4
    private LinearLayout step_fourth;
    TextView step_fourth_tv_current;
    TextView step_fourth_btn_back;

    /**
     * 账号
     */
    String account;
    String password;

    String phone;//手机号
    String id;

    String phone_code;//手机验证码

    private static final int step_0 = 0;
    private static final int step_1 = 1;
    private static final int step_2 = 2;
    private static final int step_3 = 3;
    private static final int step_4 = 4;

    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case step_0:
                    String seconds = (String) msg.obj;
                    if (null != step_second_btn_phone_code) {
                        if (!seconds.equals("0")) {
                            step_second_btn_phone_code.setText(seconds);
                            step_second_btn_phone_code.setEnabled(false);
                            step_second_btn_phone_code.setTextColor(Color.parseColor("#949494"));
                        } else if (seconds.equals("0")) {
                            step_second_btn_phone_code.setText("获取验证码");
                            step_second_btn_phone_code.setEnabled(true);
                            step_second_btn_phone_code.setTextColor(Color.parseColor("#99CC33"));
                        }
                    }
                    break;

                case step_1:
                    step_subacc();
                    break;
                case step_2:
                    step_secval();
                    break;
                case step_3:
                    step_respwd();
                    break;
                case step_4:
                    step_succes();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Resources.getLayoutId(this, "activity_forget_password"));
        initView();
        step_subacc();
    }

    private void initView() {
        TextView title = (TextView) findViewById(Resources.getId(this, "title"));
        title.setText("忘记密码");

        TextView back = (TextView) findViewById(Resources.getId(this, "back"));
        back.setOnClickListener(backClick);

        iv_step1 = (ImageView) findViewById(Resources.getId(this, "iv_step1"));
        iv_step2 = (ImageView) findViewById(Resources.getId(this, "iv_step2"));
        iv_step3 = (ImageView) findViewById(Resources.getId(this, "iv_step3"));
        iv_step4 = (ImageView) findViewById(Resources.getId(this, "iv_step4"));

        tv_step1 = (TextView) findViewById(Resources.getId(this, "tv_step1"));
        tv_step2 = (TextView) findViewById(Resources.getId(this, "tv_step2"));
        tv_step3 = (TextView) findViewById(Resources.getId(this, "tv_step3"));
        tv_step4 = (TextView) findViewById(Resources.getId(this, "tv_step4"));

        // -step 1
        step_first = (RelativeLayout) findViewById(Resources.getId(this, "step_first"));
        step_first_username = (EditText) findViewById(Resources.getId(this, "step_first_username"));
        step_first_submit = (Button) findViewById(Resources.getId(this, "step_first_submit"));
        step_first_submit.setOnClickListener(onClickListener);

        // -step 2
        step_second = (ScrollView) findViewById(Resources.getId(this, "step_second"));
        step_second_username = (TextView) findViewById(Resources.getId(this, "step_second_username"));
        step_second_phonenumber = (TextView) findViewById(Resources.getId(this, "step_second_phonenumber"));

        step_second_et_phone_code = (EditText) findViewById(Resources.getId(this, "step_second_et_phone_code"));

        step_second_btn_phone_code = (Button) findViewById(Resources.getId(this, "step_second_btn_phone_code"));
        step_second_btn_phone_code.setOnClickListener(onClickListener);

        step_second_btn_back = (Button) findViewById(Resources.getId(this, "step_second_btn_back"));
        step_second_btn_back.setOnClickListener(onClickListener);

        step_second_btn_next = (Button) findViewById(Resources.getId(this, "step_second_btn_next"));
        step_second_btn_next.setOnClickListener(onClickListener);
        step_second_service = (TextView) findViewById(Resources.getId(this, "step_second_service"));
        step_second_service.setOnClickListener(onClickListener);

        // -step 3
        step_three = (ScrollView) findViewById(Resources.getId(this, "step_three"));
        step_three_tv_account = (TextView) findViewById(Resources.getId(this, "step_three_tv_account"));
        step_three_et_password1 = (EditText) findViewById(Resources.getId(this, "step_three_et_password1"));
        step_three_et_password2 = (EditText) findViewById(Resources.getId(this, "step_three_et_password2"));
        step_three_btn_back = (Button) findViewById(Resources.getId(this, "step_three_btn_back"));
        step_three_btn_back.setOnClickListener(onClickListener);
        step_three_btn_submit = (Button) findViewById(Resources.getId(this, "step_three_btn_submit"));
        step_three_btn_submit.setOnClickListener(onClickListener);

        // -step 4
        step_fourth = (LinearLayout) findViewById(Resources.getId(this, "step_fourth"));
        step_fourth_btn_back = (TextView) findViewById(Resources.getId(this, "step_fourth_btn_back"));
        step_fourth_btn_back.setOnClickListener(onClickListener);
    }

    // set_1
    void step_subacc() {
        step_first.setVisibility(View.VISIBLE);
        step_second.setVisibility(View.GONE);
        step_three.setVisibility(View.GONE);
        step_fourth.setVisibility(View.GONE);

        iv_step1.setBackgroundResource(Resources.getDrawableId(this, "sdk_step1_select"));
        iv_step2.setBackgroundResource(Resources.getDrawableId(this, "sdk_step2_unselect"));
        iv_step3.setBackgroundResource(Resources.getDrawableId(this, "sdk_step3_unselect"));
        iv_step4.setBackgroundResource(Resources.getDrawableId(this, "sdk_step4_unselect"));

        tv_step1.setTextColor(Color.parseColor("#99CC33"));
        tv_step2.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step3.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step4.setTextColor(Color.parseColor("#a0a0a0"));
    }

    // set_2
    void step_secval() { // show view

        step_first.setVisibility(View.GONE);
        step_second.setVisibility(View.VISIBLE);
        step_three.setVisibility(View.GONE);
        step_fourth.setVisibility(View.GONE);

        iv_step1.setBackgroundResource(Resources.getDrawableId(this, "sdk_step1_unselect"));
        iv_step2.setBackgroundResource(Resources.getDrawableId(this, "sdk_step2_select"));
        iv_step3.setBackgroundResource(Resources.getDrawableId(this, "sdk_step3_unselect"));
        iv_step4.setBackgroundResource(Resources.getDrawableId(this, "sdk_step4_unselect"));

        tv_step1.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step2.setTextColor(Color.parseColor("#99CC33"));
        tv_step3.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step4.setTextColor(Color.parseColor("#a0a0a0"));
    }

    // set_3
    void step_respwd() {
        step_first.setVisibility(View.GONE);
        step_second.setVisibility(View.GONE);
        step_three.setVisibility(View.VISIBLE);
        step_fourth.setVisibility(View.GONE);

        step_three_tv_account.setText(account);

        iv_step1.setBackgroundResource(Resources.getDrawableId(this, "sdk_step1_unselect"));
        iv_step2.setBackgroundResource(Resources.getDrawableId(this, "sdk_step2_unselect"));
        iv_step3.setBackgroundResource(Resources.getDrawableId(this, "sdk_step3_select"));
        iv_step4.setBackgroundResource(Resources.getDrawableId(this, "sdk_step4_unselect"));

        tv_step1.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step2.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step3.setTextColor(Color.parseColor("#99CC33"));
        tv_step4.setTextColor(Color.parseColor("#a0a0a0"));
    }

    // set_4
    void step_succes() {
        step_first.setVisibility(View.GONE);
        step_second.setVisibility(View.GONE);
        step_three.setVisibility(View.GONE);
        step_fourth.setVisibility(View.VISIBLE);

        iv_step1.setBackgroundResource(Resources.getDrawableId(this, "sdk_step1_unselect"));
        iv_step2.setBackgroundResource(Resources.getDrawableId(this, "sdk_step2_unselect"));
        iv_step3.setBackgroundResource(Resources.getDrawableId(this, "sdk_step3_unselect"));
        iv_step4.setBackgroundResource(Resources.getDrawableId(this, "sdk_step4_select"));

        tv_step1.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step2.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step3.setTextColor(Color.parseColor("#a0a0a0"));
        tv_step4.setTextColor(Color.parseColor("#99CC33"));
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == Resources.getId(ForgetPasswordActivity.this, "step_first_submit")) {
                checkAccount();// 检查用户
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_second_btn_back")) {// step1 返回到step1
                handler.sendEmptyMessage(step_1);
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_second_btn_next")) {
                checkPhoneCode();// 检查验证码是否正确
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_three_btn_back")) { // step3 返回到step2
                handler.sendEmptyMessage(step_2);// step_secval();
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_three_btn_submit")) {
                resetPassword();
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_fourth_btn_back")) {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                finish();
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_second_service")) {
                ServiceModel.getInstance().contactService(ForgetPasswordActivity.this, false);
            } else if (id == Resources.getId(ForgetPasswordActivity.this, "step_second_btn_phone_code")) {
                getPhoneCode();// 获取验证码
            }
        }
    };

    OnClickListener backClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        TimeFactory.creator(0).getTimeChange().addWatcher(watcher);
    }

    /**
     * 重置密码请求
     */
    protected void resetPassword() {
        String password1 = step_three_et_password1.getText().toString().trim();
        String password2 = step_three_et_password2.getText().toString().trim();

        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password1.matches(Constant.REGULAR_ACCOUNT)) {
            Toast.makeText(this, "新密码格式不正确", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.equals(password1, password2)) {
            Toast.makeText(this, "密码与确认密码不一致", Toast.LENGTH_LONG).show();
            return;
        }

        password = password2;

        ForgetPasswordProcess rocess = new ForgetPasswordProcess();
        rocess.setPhone(phone);
        rocess.setCodeType(1);
        rocess.setPassword(password);
        rocess.setCode(phone_code);
        rocess.setId(id);
        rocess.post(myHandler);
    }

    //验证码是否正確
    protected void checkPhoneCode() {

        phone_code = step_second_et_phone_code.getText().toString().trim();

        if (TextUtils.isEmpty(phone_code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return;
        }

        CheckCodeRequest request = new CheckCodeRequest(phone, phone_code);

        request.post(Constant.CHECK_PHONE_CODE, new HttpCallback() {

            @Override
            public void requestSuccess(String result) {
                JSONObject json = null;
                int status = -1;
                try {
                    json = new JSONObject(result);
                    status = json.optInt("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                if (status == 200) {// 验证码验证成功
                    handler.sendEmptyMessage(step_3);
                } else {
                    String msg;
                    if (!TextUtils.isEmpty(json.optString("return_msg"))) {
                        msg = json.optString("return_msg");
                    } else {
                        msg = CodeUtils.getErrorMsg(status);
                    }
                    Logs.e("msg:" + msg);
                    Toast.makeText(ForgetPasswordActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Toast.makeText(ForgetPasswordActivity.this, "服务器开小差", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void checkAccount() {

        String account = step_first_username.getText().toString();
        Logs.i("忘记密码 确认账号 : " + account);
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }

        if (!account.matches(Constant.REGULAR_ACCOUNT)) {
            Toast.makeText(ForgetPasswordActivity.this, "账号格式不正确", Toast.LENGTH_LONG).show();
            return;
        }

        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.setType("1");
        userInfoProcess.setAccount(account);
        userInfoProcess.post(myHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeFactory.creator(0).getTimeChange().removeWatcher(watcher);
    }

    SecondsWatcher watcher = new SecondsWatcher() {

        @Override
        public void updateSeconds(String seconds) {
            Message msg = new Message();
            msg.what = step_0;
            msg.obj = seconds;
            handler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_USER_INFO_SUCCESS:// 根据账号查找手机号码
                    UserLogin info = (UserLogin) msg.obj;
                    subAccToSecVal(info);
                    break;

                case Constant.GET_USER_INFO_FAIL:
                    String tip = (String) msg.obj;
                    Toast.makeText(ForgetPasswordActivity.this, tip, Toast.LENGTH_LONG).show();
                    break;

                case Constant.VERIFYCODE_REQUEST_SUCCESS:// 获得手机验证码成功
                    step_second_btn_phone_code.setEnabled(false);
                    step_second_btn_phone_code.setTextColor(Color.parseColor("#949494"));
                    Toast.makeText(ForgetPasswordActivity.this, "验证码发送成功　请注意查收", Toast.LENGTH_LONG).show();
                    TimeFactory.creator(0).Start();
                    break;

                case Constant.VERIFYCODE_REQUEST_FAIL:// 获得手机验证码失败
                    step_second_btn_phone_code.setText("获取验证码");
                    step_second_btn_phone_code.setEnabled(true);
                    step_second_btn_phone_code.setTextColor(Color.parseColor("#99CC33"));
                    String res = (String) msg.obj;
                    Toast.makeText(ForgetPasswordActivity.this, res, Toast.LENGTH_LONG).show();
                    break;

                case Constant.MODIFY_PASSWORD_SUCCESS://修改密码成功
                    LinkedList<User> list = SharedPreferenceUtil.getUsers(ForgetPasswordActivity.this);

                    if (null != list && list.size() != 0) {
                        User user;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getAccount().equals(PluginApi.userLogin.getAccount())) {
                                user = list.get(i);
                                user.setPassword(password);
                                SharedPreferenceUtil.saveUser(ForgetPasswordActivity.this, user);
                            }
                        }
                    }
                    Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    handler.sendEmptyMessage(step_4);
                    break;

                case Constant.MODIFY_PASSWORD_FAIL://修改密码失败
                    Toast.makeText(ForgetPasswordActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;

                case Constant.IS_CODE_SUCCESS:// 忘记密码 ，验证码验证成功，继续下一步
                    step_respwd();
                    break;

                case Constant.IS_CODE_FAIL:
                    String message = msg.obj.toString();
                    if (!TextUtils.isEmpty(message)) {
                        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 验证是否绑定手机号
         */
        private void subAccToSecVal(UserLogin info) {

            if (null == info || TextUtils.isEmpty(info.getAccount())) {
                if (null == info) {
                    Logs.e("fun#subAccToSecVal info is null ");
                }
                Toast.makeText(ForgetPasswordActivity.this, "账号不存在", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(info.getPhoneNumber())) {
                Toast.makeText(ForgetPasswordActivity.this, "您的帐号暂无绑定手机，请联系客服", Toast.LENGTH_LONG).show();
                return;
            }

            id = info.getId();
            account = info.getAccount();
            phone = info.getPhoneNumber();
            String validateCode = Constant.REGULAR_PHONENUMBER;

            if (!TextUtils.isEmpty(phone) && !phone.matches(validateCode)) {
                Toast.makeText(ForgetPasswordActivity.this, "该账号绑定的手机号码格式不正确" + phone, Toast.LENGTH_LONG).show();
                return;
            }

            if (!TextUtils.isEmpty(phone) && phone.matches(validateCode)) {
                String showPhoNum = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                step_second_phonenumber.setText(showPhoNum);
                step_second_username.setText(info.getAccount());
            }
            step_secval();
        }
    };

    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        step_second_btn_phone_code.setEnabled(false);
        step_second_btn_phone_code.setTextColor(Color.parseColor("#19B1EA"));
        VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
        verifyCodeProcess.setPhone(phone);
        verifyCodeProcess.post(myHandler);
    }

}
