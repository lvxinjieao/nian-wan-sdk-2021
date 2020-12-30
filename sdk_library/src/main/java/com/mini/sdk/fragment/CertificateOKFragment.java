package com.mini.sdk.fragment;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.utils.Resources;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CertificateOKFragment extends Fragment {

	public View contentView;
	public FloatWebActivity activity;
	public static Handler handler;

	private TextView back;
	private TextView title;

	private TextView real_name;
	private TextView id_card;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = (FloatWebActivity) getActivity();
		handler = activity.handler;
		contentView = inflater.inflate(Resources.getLayoutId(activity, "fragment_certificate_ok_layout"), null);
		
		title = (TextView) contentView.findViewById(Resources.getId(activity, "title"));
		title.setText("已认证");
		
		back = (TextView) contentView.findViewById(Resources.getId(activity, "back"));
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(1);
			}
		});

		real_name = (TextView) contentView.findViewById(Resources.getId(activity, "real_name"));
		real_name.setText(PluginApi.userLogin.getReal_name());
		
		id_card = (TextView) contentView.findViewById(Resources.getId(activity, "id_card"));

		String idCard = PluginApi.userLogin.getIdcard();
		if (!TextUtils.isEmpty(idCard) && idCard.length() > 14) {
			StringBuffer stringBuffer = new StringBuffer(idCard);
			String xing = "";
			for (int i = 1; i < id_card.length() - 1; i++) {
				xing = xing + "*";
			}
			stringBuffer.replace(1, idCard.length() - 1, xing);
			id_card.setText(stringBuffer.toString());
		}
		return contentView;
	}
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			handler.sendEmptyMessage(1);
		}
		return true;
	}

}
