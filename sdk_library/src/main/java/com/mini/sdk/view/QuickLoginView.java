package com.mini.sdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.LoginActivity;
import com.mini.sdk.entity.User;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.http.process.LoginProcess;
import com.mini.sdk.listener.LoginResult;
import com.mini.sdk.utils.ComponentUtil;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;

public class QuickLoginView extends BaseView {

	public Activity activity;

	private TextView username;
	private ImageView quick_login_image;
	private Button qiuck_login_switch;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				LoginProcess loginprocess = new LoginProcess();
				loginprocess.setAccount(PluginApi.userLogin.getAccount());
				loginprocess.setPassword(PluginApi.userLogin.getPassword());
				loginprocess.post(handler);
				break;

			case 10:
				ComponentUtil.skipActivity(activity, LoginActivity.class);
				activity.finish();
				break;

			case 11:// 关闭
				activity.finish();
				break;

			case Constant.LOGIN_SUCCESS:
				UserLogin loginSuccess = (UserLogin) msg.obj;
				PluginApi.userLogin = loginSuccess;
				
				LoginResult userResult = new LoginResult();
				saveUser(loginSuccess);// 先保存在更新
				
				userResult.setCode(LoginResult.SDK_LOGIN_SUCCESS);
				userResult.setAccountId(loginSuccess.getAccountId());
				userResult.setAccount(loginSuccess.getAccount());
				userResult.setSign(loginSuccess.getSign());
				userResult.setToken(loginSuccess.getToken());
				// 登录成功后设置个标记
				PluginApi.isLogin = true;

//                FloatModel.getInstance().initFloating(activity);


				if (PluginApi.loginListener != null) {
					PluginApi.loginListener.result(userResult);
				}
				activity.finish();
				break;

			case Constant.LOGIN_FAIL:
				String message = (String) msg.obj;
				
				if (TextUtils.isEmpty(message)) {
					message = "登录失败";
				}
				
				// 登录失败后设置个标记
				PluginApi.isLogin = false;
				
				LoginResult loginFail = new LoginResult();
				loginFail.setCode(LoginResult.SDK_LOGIN_SUCCESS);
				loginFail.setMessage(message);
				
				if (PluginApi.loginListener != null) {
					PluginApi.loginListener.result(loginFail);
				}
				
				ComponentUtil.skipActivity(activity, LoginActivity.class);
				activity.finish();
				break;

			default:
				break;
			}

		};
	};

	public QuickLoginView(Activity activity_) {
		this.activity = activity_;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(Resources.getLayoutId(activity, "sdk_quick_login_view"), null);

		quick_login_image = (ImageView) contentView.findViewById(Resources.getId(activity, "quick_login_image"));
		username = (TextView) contentView.findViewById(Resources.getId(activity, "username"));
		username.setText(PluginApi.userLogin.getAccount());

		qiuck_login_switch = (Button) contentView.findViewById(Resources.getId(activity, "qiuck_login_switch"));
		qiuck_login_switch.setOnClickListener(this);

		quick_login_image.startAnimation(rotaAnimation());

		handler.sendEmptyMessageDelayed(0, 2500);
	}

	@Override
	public void onClick(View v) {
		handler.removeMessages(0);
		handler.sendEmptyMessage(10);
	}

	@Override
	public View getContentView() {
		return contentView;
	}

	public static Animation rotaAnimation() {
		RotateAnimation ra = new RotateAnimation(0, 355, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setInterpolator(new LinearInterpolator());
		ra.setDuration(888);
		ra.setRepeatCount(-1);
		ra.setStartOffset(0);
		ra.setRepeatMode(Animation.RESTART);
		return ra;
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
		SharedPreferenceUtil.saveUser(activity, user);
	}

}
