package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.BaseProcess;
import com.mini.sdk.http.request.WXOrderRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class OrderInfoProcess extends BaseProcess {

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private String goodsPrice;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 充值类型 【平台币:0】 【游戏:1】
     */
    private String payType;
    /**
     * 游戏订单信息
     */
    private String extend;
    /**
     * 区服名称
     */
    private String serverName;
    /**
     * 角色名称
     */
    private String roleName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sdk_version", "0");// 表示android发送的请求，固定值0
        map.put("title", goodsName);
        map.put("price", goodsPrice);
        map.put("body", goodsDesc);
        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("game_appid", ChannelAndGame.getInstance().getGameAppId());
        map.put("code", payType);
        map.put("extend", extend);
        if (!TextUtils.isEmpty(serverName) && !TextUtils.isEmpty(roleName) && payType.equals("1")) {
            map.put("server_name", serverName);
            map.put("game_player_name", roleName);
        }
        String param = RequestParamUtil.getRequestParamString(map);
        Logs.i("请求订单  request : " + param.toString());
        return param;
    }

    @Override
    public void post(Handler handler) {

        WXOrderRequest wxRequest = new WXOrderRequest(handler);
        wxRequest.goodsType = payType;
        wxRequest.post(Constant.wx_order_url, getParamStr());

    }


}
