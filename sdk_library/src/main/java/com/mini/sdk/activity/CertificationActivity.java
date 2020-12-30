package com.mini.sdk.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.mini.sdk.utils.DensityUtil;
import com.mini.sdk.view.CertificationView;

public class CertificationActivity extends Activity {


    public CertificationView certificationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        certificationView = new CertificationView(this);
        setContentView(certificationView.getContentView());

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DensityUtil.width_height(this, 0.60f, 266);
        } else {
            DensityUtil.width_height(this, 0.95f, 266);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
