package com.mini.sdk.fragment;

import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.SharedPreferenceUtil;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment implements OnClickListener {

	private FloatWebActivity activity;
	private View contentView;

	private static Handler handler;

	private TextView back;
	private TextView title;
	private ToggleButton control_fast_login;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (FloatWebActivity) getActivity();
		handler = activity.handler;
		contentView = inflater.inflate(Resources.getLayoutId(activity, "fragment_settings_layout"), container, false);

		back = (TextView) contentView.findViewById(Resources.getId(activity, "back"));
		back.setOnClickListener(this);

		title = (TextView) contentView.findViewById(Resources.getId(activity, "title"));
		title.setText("设置");

		control_fast_login = (ToggleButton) contentView.findViewById(Resources.getId(activity, "control_fast_login"));
		control_fast_login.setChecked(SharedPreferenceUtil.getFastLogin(activity));

		control_fast_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferenceUtil.setFastLogin(activity, isChecked);
			}
		});
		return contentView;
	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			handler.sendEmptyMessage(1);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		handler.sendEmptyMessage(1);
	}
}
