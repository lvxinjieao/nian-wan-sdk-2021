package com.mini.sdk.fragment;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.entity.User;
import com.mini.sdk.http.process.ChangePasswordProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HEtUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;

import java.util.LinkedList;

/**
 * 修改密码
 */
public class ChangePasswordFragment extends Fragment {

	public FloatWebActivity activity;
	public View contentView;
	public static Handler handler;

	public String senAcc;
	public String senPwd;

	EditText oldPwd;
	EditText newPwd;
	EditText subPwd;
	Button btnSub;

	String oldPwd_;
	String newPwd_;
	String subPwd_;
	String cheNum_;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (FloatWebActivity) getActivity();
		handler = activity.handler;
		contentView = inflater.inflate(Resources.getLayoutId(activity, "activity_change_password"), container, false);

		TextView title = (TextView) contentView.findViewById(Resources.getId(activity, "title"));
		title.setText("修改密码");

		TextView back = (TextView) contentView.findViewById(Resources.getId(activity, "back"));
		back.setOnClickListener(backClick);

		User user = SharedPreferenceUtil.getUsers(activity).getFirst();
		if (user != null) {
			senAcc = user.getAccount();
			senPwd = user.getPassword();
		}

		oldPwd = (EditText) contentView.findViewById(Resources.getId(activity, "edt_oldPwd"));
		newPwd = (EditText) contentView.findViewById(Resources.getId(activity, "edt_newPwd"));
		subPwd = (EditText) contentView.findViewById(Resources.getId(activity, "edt_subPwd"));

		btnSub = (Button) contentView.findViewById(Resources.getId(activity, "btnSub"));

		RelativeLayout rlOldPwdEye = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_oldPwd_eye"));
		RelativeLayout rlOldPwdClear = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_oldPwd_clear"));
		ImageView ivOldPwdEye = (ImageView) contentView.findViewById(Resources.getId(activity, "iv_mch_oldPwd_eye"));

		RelativeLayout rlNewPwdEye = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_newPwd_eye"));
		RelativeLayout rlNewPwdClear = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_newPwd_clear"));
		ImageView ivNewPwdEye = (ImageView) contentView.findViewById(Resources.getId(activity, "iv_mch_newPwd_eye"));

		RelativeLayout rlRePwdEye = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_rePwd_eye"));
		RelativeLayout rlRePwdClear = (RelativeLayout) contentView
				.findViewById(Resources.getId(activity, "rl_mch_rePwd_clear"));
		ImageView ivRePwdEye = (ImageView) contentView.findViewById(Resources.getId(activity, "iv_mch_rePwd_eye"));

		new HEtUtils().etHandle(activity, oldPwd, rlOldPwdClear, rlOldPwdEye, ivOldPwdEye);
		new HEtUtils().etHandle(activity, newPwd, rlNewPwdClear, rlNewPwdEye, ivNewPwdEye);
		new HEtUtils().etHandle(activity, subPwd, rlRePwdClear, rlRePwdEye, ivRePwdEye);

		btnSub.setOnClickListener(subListener);
		return contentView;
	}

	OnClickListener backClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			handler.sendEmptyMessage(1);
		}
	};

	OnClickListener subListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			oldPwd_ = oldPwd.getText().toString();
			newPwd_ = newPwd.getText().toString();
			subPwd_ = subPwd.getText().toString();
			if (TextUtils.isEmpty(senAcc)) {
				Toast.makeText(activity, "未登录", Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(oldPwd_)) {
				Toast.makeText(activity, "原始密码不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			if (!(oldPwd_.equals(senPwd))) {
				Toast.makeText(activity, "原始密码不正确", Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(newPwd_)) {
				Toast.makeText(activity, "新密码不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			if (!newPwd_.matches(Constant.REGULAR_ACCOUNT)) {
				Toast.makeText(activity, "新密码格式不正确", Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(subPwd_)) {
				Toast.makeText(activity, "请再次输入密码", Toast.LENGTH_LONG).show();
				return;
			}

			if (!(newPwd_.equals(subPwd_))) {
				Toast.makeText(activity, "新密码与确认密码不一致", Toast.LENGTH_LONG).show();
				return;
			}

			if (oldPwd_.equals(newPwd_)) {
				Toast.makeText(activity, "新密码与原始密码不能相同", Toast.LENGTH_LONG).show();
				return;
			}

			changePwd();
		}
	};

	// 出发更改密码请求方法
	private void changePwd() {
		ChangePasswordProcess changePwdProcess = new ChangePasswordProcess();
		changePwdProcess.setPwd(oldPwd_);
		changePwdProcess.setRepwd(subPwd_);
		changePwdProcess.setCode("pwd");
		changePwdProcess.post(myHandler);
	}

	/**
	 * 请求后根据结果执行操作
	 */
	@SuppressLint("HandlerLeak")
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case Constant.MODIFY_PASSWORD_SUCCESS:// 密码修改成功
				Logs.e("密码修改成功");
				PluginApi.userLogin.setPassword(newPwd_);
				LinkedList<User> list = SharedPreferenceUtil.getUsers(activity);

				if (null != list && list.size() != 0) {
					User user;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getAccount().equals(PluginApi.userLogin.getAccount())) {
							user = list.get(i);
							user.setPassword(newPwd_);
							SharedPreferenceUtil.saveUser(activity, user);
						}
					}
				}
				Toast.makeText(activity, "密码修改成功", Toast.LENGTH_LONG).show();
				activity.finish();
				break;

			case Constant.MODIFY_PASSWORD_FAIL:// 密码修改失败
				String tip = (String) msg.obj;
				if (TextUtils.isEmpty(tip)) {
					tip = "密码修改失败";
				}
				Toast.makeText(activity, tip, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			handler.sendEmptyMessage(1);
		}
		return true;
	}
}
