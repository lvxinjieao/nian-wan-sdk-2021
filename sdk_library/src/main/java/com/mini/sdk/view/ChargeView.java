package com.mini.sdk.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.WapPayActivity;
import com.mini.sdk.activity.WebViewActivity;
import com.mini.sdk.bean.UserReLogin;
import com.mini.sdk.bean.UserReLogin.ReLoginCallback;
import com.mini.sdk.bean.pay.AlipayPay;
import com.mini.sdk.callback.SelectPTBTypeCallback;
import com.mini.sdk.dialog.DialogUtil;
import com.mini.sdk.dialog.PTBPayResultDialog;
import com.mini.sdk.dialog.SelectCoinDialog;
import com.mini.sdk.entity.DiscountPrice;
import com.mini.sdk.entity.GamePayTypeEntity;
import com.mini.sdk.entity.PTBPayResult;
import com.mini.sdk.entity.UserDiscountEntity;
import com.mini.sdk.entity.UserPTBInfo;
import com.mini.sdk.entity.WXOrderInfo;
import com.mini.sdk.http.process.OrderInfoProcess;
import com.mini.sdk.http.process.PTBPayProcess;
import com.mini.sdk.http.process.PayTypeProcess;
import com.mini.sdk.http.process.UserCoinProcess;
import com.mini.sdk.http.process.UserDiscountProcess;
import com.mini.sdk.listener.PayListener;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.DeviceInfo;
import com.mini.sdk.utils.MoneyUtils;
import com.mini.sdk.utils.Resources;

public class ChargeView extends BaseView {

    public Activity context;

    private TextView back;

    /**
     * 折扣
     */
    private float discount = 10;
    private DiscountPrice discountPrice;
    /**
     * 原价
     */
    private String price = PluginApi.orderInfo.getGoodsPriceYuan();
    /**
     * 折扣价
     */
    private String discount_price = price;

    private TextView charge_account;
    private TextView charge_price;
    private TextView charge_product;
    private TextView charge_discount;

    private RelativeLayout rl_alipay, rl_weixin, rl_platform_currency;

    private ImageView select_alipay, select_weixin, select_platform_currency;

    private boolean agrement = true;//默认选择
    private ImageView selected;//选择支付协议
    private TextView protocol;//支付协议

    private TextView copy;

    private Button pay_confrim;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case Constant.GAME_PAY_TYPE_SUCCESS:
                    handlerPayType((GamePayTypeEntity) msg.obj);
                    break;
                case Constant.GAME_PAY_TYPE_FAIL:
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;


                case Constant.GET_USER_DISCOUNT_SUCCESS:// 折扣信息成功
                    handlerDiscount((UserDiscountEntity) msg.obj);
                    break;
                case Constant.GET_USER_DISCOUNT_FAIL:// 折扣信息失败
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;


                case Constant.PTB_MONEY_SUCCESS:// 获取平台币余额成功
                    handlerCoin(msg.obj);
                    break;
                case Constant.PTB_MONEY_FAIL:    // 获取平台币余额异常
                    Toast.makeText(context, "获取平台币余额出现异常：" + msg.obj, Toast.LENGTH_LONG).show();
                    PluginApi.flag = true;
                    break;


                case Constant.PTB_PAY_SUCCESS:    // 平台币支付成功
                    handlerCoinResult(msg.obj);
                    break;
                case Constant.PTB_PAY_FAIL:        // 平台币支付失败
                    getContentView().setVisibility(View.VISIBLE);
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;


                case Constant.WFT_ORDERINFO_SUCCESS:// 微信订单请求成功
                    boolean avilible = DeviceInfo.isWeixinAvilible(context);
                    if (!avilible) {
                        Toast.makeText(context, "没有安装微信", Toast.LENGTH_LONG).show();
                        PluginApi.flag = true;
                        return;
                    }

                    WXOrderInfo wxOrderInfo = (WXOrderInfo) msg.obj;
                    Intent intent = new Intent(context, WapPayActivity.class);
                    intent.putExtra("WXOrderInfo", wxOrderInfo);
                    context.startActivity(intent);
                    context.finish();//关闭支付中心页面
                    break;
                case Constant.WFT_ORDERINFO_FAIL:// 微信订单请求失败
                    Toast.makeText(context, "微信支付失败 -:" + msg.obj, Toast.LENGTH_LONG).show();
                    PluginApi.flag = true;
                    break;

            }
        }
    };

    /**
     * 用户平台币余额
     */
    private String userCoin = "";
    /**
     * 用户绑定平台币余额
     */
    private String userBindCoin = "";

    /**
     * 平台币弹窗
     */
    private SelectCoinDialog coinDialog;

    private void handlerCoin(Object obj) {
        PluginApi.flag = true;
        UserPTBInfo userPtb = (UserPTBInfo) obj;
        userCoin = String.format("%.2f", userPtb.getPtbMoney());
        userBindCoin = String.format("%.2f", userPtb.getBindptbMoney());

        coinDialog = new SelectCoinDialog.Builder()
                .setTitle("平台币")
                .setPTB("平台币余额:" + userCoin)
                .setPayPTB("应付款平台币数量:" + discount_price).setBindPTB("绑定平台币余额:" + userBindCoin)
                .setmmSelectPTBTypeCallback(selectPtbTypeCallback)
                .setmmCloseListener(closeListener)
                .show(context, ((Activity) context).getFragmentManager(), discountPrice);

        coinDialog.setCancelable(true);
    }

    private boolean isBind;
    private boolean isPTB; // 用于支付成功弹窗展示是平台币还是绑币支付

    private SelectPTBTypeCallback selectPtbTypeCallback = new SelectPTBTypeCallback() {

        @Override
        public void selectPTBPayType(View v, boolean isGameType) {

            isBind = isGameType;
            float price = Float.parseFloat(discount_price);

            if (isGameType) {
                float bindptb = MoneyUtils.priceToFloat(userBindCoin);
                if ((bindptb - price) >= 0) {
                    isPTB = false;
                    coinProcess("2");
                } else {
                    getContentView().setVisibility(View.VISIBLE);
                    DialogUtil.showAlert(context, "提示", "绑定平台币余额不足", "确定");
                }
            } else {
                float ptb = MoneyUtils.priceToFloat(userCoin);
                if ((ptb - price) >= 0) {
                    isPTB = true;
                    coinProcess("1");
                } else {
                    getContentView().setVisibility(View.VISIBLE);
                    DialogUtil.showAlert(context, "提示", "平台币余额不足", "确定");
                }
            }
        }
    };

    OnClickListener closeListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getContentView().setVisibility(View.VISIBLE);
            coinDialog.dismissAllowingStateLoss();
        }
    };

    public ChargeView(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(Resources.getLayoutId(context, "sdk_charge_view"), null);

        initUI();
        initData();
    }

    public void initUI() {
        back = (TextView) contentView.findViewById(Resources.getId(context, "back"));
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PluginApi.payListener.result(PayListener.SDK_PAY_CANCEL, "支付取消");
                ((Activity) context).finish();
            }
        });

        charge_account = (TextView) contentView.findViewById(Resources.getId(context, "charge_account"));
        charge_account.setText(PluginApi.userLogin.getAccount());

        charge_price = (TextView) contentView.findViewById(Resources.getId(context, "charge_price"));
        charge_price.setText("￥" + PluginApi.orderInfo.getFloatGoodsPriceYuan() + "元");

        charge_product = (TextView) contentView.findViewById(Resources.getId(context, "charge_product"));
        charge_product.setText(PluginApi.orderInfo.getProductName());

        charge_discount = (TextView) contentView.findViewById(Resources.getId(context, "charge_discount"));

        rl_alipay = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_alipay"));
        rl_alipay.setOnClickListener(this);

        rl_weixin = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_weixin"));
        rl_weixin.setOnClickListener(this);

        rl_platform_currency = (RelativeLayout) contentView.findViewById(Resources.getId(context, "rl_platform_currency"));
        rl_platform_currency.setOnClickListener(this);

        select_alipay = (ImageView) contentView.findViewById(Resources.getId(context, "select_alipay"));
        select_weixin = (ImageView) contentView.findViewById(Resources.getId(context, "select_weixin"));
        select_platform_currency = (ImageView) contentView.findViewById(Resources.getId(context, "select_platform_currency"));


        selected = contentView.findViewById(Resources.getId(context, "selected"));
        selected.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (agrement) {
                    selected.setBackgroundResource(Resources.getDrawableId(context, "charge_unselect"));
                } else {
                    selected.setBackgroundResource(Resources.getDrawableId(context, "charge_select"));
                }
                agrement = !agrement;
            }
        });

        protocol = contentView.findViewById(Resources.getId(context, "protocol"));
        protocol.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                agrement = true;
                selected.setBackgroundResource(Resources.getDrawableId(context, "charge_select"));
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "支付协议");
                intent.putExtra("url", Constant.pay_agreement_url);
                context.startActivity(intent);
            }
        });


        copy = contentView.findViewById(Resources.getId(context, "copy"));
        copy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "nianwan9");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            }
        });

        pay_confrim = contentView.findViewById(Resources.getId(context, "pay_confrim"));
        pay_confrim.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (agrement) {
                    paySelect();
                } else {
                    Toast.makeText(context, "请阅读支付协议", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void initData() {
        UserDiscountProcess discountProcess = new UserDiscountProcess();
        discountProcess.post(handler);
    }

    private void paySelect() {
        switch (selectType) {

            case PAYTYPE_ZFB:
                alipayProcess();
                break;

            case PAYTYPE_WX:
                wxPayProcess();
                break;

            case PAYTYPE_PTB:
                checkCoinPay();
                getContentView().setVisibility(View.INVISIBLE);
                break;
        }

    }

    /**
     * 支付宝
     */
    private void alipayProcess() {
        AlipayPay alipayPay = new AlipayPay(context);
        alipayPay.alipayPayProcess();
    }

    /**
     * 微信
     */
    private void wxPayProcess() {
        OrderInfoProcess orderProcess = new OrderInfoProcess();
        orderProcess.setGoodsName(PluginApi.orderInfo.getProductName());
        orderProcess.setGoodsPrice(PluginApi.orderInfo.getGoodsPriceYuan());
        orderProcess.setGoodsDesc(PluginApi.orderInfo.getProductDesc());
        orderProcess.setExtend(PluginApi.orderInfo.getExtendInfo());
        orderProcess.setPayType("1");
        orderProcess.setRoleName(PluginApi.orderInfo.getRoleName());// 角色名称
        orderProcess.setServerName(PluginApi.orderInfo.getServerName());// 服务器名称
        orderProcess.post(handler);
    }

    /**
     * 平台币支付
     */
    private void coinProcess(String payType) {
        PTBPayProcess ptbpayProcess = new PTBPayProcess();
        ptbpayProcess.setGoodsName(PluginApi.orderInfo.getProductName());
        ptbpayProcess.setGoodsPrice(PluginApi.orderInfo.getGoodsPriceYuan());
        ptbpayProcess.setGoodsDesc(PluginApi.orderInfo.getProductDesc());
        ptbpayProcess.setExtend(PluginApi.orderInfo.getExtendInfo());
        ptbpayProcess.setCode(payType);
        ptbpayProcess.setServerName(PluginApi.orderInfo.getServerName());
        ptbpayProcess.setRoleName(PluginApi.orderInfo.getRoleName());
        ptbpayProcess.post(handler);
    }


    /**
     * 查询平台币
     */
    private void checkCoinPay() {
        String user_id = PluginApi.userLogin.getAccountId();
        if (!TextUtils.isEmpty(user_id)) {
            queryCoint();
        } else {
            UserReLogin reLogin = new UserReLogin(context);
            reLogin.userToLogin(new ReLoginCallback() {
                @Override
                public void reLoginResult(boolean success) {
                    if (success) {
                        queryCoint();
                    }
                }
            });
        }
    }

    /**
     * 获取平台币 Type 0是游戏币 1是绑定游戏币
     */
    private void queryCoint() {
        new UserCoinProcess().post(handler);
    }


    private void handlerPayType(GamePayTypeEntity gamePayType) {
        if (null != gamePayType) {
            rl_alipay.setVisibility(gamePayType.isHaveZFB() ? View.VISIBLE : View.GONE);
            rl_weixin.setVisibility(gamePayType.isHaveWX() ? View.VISIBLE : View.GONE);
            selectType = gamePayType.isHaveWX() ? PAYTYPE_WX : PAYTYPE_PTB;
            selectType = gamePayType.isHaveZFB() ? PAYTYPE_ZFB : PAYTYPE_PTB;
            select(selectType);
        }
    }

    private void handlerDiscount(UserDiscountEntity userDiscount) {

        if (0 != userDiscount.getDiscountType() && 10 != userDiscount.getDiscountNum()) {
            discount = userDiscount.getDiscountNum();
            discount_price = String.format("%.2f", Float.parseFloat(price) * discount / 10);
            String discount_message = "折扣:--";
            if (1 == userDiscount.getDiscountType()) {
                discount_message = "首充折扣:" + userDiscount.getDiscountNum();
            } else if (2 == userDiscount.getDiscountType()) {
                discount_message = "续充折扣:" + userDiscount.getDiscountNum();
            }
            charge_discount.setText(discount_message);
            charge_discount.setVisibility(View.VISIBLE);
        }
        discountPrice = new DiscountPrice();
        discountPrice.setPrice(price);
        discountPrice.setDiscountPrice(discount_price);

        PayTypeProcess payTypeProcess = new PayTypeProcess();
        payTypeProcess.post(handler);
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == rl_alipay.getId()) {
            selectType = PAYTYPE_ZFB;
        } else if (id == rl_weixin.getId()) {
            selectType = PAYTYPE_WX;
        } else if (id == rl_platform_currency.getId()) {
            selectType = PAYTYPE_PTB;
        }
        select(selectType);
    }

    private final int PAYTYPE_ZFB = 0x02;
    private final int PAYTYPE_WX = 0x01;
    private final int PAYTYPE_PTB = 0x00;
    // 选择的支付按钮
    private int selectType = PAYTYPE_ZFB;

    private void select(int i) {
        select_alipay.setBackgroundResource(Resources.getDrawableId(context, "charge_unselect"));
        select_weixin.setBackgroundResource(Resources.getDrawableId(context, "charge_unselect"));
        select_platform_currency.setBackgroundResource(Resources.getDrawableId(context, "charge_unselect"));
        switch (i) {
            case PAYTYPE_ZFB:
                select_alipay.setBackgroundResource(Resources.getDrawableId(context, "charge_select"));
                break;

            case PAYTYPE_WX:
                select_weixin.setBackgroundResource(Resources.getDrawableId(context, "charge_select"));
                break;

            case PAYTYPE_PTB:
                select_platform_currency.setBackgroundResource(Resources.getDrawableId(context, "charge_select"));
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void handlerCoinResult(Object obj) {
        PTBPayResult ptbPayResult = (PTBPayResult) obj;

        if (null != ptbPayResult && ptbPayResult.getReturn_status().equals("1")) {// 平台币 支付成功
            PluginApi.payListener.result(PayListener.SDK_PAY_SUCCEED, "平台币 支付成功");

            String mPrice = "";
            if (isBind) {
                mPrice = price;
            } else {
                mPrice = discount_price;
            }

            if (isPTB) {
                PTBPayResultDialog show_coin = new PTBPayResultDialog.Builder()
                        .setMoney(mPrice)
                        .setGoodsName(PluginApi.orderInfo.getProductName())
                        .setTradeWay("平台币")
                        .setTradeNo(ptbPayResult.getOrderNumber())
                        .setDialogKeyListener(backListener)
                        .setCloseClick(closeClick).show(context, ((Activity) context).getFragmentManager());
                show_coin.setCancelable(false);
            } else {
                PTBPayResultDialog show_bind_coin = new PTBPayResultDialog.Builder().setMoney(mPrice)
                        .setGoodsName(PluginApi.orderInfo.getProductName()).setTradeWay("绑定平台币")
                        .setTradeNo(ptbPayResult.getOrderNumber()).setDialogKeyListener(backListener)
                        .setCloseClick(closeClick).show(context, ((Activity) context).getFragmentManager());
                show_bind_coin.setCancelable(false);
            }

        } else {// 支付失败
            PluginApi.payListener.result(PayListener.SDK_PAY_FAILED, "平台币 支付失败");
        }
    }

    /**
     * 返回按钮监听
     */
    DialogInterface.OnKeyListener backListener = new DialogInterface.OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            ((Activity) context).finish();
            return false;
        }
    };

    /**
     * 确定按钮监听
     */
    private OnClickListener closeClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Activity) context).finish();
        }
    };

}
