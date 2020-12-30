package com.mini.sdk.bean.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.AlipayVerifyResult;
import com.mini.sdk.entity.PayResult;
import com.mini.sdk.http.process.AlipayOrderProcess;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

public class AlipayPay {

    private boolean isBuyPtb;

    public Activity activity;

    private PlatformCoinCallback coinCallback;

    public AlipayPay(Activity activity) {
        isBuyPtb = false;
        this.activity = activity;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.ZFB_PAY_VALIDATE_SUCCESS:    // 请求支付订单成功
                    alipayPayResult(msg.obj);
                    break;

                case Constant.ZFB_PAY_VALIDATE_FAIL:    // 请求支付订单失败
                    Toast.makeText(activity, "支付宝支付失败:" + msg.obj.toString(), Toast.LENGTH_LONG).show();
                    PluginApi.flag = true;
                    break;

                case Constant.SDK_PAY_FLAG:                // 支付结果
                    handlerZfbSDKResult(msg.obj);
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 支付宝游戏充值
     */
    public void alipayPayProcess() {
        AlipayOrderProcess alipayOrderProcess = new AlipayOrderProcess();
        alipayOrderProcess.setGoodsPrice(PluginApi.orderInfo.getGoodsPriceYuan());
        alipayOrderProcess.setGoodsName(PluginApi.orderInfo.getProductName());
        alipayOrderProcess.setGoodsDesc(PluginApi.orderInfo.getProductDesc());
        alipayOrderProcess.setServerName(PluginApi.orderInfo.getServerName());
        alipayOrderProcess.setRoleName(PluginApi.orderInfo.getRoleName());
        alipayOrderProcess.setPayType("1");
        alipayOrderProcess.setExtend(PluginApi.orderInfo.getExtendInfo());
        alipayOrderProcess.post(handler);
    }

    /**
     * 平台币充值(支付宝)
     *
     * @param goodsName  需要充值的物品名称
     * @param goodsPrice 需要充值的物品价格
     * @param goodsDesc
     * @param callback   支付回调接口
     */
    public void platformCurrencyProcess(String goodsName, String goodsPrice, String goodsDesc, PlatformCoinCallback callback) {
        isBuyPtb = true;

        if (null != callback) {
            this.coinCallback = callback;
        }

        AlipayOrderProcess alipayOrderProcess = new AlipayOrderProcess();
        alipayOrderProcess.setGoodsName(goodsName);
        alipayOrderProcess.setGoodsPrice(goodsPrice);
        alipayOrderProcess.setGoodsDesc(goodsDesc);
        alipayOrderProcess.setPayType("0");
        alipayOrderProcess.setExtend("平台币充值");
        alipayOrderProcess.post(handler);
    }

    /**
     * 处理响应结果，支付宝发起支付
     */
    private void alipayPayResult(Object obj) {

        if (null == activity) {
            Logs.e("支付页面已销毁");
            return;
        }

        AlipayVerifyResult zfbPayResult = (AlipayVerifyResult) obj;
        if (null == zfbPayResult) {
            Logs.e("支付宝支付参数为空");
            return;
        }

        if (TextUtils.isEmpty(zfbPayResult.getOrderInfo())) {
            String msg = zfbPayResult.getMsg();
            if (TextUtils.isEmpty(msg)) {
                msg = "验证订单失败,请重试";
            }
            Logs.e("error:" + msg);
            return;
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = zfbPayResult.getOrderInfo();

        new Thread(new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);// 构造PayTask 对象
                String result = alipay.pay(payInfo, true);// 调用支付接口，获取支付结果
                Message msg = new Message();
                msg.what = Constant.SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void handlerZfbSDKResult(Object obj) {

        PayResult payResult = new PayResult((String) obj);
        String status = "-1";

        if (!TextUtils.isEmpty(payResult.getResultStatus())) {
            status = payResult.getResultStatus();
        }

        if (TextUtils.equals(status, "9000")) {
            PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "支付成功");
        } else if (TextUtils.equals(status, "6001")) {
            PluginApi.payListener.result(PayListener.SDK_PAY_CANCEL, "支付取消");
        } else if (TextUtils.equals(status, "4000")) {
            PluginApi.payListener.result(PayListener.SDK_PAY_FAILED, "支付失败");
        }

        if (isBuyPtb) {// 支付宝微信充值平台币
            if (null != coinCallback) {
                boolean res = "0".equals(status) || "1".equals(status);
                coinCallback.result(res);
            }
        } else {// 微信支付
            if (status.equals("0") || status.equals("1")) {
                PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "支付成功");
            }
        }

        activity.finish();
        PluginApi.flag = true;
    }
}
