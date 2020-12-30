package com.mini.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.http.process.CertificateProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Resources;

public class CertificationView extends BaseView {

    public Activity activity;

    private EditText card_name;
    private EditText card_id;

    private TextView submit;
    private TextView exit;


    public CertificationView(Activity activity_) {
        this.activity = activity_;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(Resources.getLayoutId(activity, "sdk_certification"), null);
        initUI();
    }


    public void initUI() {
        card_name = contentView.findViewById(Resources.getId(activity, "card_name"));
        card_id = contentView.findViewById(Resources.getId(activity, "card_id"));

        submit = contentView.findViewById(Resources.getId(activity, "submit"));
        exit = contentView.findViewById(Resources.getId(activity, "exit"));

        exit.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == Resources.getId(activity, "exit")) {
            activity.finish();
        } else if (v.getId() == Resources.getId(activity, "submit")) {
            if (check()){
                real();
            }
        }
    }


    /**
     * 验证姓名 身份证号是否正确
     */
    private String str_card_name;
    private String str_card_id;

    private boolean check() {
        str_card_name = card_name.getText().toString();
        str_card_id = card_id.getText().toString().trim();

        if (TextUtils.isEmpty(str_card_name)) {
            Toast.makeText(activity, "请输入姓名", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!str_card_name.matches(Constant.REGULAR_NAME)) {
            Toast.makeText(activity, "请输入正确的姓名", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(str_card_id)) {
            Toast.makeText(activity, "请输入身份证号", Toast.LENGTH_LONG).show();
            return false;
        }

        if (str_card_id.length() == 15) {
            if (!str_card_id.matches(Constant.REGEX_ID_CARD15)) {
                Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else if (str_card_id.length() == 18) {
            if (!str_card_id.matches(Constant.REGEX_ID_CARD18)) {
                Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else {
            Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    /**
     * 请求实名认证
     */
    private void real() {
        CertificateProcess process = new CertificateProcess();
        process.setIdcard(str_card_id);
        process.setReal_name(str_card_name);
        process.post(handler);
    }

    /**
     * 请求返回
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TO_CERTIFICATE_SUCCESS:
                    String idcard = (String) msg.obj;
                    PluginApi.userLogin.setIdcard(idcard);
                    Toast.makeText(activity,"实名认证成功",Toast.LENGTH_LONG).show();
                    activity.finish();
                    break;
                case Constant.TO_CERTIFICATE_FAIL:
                    Toast.makeText(activity,"实名认证失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
