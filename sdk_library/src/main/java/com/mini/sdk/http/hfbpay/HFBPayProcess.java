package com.mini.sdk.http.hfbpay;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class HFBPayProcess {

    /**
     * 金额(必填)
     */
    private String goodsPrice;
    /**
     * 物品名称(必填)
     */
    private String goodsName;
    /**
     * 商户添加备注(可选)
     */
    private String remark;
    /**
     * 充值类型 平台币 0 游戏 1
     */
    private String payType;
    /**
     * 游戏订单信息
     */
    private String extend;
    /***
     * 商品描述
     */
    private String goodsDesc;

    public void post(Handler handler) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sdk_version", "0");// 表示android发送的请求，固定值0
//		map.put("extend", extend);
        map.put("title", goodsName);
        map.put("price", goodsPrice);
        map.put("body", goodsDesc);

        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("game_appid", ChannelAndGame.getInstance().getGameAppId());
        // map.put("promote_id", Constant.getInstance().getPromoteId());
        // map.put("promote_account", Constant.getInstance().getPromoteAccount());
        map.put("code", payType);
        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());

        map.put("pay_type", "10");// 汇付宝支付类型等发布出去时候写个列表界面
        String param = RequestParamUtil.getRequestParamString(map);

        HFBPayRequest request = new HFBPayRequest(handler);
        request.post(Constant.hfb_order_info_url, param);

    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

}
