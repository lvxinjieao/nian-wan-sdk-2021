package com.mini.sdk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.http.process.CertificateProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

public class CertificateNoFragment extends Fragment {

	private View contentView;
	private FloatWebActivity activity;
	public static Handler handler;
	
	private TextView back;
	private TextView title;
	
	private ScrollView attestation;
	private LinearLayout attestation_ok;
	
	private EditText editName, editID;// 姓名 身份证号码
	private Button mBtn;//
	private String mNameStr, idStr;// 姓名 身份证号码
	private LinearLayout mWrong;
	private TextView textWrong;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(Resources.getLayoutId(getActivity(), "fragment_certificate_no_layout"), null);
		activity = (FloatWebActivity) getActivity();
		handler = activity.handler;
		initview(contentView);
		return contentView;
	}

	public void initview(View inflate) {
		back = (TextView) inflate.findViewById(Resources.getId(activity, "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(1);
			}
		});
		
		title = (TextView) inflate.findViewById(Resources.getId(activity, "title"));
		title.setText("实名认证");
		
		attestation = (ScrollView) inflate.findViewById(Resources.getId(activity, "attestation"));
		attestation_ok = (LinearLayout) inflate.findViewById(Resources.getId(activity, "attestation_ok"));
		
		editName = (EditText) inflate.findViewById(Resources.getId(activity, "mch_et_toCertificate_name"));
		mWrong = (LinearLayout) inflate.findViewById(Resources.getId(activity, "ll_certificate_wrong"));
		textWrong = (TextView) inflate.findViewById(Resources.getId(activity, "text_certificate_wrong"));
		editName.addTextChangedListener(textWatcher);
		editID = (EditText) inflate.findViewById(Resources.getId(activity, "mch_et_toCertificate_card"));
		editID.addTextChangedListener(textWatcher);
		mBtn = (Button) inflate.findViewById(Resources.getId(activity, "mch_attestation_button"));
		mBtn.setOnClickListener(attestationoClick);
	}

	/**
	 * 请求返回
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.TO_CERTIFICATE_SUCCESS:
				Logs.e((String) msg.obj);
				String idcard = (String) msg.obj;
				PluginApi.userLogin.setIdcard(idcard);
				
				attestation.setVisibility(View.GONE);
				attestation_ok.setVisibility(View.VISIBLE);
				break;
			case Constant.TO_CERTIFICATE_FAIL:
				mWrong.setVisibility(View.VISIBLE);
				textWrong.setText((String) msg.obj);
				break;
			}
		}
	};
	/**
	 * 去认证
	 */
	OnClickListener attestationoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isTure()) {
				goPostReal();
			}
		}
	};

	/**
	 * 请求实名认证
	 */
	private void goPostReal() {
		CertificateProcess process = new CertificateProcess();
		process.setIdcard(idStr);
		process.setReal_name(mNameStr);
		process.post(mHandler);
	}

	/**
	 * 验证姓名 身份证号是否正确
	 *
	 * @return
	 */
	private boolean isTure() {
		mNameStr = editName.getText().toString();
		idStr = editID.getText().toString().trim();
		if (TextUtils.isEmpty(mNameStr)) {
			Toast.makeText(activity, "请输入姓名", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!mNameStr.matches(Constant.REGULAR_NAME)) {
			Toast.makeText(activity, "请输入正确的姓名", Toast.LENGTH_LONG).show();
			return false;
		}
		if (TextUtils.isEmpty(idStr)) {
			Toast.makeText(activity, "请输入身份证号", Toast.LENGTH_LONG).show();
			return false;
		}
		if (idStr.length() == 15) {
			if (!idStr.matches(Constant.REGEX_ID_CARD15)) {
				Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		} else if (idStr.length() == 18) {
			if (!idStr.matches(Constant.REGEX_ID_CARD18)) {
				Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		} else {
			Toast.makeText(activity, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mWrong.setVisibility(View.INVISIBLE);
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			handler.sendEmptyMessage(1);
		}
		return true;
	}
	
}
