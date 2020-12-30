package com.mini.sdk.activity;

import java.util.HashMap;
import java.util.Map;

import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.WXOrderInfo;
import com.mini.sdk.http.process.WXPayResultProgress;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WapPayActivity extends Activity {

	private WebView webview;
	private WXOrderInfo wxOrderInfo;
	private boolean isFirst = true;

	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.GET_WX_PAY_RESULT_SUCCESS:
				PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "微信支付成功");
				finish();
				break;

			case Constant.GET_WX_PAY_RESULT_FAIL:
				PluginApi.payListener.result(PayListener.SDK_PAY_FAILED, "微信支付失败");
				finish();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Resources.getLayoutId(this, "activity_sdk_webview"));
		PluginApi.flag = false;
		wxOrderInfo = (WXOrderInfo) getIntent().getSerializableExtra("WXOrderInfo");
		webview = (WebView) findViewById(Resources.getId(this, "webview"));
		initWebView();
	}

	private void initWebView() {
		
		if (wxOrderInfo == null) {
			Logs.e("wxOrderInfo is null!");
			return;
		}
		
		webview.getSettings().setUseWideViewPort(true); // 自适应屏幕
		webview.getSettings().setAppCacheEnabled(true);	// 设置缓存
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview.setHorizontalScrollBarEnabled(false);
		webview.setVerticalScrollBarEnabled(false);
		webview.setScrollbarFadingEnabled(true);
		webview.getSettings().setJavaScriptEnabled(true);// 支持js交互
		webview.addJavascriptInterface(new JsInterface(), "mengchuang"); // js方法
		
		webview.setWebChromeClient(new WebChromeClient());
		
		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("weixin://wap/pay?")) { // 在非微信内部WebView的H5页面中调出微信支付
					try {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivity(intent);
					} catch (Exception e) {
						Logs.e(e.toString());
					}
					return true;
				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}
			}
			
			
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				 handler.proceed();
			}
		};
		
		webview.setWebViewClient(webViewClient);
		
		if (wxOrderInfo.getTag().equals("wx")) {
			Map<String, String> extraHeaders = new HashMap<String, String>();
			extraHeaders.put("Referer", wxOrderInfo.getCal_url());
			webview.loadUrl(wxOrderInfo.getUrl(), extraHeaders);
		} else {
			webview.loadUrl(wxOrderInfo.getUrl());
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (wxOrderInfo != null) {
			if (!isFirst && wxOrderInfo.getTag().equals("wx")) {
				WXPayResultProgress progress = new WXPayResultProgress();
				progress.setOrderNo(wxOrderInfo.getOrderNo());
				progress.post(handler);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isFirst = false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PluginApi.flag = true;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
	class JsInterface {

		@JavascriptInterface
		public void getPayResult(String result) {	// js交互，通知支付结果并关闭页面
			Logs.i("result: " + result);
			if (result.equals("succeed")) {
				PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "微信支付成功");
			} else {
				PluginApi.payListener.result(PayListener.SDK_PAY_FAILED, "微信支付失败");
			}
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		} else {
			handler.sendEmptyMessage(Constant.GET_WX_PAY_RESULT_FAIL);
		}
		return super.onKeyDown(keyCode, event);
	}
}
