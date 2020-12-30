package com.mini.sdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mini.sdk.utils.Resources;

public class WebViewActivity extends Activity {

    private WebView webView;
    private TextView back;
    private TextView title;

    private String str_title;
    private String str_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str_title = getIntent().getStringExtra("title");
        str_url = getIntent().getStringExtra("url");
        setContentView(Resources.getLayoutId(this, "activity_web_view"));
        initView();
    }

    private void initView() {
        back = (TextView) findViewById(Resources.getId(WebViewActivity.this, "back"));
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });

        title = (TextView) findViewById(Resources.getId(WebViewActivity.this, "title"));
        title.setText(str_title);

        webView = (WebView) findViewById(Resources.getId(this, "webView"));
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小


        webView.loadUrl(str_url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
