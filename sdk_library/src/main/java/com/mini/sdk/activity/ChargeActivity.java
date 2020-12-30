package com.mini.sdk.activity;

import com.mini.sdk.PluginApi;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.view.ChargeView;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class ChargeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChargeView chargeView = new ChargeView(this);
		setContentView(chargeView.getContentView());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "支付取消");
		}
		return super.onKeyDown(keyCode, event);
	}
}
