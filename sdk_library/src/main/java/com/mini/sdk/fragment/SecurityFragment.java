package com.mini.sdk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.bean.ServiceModel;
import com.mini.sdk.bean.VerifyCode;
import com.mini.sdk.http.process.UserBindPhoneProcess;
import com.mini.sdk.http.process.UserUnBindPhoneProcess;
import com.mini.sdk.http.process.VerificationCodeProcess;
import com.mini.sdk.observer.SecondsWatcher;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;
import com.mini.sdk.view.util.CheckImage;
import com.mini.sdk.view.util.TimeFactory;

public class SecurityFragment extends Fragment {

	public FloatWebActivity activity;
	public static Handler handler;
	public View contentView;

	CheckImage checkImage;
	String checkNumber;// 验证码
	// 上面三个数字
	TextView step1, step_verifyidentity, step_securityphone;
	// 上面三个步骤
	TextView step1_, step_verifyidentity_, step_securityphone_;
	// 步骤间的横线
	View lineFirst, lineSecond;
	// 绑定手机第一个界面
	RelativeLayout bindphone_body_setup1;
	// 绑定手机第二个界面
	LinearLayout bindphone_body_setup2;
	// 绑定手机成功界面
	LinearLayout bindphone_body_setsuccess;
	// 解绑手机按钮和输入手机验证码的输入框
	LinearLayout mch_ll_unbindphone;
	LinearLayout ll_mch_bindphone_top;
	Button btn_unbindstep1_cancel;
	Button btn_unbindstep1_sure;
	LinearLayout ll_mch_unbindstep1;
	// step1------------------------------------------
	// 密码输入框
	LinearLayout ll_mch_unbindstep2;
	EditText edt_bindphone;
	// 确定定按钮
	Button btn_bindphone1;
	LinearLayout ll_mch_unbindstep3;
	TextView tv_mch_unbindstep3_success;
	Button btn_unbindstep3_return;
	// step2------------------------------------------
	// 手机号码输入框
	EditText step2_edt_phone;
	// 验证码输入框
	EditText step2_edt_checknum;
	// 验证码按钮
	Button step2_btn_checknum;
	// 上一步
	Button step2_btn_back;
	// 下一步
	Button step2_btn_next;
	// 联系客服
	TextView step2_tv_service;
	// step3----------------------------------------
	// 已绑定手机号
	TextView txt_bindphone_tip;
	// 已绑定手机号输入框
	EditText edt_unbindphone;
	Button btn_unbindstep2_previous;
	Button btn_unbindstep2_unbind;

	TextView text_title;

	String edit_bindphoneStr;

	private int step;
	private int isUnBind = 0;
	TextView text_back;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (FloatWebActivity) getActivity();
		handler = activity.handler;
		contentView = inflater.inflate(Resources.getLayoutId(activity, "activity_bind_phone"), container, false);
		initView();
		showInit();
		return contentView;
	}

	private void initView() {
		TextView title = (TextView) contentView.findViewById(Resources.getId(activity, "title"));
		title.setText("安全中心");

		TextView back = (TextView) contentView.findViewById(Resources.getId(activity, "back"));
		back.setOnClickListener(backClick);

		// 最上面步骤显示
		step1 = (TextView) contentView.findViewById(Resources.getId(activity, "step1"));
		step_verifyidentity = (TextView) contentView.findViewById(Resources.getId(activity, "step_verifyidentity"));
		step_securityphone = (TextView) contentView.findViewById(Resources.getId(activity, "step_securityphone"));
		step1_ = (TextView) contentView.findViewById(Resources.getId(activity, "step1_"));
		step_verifyidentity_ = (TextView) contentView.findViewById(Resources.getId(activity, "step_verifyidentity_"));
		step_securityphone_ = (TextView) contentView.findViewById(Resources.getId(activity, "step_securityphone_"));
		lineFirst = (View) contentView.findViewById(Resources.getId(activity, "line_bindphone_1"));
		lineSecond = (View) contentView.findViewById(Resources.getId(activity, "line_bindphone_2"));

		// 绑定手机第一个界面
		bindphone_body_setup1 = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "bindphone_body_setup1"));
		// 绑定手机第二个界面
		bindphone_body_setup2 = (LinearLayout) contentView
				.findViewById(Resources.getId(activity, "bindphone_body_setup2"));
		// 绑定手机成功界面
		bindphone_body_setsuccess = (LinearLayout) contentView
				.findViewById(Resources.getId(activity, "bindphone_body_setsuccess"));
		// 解除绑定界面
		mch_ll_unbindphone = (LinearLayout) contentView.findViewById(Resources.getId(activity, "mch_ll_unbindphone"));
		ll_mch_bindphone_top = (LinearLayout) contentView
				.findViewById(Resources.getId(activity, "ll_mch_bindphone_top"));
		ll_mch_unbindstep1 = (LinearLayout) contentView.findViewById(Resources.getId(activity, "ll_mch_unbindstep1"));
		ll_mch_unbindstep2 = (LinearLayout) contentView.findViewById(Resources.getId(activity, "ll_mch_unbindstep2"));
		ll_mch_unbindstep3 = (LinearLayout) contentView.findViewById(Resources.getId(activity, "ll_mch_unbindstep3"));
		tv_mch_unbindstep3_success = (TextView) contentView
				.findViewById(Resources.getId(activity, "tv_mch_unbindstep3_success"));
		btn_unbindstep3_return = (Button) contentView.findViewById(Resources.getId(activity, "btn_unbindstep3_return"));
		btn_unbindstep3_return.setOnClickListener(clickListener);
		// step1------------------------------------------
		btn_unbindstep1_cancel = (Button) contentView.findViewById(Resources.getId(activity, "btn_unbindstep1_cancel"));
		btn_unbindstep1_sure = (Button) contentView.findViewById(Resources.getId(activity, "btn_unbindstep1_sure"));
		btn_unbindstep1_cancel.setOnClickListener(clickListener);
		btn_unbindstep1_sure.setOnClickListener(clickListener);
		// 密码输入框
		edt_bindphone = (EditText) contentView.findViewById(Resources.getId(activity, "edt_bindphone"));
		// 确定定按钮
		btn_bindphone1 = (Button) contentView.findViewById(Resources.getId(activity, "btn_bindphone1"));
		btn_bindphone1.setOnClickListener(clickListener);
		// step2------------------------------------------
		// 手机号码输入框
		step2_edt_phone = (EditText) contentView.findViewById(Resources.getId(activity, "step2_edt_phone"));
		// 验证码输入框
		step2_edt_checknum = (EditText) contentView.findViewById(Resources.getId(activity, "step2_edt_checknum"));
		// 验证码按钮
		step2_btn_checknum = (Button) contentView.findViewById(Resources.getId(activity, "step2_btn_checknum"));
		step2_btn_checknum.setOnClickListener(clickListener);
		// 上一步
		step2_btn_back = (Button) contentView.findViewById(Resources.getId(activity, "step2_btn_back"));
		step2_btn_back.setOnClickListener(clickListener);
		// 下一步

		step2_btn_next = (Button) contentView.findViewById(Resources.getId(activity, "step2_btn_next"));
		step2_btn_next.setOnClickListener(clickListener);

		step2_tv_service = (TextView) contentView.findViewById(Resources.getId(activity, "step2_tv_service"));
		step2_tv_service.setOnClickListener(clickListener);
		// step3----------------------------------------

		txt_bindphone_tip = (TextView) contentView.findViewById(Resources.getId(activity, "txt_bindphone_tip"));

		step_verifyidentity = (TextView) contentView.findViewById(Resources.getId(activity, "step_verifyidentity"));

		step_securityphone = (TextView) contentView.findViewById(Resources.getId(activity, "step_securityphone"));
		step_securityphone.setBackgroundResource(Resources.getDrawableId(activity, "mch_step3_80x80_grey"));
		step_verifyidentity_ = (TextView) contentView.findViewById(Resources.getId(activity, "step_verifyidentity_"));
		step_verifyidentity_.setTextColor(Color.parseColor("#a0a0a0"));
		step_securityphone_ = (TextView) contentView.findViewById(Resources.getId(activity, "step_securityphone_"));
		step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));
		text_title = (TextView) contentView.findViewById(Resources.getId(activity, "text_title"));

		edt_unbindphone = (EditText) contentView.findViewById(Resources.getId(activity, "edt_unbindphone"));
		btn_unbindstep2_previous = (Button) contentView
				.findViewById(Resources.getId(activity, "btn_unbindstep2_previous"));
		btn_unbindstep2_unbind = (Button) contentView.findViewById(Resources.getId(activity, "btn_unbindstep2_unbind"));
		btn_unbindstep2_previous.setOnClickListener(clickListener);
		btn_unbindstep2_unbind.setOnClickListener(clickListener);

		text_back = (TextView) contentView.findViewById(Resources.getId(activity, "text_back"));
		text_back.setOnClickListener(bindphonelinstener);
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			/** 解除绑定 */

			if (view.getId() == btn_unbindstep1_cancel.getId()) {
				activity.finish();
				return;
			}
			if (view.getId() == btn_unbindstep1_sure.getId()) {

				TimeFactory.creator(3).getTimeChange().addWatcher(unBindsecondsWatcher);
				TimeFactory.creator(2).getTimeChange().removeWatcher(secondsWatcher);
				String phone = PluginApi.userLogin.getPhoneNumber();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(activity, "该账号没有绑定手机号码", Toast.LENGTH_LONG).show();
					return;
				}
				String valCode = Constant.REGULAR_PHONENUMBER;
				if (!phone.matches(valCode)) {
					Toast.makeText(activity, "该账号绑定的手机号码无法被识别", Toast.LENGTH_LONG).show();
					return;
				}
				VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
				verifyCodeProcess.setPhone(phone);
				verifyCodeProcess.post(UnBindHandler);
				return;
			}

			if (view.getId() == btn_unbindstep2_previous.getId()) {
				ll_mch_unbindstep1.setVisibility(View.VISIBLE);
				ll_mch_unbindstep2.setVisibility(View.GONE);
				return;
			}
			if (view.getId() == btn_unbindstep2_unbind.getId()) {
				unbind();
				return;
			}
			if (view.getId() == btn_unbindstep3_return.getId()) {
				activity.finish();
				return;
			}
			/** 控制解绑显示隐藏 */
			if (view.getId() == txt_bindphone_tip.getId()) {
				if (isUnBind == 1) {
					title1();
					isUnBind = 0;
				} else if (isUnBind == 0) {
					title2();
					isUnBind = 1;
				}
				return;
			}
			/** 绑定手机验证密码 */
			if (view.getId() == btn_bindphone1.getId()) {
				bindstep1();
				return;
			}
			/** 绑定手机发验证码 */
			if (view.getId() == step2_btn_checknum.getId()) {
				bindstep2num();
				return;
			}
			/** 绑定手机上一步 */
			if (view.getId() == step2_btn_back.getId()) {
				bodystep1();
				return;
			}
			/** 绑定手机下一步 */
			if (view.getId() == step2_btn_next.getId()) {
				bindstep2();
				return;
			}

			if (view.getId() == step2_tv_service.getId()) {
				ServiceModel.getInstance().contactService(activity, false);
				return;
			}
		}
	};

	/**
	 * 绑定手机发送验证码
	 */
	private void bindstep2num() {
		TimeFactory.creator(2).getTimeChange().addWatcher(secondsWatcher);
		TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);
		step2_btn_checknum.setEnabled(true);
		step2_btn_checknum.setTextColor(Color.parseColor("#19B1EA"));
		String phone = step2_edt_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(activity, "请先输入手机号码", Toast.LENGTH_LONG).show();
			return;
		}
		String valCode = Constant.REGULAR_PHONENUMBER;
		if (!phone.matches(valCode)) {
			Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
			return;
		}
		step2_btn_checknum.setTextColor(Color.parseColor("#19B1EA"));
		step2_btn_checknum.setEnabled(false);
		VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
		verifyCodeProcess.setPhone(phone);
		verifyCodeProcess.setType("1");
		verifyCodeProcess.post(myHandler);
	}

	/**
	 * 绑定手机第一步
	 */
	private void bindstep1() {
		String password = edt_bindphone.getText().toString().trim();
		
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(activity, "请输入登录密码", Toast.LENGTH_LONG).show();
			return;
		}

		String realpassword = SharedPreferenceUtil.getUsers(activity).getFirst().getPassword();

		if (!password.equals(realpassword)) {
			Toast.makeText(activity, "登录密码输入不正确", Toast.LENGTH_LONG).show();
			return;
		}
		
		bodystep2();
	}

	/**
	 * 绑定手机
	 */
	private void bindstep2() {
		String phone = step2_edt_phone.getText().toString().trim();
		String code = step2_edt_checknum.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(activity, "请输入手机号", Toast.LENGTH_LONG).show();
			return;
		}
		String valCode = Constant.REGULAR_PHONENUMBER;
		if (!phone.matches(valCode)) {
			Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(activity, "请输入验证码", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			UserBindPhoneProcess process = new UserBindPhoneProcess();
			process.setPhone(phone);
			process.setCode("phone");
			process.setSmcode(code);
			process.post(myHandler);
		} catch (Exception e) {
			Toast.makeText(activity, "程序异常", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return;
	}

	OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			handler.sendEmptyMessage(1);
		}
	};

	private void showInit() {
		step = 1;
		bodystep1();
		String phone = PluginApi.userLogin.getPhoneNumber();

		/**
		 * 没有绑定手机
		 */
		if (TextUtils.isEmpty(phone)) {
			bodystep1();
		}
		/**
		 * 已经绑定手机btn_bindphone
		 */
		if (!TextUtils.isEmpty(phone) && phone.length() >= 11) {
			bodystepunbind1();
			StringBuilder tip = new StringBuilder();
			tip.append("已绑定手机号：").append(phone.substring(0, 4)).append("****").append(phone.substring(7));
			txt_bindphone_tip.setText(tip);
			txt_bindphone_tip.setOnClickListener(clickListener);
			return;
		}
	}

	OnClickListener bindphonelinstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			/**
			 * 返回
			 */
			if (v.getId() == Resources.getId(activity, "text_back")) {
				handler.sendEmptyMessage(1);
				return;
			}

			/** 绑定手机读秒按钮 */
			if (v.getId() == Resources.getId(activity, "msg_mm")) {
				TimeFactory.creator(2).getTimeChange().addWatcher(secondsWatcher);
				TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);

				String phone = edt_bindphone.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(activity, "请先输入手机号码", Toast.LENGTH_LONG).show();
					return;
				}
				String valCode = Constant.REGULAR_PHONENUMBER;
				if (!phone.matches(valCode)) {
					Toast.makeText(activity, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
					return;
				}
				VerificationCodeProcess verifyCodeProcess = new VerificationCodeProcess();
				verifyCodeProcess.setPhone(phone);
				verifyCodeProcess.post(myHandler);
				return;
			}
		}
	};

	Handler secondsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null != step2_btn_checknum) {
				String seconds = (String) msg.obj;
				if ("0".equals(seconds)) {
					step2_btn_checknum.setText("获取验证码");
					step2_btn_checknum.setEnabled(true);
					step2_btn_checknum.setTextColor(Color.parseColor("#99cc33"));
				} else {
					step2_btn_checknum.setText((String) msg.obj);
					step2_btn_checknum.setTextColor(Color.parseColor("#a0a0a0"));
					step2_btn_checknum.setEnabled(false);
				}
			}
		}
	};
	Handler UnBindSecondsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		TimeFactory.creator(3).getTimeChange().addWatcher(unBindsecondsWatcher);
	}

	@Override
	public void onPause() {
		super.onPause();
		TimeFactory.creator(2).getTimeChange().removeWatcher(secondsWatcher);
		TimeFactory.creator(3).getTimeChange().removeWatcher(unBindsecondsWatcher);
	}

	SecondsWatcher secondsWatcher = new SecondsWatcher() {

		@Override
		public void updateSeconds(String seconds) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = seconds;
			secondsHandler.sendMessage(msg);
		}
	};
	SecondsWatcher unBindsecondsWatcher = new SecondsWatcher() {

		@Override
		public void updateSeconds(String seconds) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = seconds;
			UnBindSecondsHandler.sendMessage(msg);
		}
	};

	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
				step2_btn_checknum.setTextColor(Color.parseColor("#a0a0a0"));
				step2_btn_checknum.setEnabled(false);
				VerifyCode code = (VerifyCode) msg.obj;
				showCode(code);
				break;
			case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
				String res = (String) msg.obj;
				Toast.makeText(activity, res, Toast.LENGTH_LONG).show();
				step2_btn_checknum.setText("获取验证码");
				step2_btn_checknum.setEnabled(true);
				step2_btn_checknum.setTextColor(Color.parseColor("#99cc33"));
				break;
			case Constant.USER_BIND_PHONE_SUCCESS:// 绑定手机成功
				Toast.makeText(activity, "绑定成功", Toast.LENGTH_LONG).show();
				TimeFactory.creator(2).calcel();
				shouBindSuccess();
				break;
			case Constant.USER_BIND_PHONE_FAIL:// 绑定手机失败
				String message = (String) msg.obj;
				if (!TextUtils.isEmpty(message)) {
					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				}

				break;
			default:
				break;
			}
		}
	};
	public Handler UnBindHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.VERIFYCODE_REQUEST_SUCCESS:// 请求获得手机验证码成功
				ll_mch_unbindstep2.setVisibility(View.VISIBLE);
				mch_ll_unbindphone.setVisibility(View.VISIBLE);
				ll_mch_unbindstep1.setVisibility(View.GONE);
				VerifyCode code = (VerifyCode) msg.obj;
				showUnBindCode(code);
				break;
			case Constant.VERIFYCODE_REQUEST_FAIL:// 请求获得手机验证码失败
				String res = (String) msg.obj;
				Toast.makeText(activity, res, Toast.LENGTH_LONG).show();
				break;
			case Constant.USER_BIND_PHONE_SUCCESS:// 解绑手机成功
				mch_ll_unbindphone.setVisibility(View.VISIBLE);
				ll_mch_unbindstep3.setVisibility(View.VISIBLE);
				ll_mch_unbindstep2.setVisibility(View.GONE);
				ll_mch_unbindstep1.setVisibility(View.GONE);
				ll_mch_bindphone_top.setVisibility(View.GONE);
				txt_bindphone_tip.setVisibility(View.GONE);
				tv_mch_unbindstep3_success.setText("手机号：" + PluginApi.userLogin.getPhoneNumber() + "解绑成功！");
				Toast.makeText(activity, "解除手机绑定成功", Toast.LENGTH_LONG).show();
				break;
			case Constant.USER_BIND_PHONE_FAIL:// 解绑手机失败
				String message = msg.obj.toString();
				if (!TextUtils.isEmpty(message)) {
					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

	protected void shouBindSuccess() {
		PluginApi.userLogin.setPhoneNumber(edt_bindphone.getText().toString().trim());
		bodystep3();
	}

	void showCode(VerifyCode code) {
		TimeFactory.creator(2).Start();
		Toast.makeText(activity, "验证码发送成功,请查收", Toast.LENGTH_LONG).show();
	}

	void showUnBindCode(VerifyCode code) {
		TimeFactory.creator(3).Start();
		Toast.makeText(activity, "验证码发送成功,请查收", Toast.LENGTH_LONG).show();
		edt_unbindphone.setText("");
	}

	/**
	 * 绑定手机第一步
	 */
	private void bodystep1() {
		title1();
		hidestep();
		bindphone_body_setup1.setVisibility(View.VISIBLE);
		step1.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step1_select"));
		step1_.setTextColor(Color.parseColor("#99cc33"));
		lineFirst.setBackgroundColor(Color.parseColor("#99cc33"));
	}

	/**
	 * 绑定手机第二步
	 */
	private void bodystep2() {
		title2();
		hidestep();
		bindphone_body_setup2.setVisibility(View.VISIBLE);
		step_verifyidentity.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step2_select"));
		step_verifyidentity_.setTextColor(Color.parseColor("#99cc33"));
		lineSecond.setBackgroundColor(Color.parseColor("#99cc33"));
	}

	/**
	 * 绑定手机第三步（成功界面）
	 */
	private void bodystep3() {
		title3();
		hidestep();
		bindphone_body_setsuccess.setVisibility(View.VISIBLE);
	}

	/**
	 * 解除绑定
	 */
	private void bodystepunbind1() {
		title1();
		hidestep();
		// 设置解绑界面显示
		mch_ll_unbindphone.setVisibility(View.VISIBLE);
		ll_mch_bindphone_top.setVisibility(View.GONE);
	}

	/**
	 * 设置所有步骤不显示
	 */
	private void hidestep() {
		bindphone_body_setup1.setVisibility(View.GONE);
		bindphone_body_setup2.setVisibility(View.GONE);
		bindphone_body_setsuccess.setVisibility(View.GONE);
		ll_mch_bindphone_top.setVisibility(View.VISIBLE);
	}

	/**
	 * 第一步显示
	 */
	private void title1() {
		step1.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step1_select"));
		step_verifyidentity.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step2_unselect"));
		step_securityphone.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step3_unselect"));
		step1_.setTextColor(Color.parseColor("#99cc33"));
		step_verifyidentity_.setTextColor(Color.parseColor("#a0a0a0"));
		step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));
		lineFirst.setBackgroundColor(Color.parseColor("#99cc33"));
		lineSecond.setBackgroundColor(Color.parseColor("#a0a0a0"));
	}

	private void title2() {
		step1.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step1_select"));
		step_verifyidentity.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step2_select"));
		step_securityphone.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step3_unselect"));
		step1_.setTextColor(Color.parseColor("#99cc33"));
		step_verifyidentity_.setTextColor(Color.parseColor("#99cc33"));
		step_securityphone_.setTextColor(Color.parseColor("#a0a0a0"));
		lineFirst.setBackgroundColor(Color.parseColor("#99cc33"));
		lineSecond.setBackgroundColor(Color.parseColor("#99cc33"));
	}

	private void title3() {
		step1.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step1_select"));
		step_verifyidentity.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step2_select"));
		step_securityphone.setBackgroundResource(Resources.getDrawableId(activity, "sdk_step3_select"));
		step1_.setTextColor(Color.parseColor("#99cc33"));
		step_verifyidentity_.setTextColor(Color.parseColor("#99cc33"));
		step_securityphone_.setTextColor(Color.parseColor("#99cc33"));
		lineFirst.setBackgroundColor(Color.parseColor("#99cc33"));
		lineSecond.setBackgroundColor(Color.parseColor("#99cc33"));
	}

	/**
	 * 执行解除绑定操作
	 */
	private void unbind() {
		String code = edt_unbindphone.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(activity, "请输入短信验证码", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			UserUnBindPhoneProcess process = new UserUnBindPhoneProcess();
			process.setCode(code);
			process.post(UnBindHandler);
		} catch (Exception e) {
			Toast.makeText(activity, "程序异常", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return;
	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			handler.sendEmptyMessage(1);
		}
		return false;
	}
}
