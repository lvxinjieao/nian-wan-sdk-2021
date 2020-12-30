package com.mini.sdk.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.activity.WebViewActivity;
import com.mini.sdk.http.process.RegisterPhoneProcess;
import com.mini.sdk.http.process.VerificationCodeProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HEtUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneRegisterView extends BaseView {

	private Context context;
	private Handler handler;

	private ImageView phone_back;
	private EditText phone_number;
	private EditText phone_code;

	private Button send_sms;

	private EditText phone_password;

	private Button btn_read;
	private TextView txt_agreement;

	private RelativeLayout rl_phone_eye;
	private ImageView iv_phone_eye;

	private Button phone_register;

	/** 是否阅读 */
	private boolean isRead = true;

	int time = 60;

	public PhoneRegisterView(Context context, final Handler handler) {
		this.context = context;
		this.handler = handler;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(Resources.getLayoutId(context, "sdk_phone_register_view"), null);

		initView();
	}

	private void initView() {
		phone_back = (ImageView) contentView.findViewById(Resources.getId(context, "phone_back"));
		phone_back.setOnClickListener(this);

		phone_number = (EditText) contentView.findViewById(Resources.getId(context, "phone_number"));
		phone_code = (EditText) contentView.findViewById(Resources.getId(context, "phone_code"));

		send_sms = (Button) contentView.findViewById(Resources.getId(context, "send_sms"));
		send_sms.setOnClickListener(this);

		phone_password = (EditText) contentView.findViewById(Resources.getId(context, "phone_password"));

		btn_read = (Button) contentView.findViewById(Resources.getId(context, "btn_read"));
		btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_select"));
		btn_read.setOnClickListener(this);

		txt_agreement = (TextView) contentView.findViewById(Resources.getId(context, "txt_agreement"));
		txt_agreement.setOnClickListener(this);

		rl_phone_eye = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_phone_eye"));
		iv_phone_eye = (ImageView) contentView.findViewById(Resources.getId(context, "iv_phone_eye"));

		new HEtUtils().etHandle(context, phone_password, null, rl_phone_eye, iv_phone_eye);

		phone_register = (Button) contentView.findViewById(Resources.getId(context, "phone_register"));
		phone_register.setOnClickListener(this);
	}

	Handler hand = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
				send_sms.setEnabled(false);
				send_sms.setTextColor(Color.parseColor("#949494"));
				Toast.makeText(context, "验证码发送成功　请注意查收",Toast.LENGTH_LONG).show();

				new Thread(new Runnable() {

					@Override
					public void run() {
						for (; time > 0; time--) {
							try {
								new Thread().sleep(1000);
								hand.sendEmptyMessage(0);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

				break;

			case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
				send_sms.setText("获取验证码");
				send_sms.setEnabled(true);
				send_sms.setTextColor(Color.parseColor("#19B1EA"));
				String res = (String) msg.obj;
				Toast.makeText(context, res,Toast.LENGTH_LONG).show();
				break;

			case 0:
				if (time == 0) {
					send_sms.setText("获取验证码");
					send_sms.setEnabled(true);
					removeMessages(0);
					send_sms.setTextColor(Color.parseColor("#19B1EA"));
					time = 60;
				} else {
					send_sms.setText(time + "秒后重新获取");
					send_sms.setEnabled(false);
					send_sms.setTextColor(Color.parseColor("#949494"));
				}
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (1 == 2) {

		} else if (id == btn_read.getId()) {
			isRead = !isRead;
			if (isRead) {
				btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_select"));
			} else {
				btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_unselect"));
			}
		} else if (id == txt_agreement.getId()) {
			isRead = true;
			btn_read.setBackgroundResource(Resources.getDrawableId(context, "circle_select"));
			Logs.i("用户协议 "+Constant.user_agreement_url);
			Intent intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("title", "用户协议");
			intent.putExtra("url", Constant.user_agreement_url);
			context.startActivity(intent);
		} else if (id == send_sms.getId()) {
			send_sms.setEnabled(false);
			String phone = phone_number.getText().toString();

			if (getPhoneValidateMessage(phone)) {
				VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
				verifyCodeProcess.setPhone(phone);
				verifyCodeProcess.post(hand);
			} else {
				send_sms.setEnabled(true);
			}

		} else if (id == phone_register.getId()) {
			String number = phone_number.getText().toString();
			String code = phone_code.getText().toString();
			String password = phone_password.getText().toString();

			if (TextUtils.isEmpty(number)) {
				Toast.makeText(context, "请输入手机号",Toast.LENGTH_LONG).show();
				return;
			}
			
			if (number.trim().length() != 11) {
				Toast.makeText(context, "账号格式不正确",Toast.LENGTH_LONG).show();
				return;
			}

			if (TextUtils.isEmpty(password)) {
				Toast.makeText(context, "请输入密码",Toast.LENGTH_LONG).show();
				return;
			}

			if (!isRegularAcc(password)) {
				Toast.makeText(context, "密码格式不正确",Toast.LENGTH_LONG).show();
				return;
			}

			if (!isRead) {
				Toast.makeText(context, "请阅读并同意用户注册协议",Toast.LENGTH_LONG).show();
				return;
			}

			RegisterPhoneProcess registerProcess = new RegisterPhoneProcess();
			registerProcess.setAccount(number);
			registerProcess.setPassword(password);
			registerProcess.setCode(code);
			registerProcess.post(handler);
		}
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

	/**
	 * 用户信息检查，获取手机验证码
	 */
	protected boolean getPhoneValidateMessage(String username) {
		String num = Constant.REGULAR_PHONENUMBER;

		if (TextUtils.isEmpty(username)) {
			Toast.makeText(context, "手机号不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if (!username.matches(num)) {
			Toast.makeText(context, "请输入正确的手机号码",Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	/**
	 * 注册用户信息检查
	 */
	public Boolean checkRegister(String account, String password, String code, boolean isRead) {
		if (TextUtils.isEmpty(account)) {
			Toast.makeText(context, "请输入手机号",Toast.LENGTH_LONG).show();
			return false;
		}
		String reg_acc = Constant.REGULAR_PHONENUMBER;
		Pattern pattern_acc = Pattern.compile(reg_acc);
		Matcher matcher_acc = pattern_acc.matcher(account);
		boolean b_acc = matcher_acc.matches();

		if (TextUtils.isEmpty(code)) {
			Toast.makeText(context, "验证码不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if (TextUtils.isEmpty(password)) {
			Toast.makeText(context, "请输入密码",Toast.LENGTH_LONG).show();
			return false;
		}

		String reg_pwd = Constant.REGULAR_ACCOUNT;
		Pattern pattern_pwd = Pattern.compile(password);
		Matcher matcher_pwd = pattern_pwd.matcher(password);
		boolean b_pwd = matcher_pwd.matches();

		if (!b_pwd) {
			Toast.makeText(context, "密码格式不正确",Toast.LENGTH_LONG).show();
			return false;
		}

		if (!isRead) {
			Toast.makeText(context, "请阅读并同意用户注册协议",Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public void setOnClick(OnClickListener onclick) {
		phone_back.setOnClickListener(onclick);
	}

	@Override
	public View getContentView() {
		return contentView;
	}
}
