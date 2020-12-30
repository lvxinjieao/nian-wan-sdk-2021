package com.cn.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.mini.sdk.PluginApi;
import com.mini.sdk.listener.ExitListener;
import com.mini.sdk.listener.InitListener;
import com.mini.sdk.listener.LoginListener;
import com.mini.sdk.listener.LoginResult;
import com.mini.sdk.listener.LogoutListener;
import com.mini.sdk.listener.OrderInfo;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.listener.UpdateRoleListener;

public class MainActivity extends Activity {

    public PluginApi instance;

    public Button login, logout, exit1, pay, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = PluginApi.getInstance(MainActivity.this);

        instance.init(new InitListener() {

            @Override
            public void result(int code, String message) {
                if (code == InitListener.SDK_INIT_SUCCESS) {
                    Log.i("sdk_1.2.2", "初始化成功");
                } else {
                    Log.i("sdk_1.2.2", "初始化失败");
                }
            }
        },new LogoutListener() {

            @Override
            public void result(int code, String message) {
                if (code == LogoutListener.SDK_LOGOUT_SUCCESS) {
                    Log.i("sdk_1.2.2", "登出成功");
                } else {
                    Log.i("sdk_1.2.2", "登出失败");
                }
            }
        }, new ExitListener() {

            @Override
            public void result(int code, String message) {
                if (code == ExitListener.SDK_EXIT_SUCCESS) {
                    Log.i("sdk_1.2.2", "退出成功");
                } else {
                    Log.i("sdk_1.2.2", "退出失败");
                }
            }
        },true);

        initView();
    }





    private void initView() {

        // 登录
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginApi.getInstance(MainActivity.this).login(loginListener);
            }
        });

        // 取消
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginApi.getInstance(MainActivity.this).logout();
            }
        });

        // 支付
        pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                OrderInfo order = new OrderInfo();
                order.setProductName("ProductName");// 物品名称
                order.setProductDesc("ProductDesc"); // 物品描述
                order.setAmount(10);// 商品价格（单位分）
                order.setExtendInfo(String.valueOf(System.currentTimeMillis()));// 透传参数，建议填订单号，不可重复
                order.setServerName("ServerName");
                order.setRoleName("RoleName");
                order.setGameServerId("12");// 游戏区服ID
                PluginApi.getInstance(MainActivity.this).pay(order, payCallback);
            }
        });

        // 返回手机桌面
        exit1 = (Button) findViewById(R.id.exit1);
        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginApi.getInstance(MainActivity.this).exit();
            }
        });

        // 上传角色final
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginApi.getInstance(MainActivity.this).uploadRole("1", "服务器名", "角色名", "1", uploadRoleCallBack);
            }
        });
    }


    /**
     * 上传角色回调 1：成功 ，其他为失败
     */
    private UpdateRoleListener uploadRoleCallBack = new UpdateRoleListener() {
        @Override
        public void result(String message) {
            Log.i("sdk_1.2.2", "上传角色回调: " + message);
            if ("1".equals(message)) {
                Log.i("sdk_1.2.2", "上传角色成功");
            }
        }
    };

    /**
     * 登录回调接口
     */
    private LoginListener loginListener = new LoginListener() {

        @Override
        public void result(LoginResult result) {
            switch (result.getCode()) {
                case LoginResult.SDK_LOGIN_FAILED:
                    Log.i("sdk_1.2.2", "登录失败");
                    break;
                case LoginResult.SDK_LOGIN_SUCCESS:
                    Log.i("sdk_1.2.2", "登录成功");

                    Log.i("sdk_1.2.2", " Account:" + result.getAccount());
                    Log.i("sdk_1.2.2", " AccountId :" + result.getAccountId());
                    Log.i("sdk_1.2.2", " token:" + result.getToken());

                    break;
            }
        }
    };

    /**
     * 支付结果回调
     */
    private PayListener payCallback = new PayListener() {
        @Override
        public void result(int code, String message) {

            switch (code) {
                case PayListener.SDK_PAY_SUCCEED:// 支付成功
                    Log.i("sdk_1.2.2", message);
                    break;

                case PayListener.SDK_PAY_CANCEL:// 支付取消
                    Log.i("sdk_1.2.2", message);
                    break;

                case PayListener.SDK_PAY_FAILED:// 支付失败
                    Log.i("sdk_1.2.2", message);
                    break;
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        PluginApi.getInstance(MainActivity.this).onStart();
    }

    /**
     * 游戏从后台开始，开始计时(必接)
     */
    @Override
    protected void onResume() {
        super.onResume();
        PluginApi.getInstance(MainActivity.this).onResume(); // 请求后台设置的悬浮球开关、图片等
    }


    @Override
    protected void onPause() {
        super.onPause();
        PluginApi.getInstance(MainActivity.this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PluginApi.getInstance(MainActivity.this).onStop();
    }

    /**
     * 游戏进入后台或退出游戏时，请求下线（下线通知）（必接）
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PluginApi.getInstance(MainActivity.this).onDestroy();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            PluginApi.getInstance(MainActivity.this).exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

