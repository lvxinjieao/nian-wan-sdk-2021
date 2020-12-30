package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.PTBPayRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class PTBPayProcess {

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
     * 支付类型 1 平台币  2绑定币
     */
    private String code;
    /**
     * 游戏订单信息
     */
    private String extend;

    private String serverName; // 区服名字
    private String roleName; // 角色名

    public void post(Handler handler) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sdk_version", "0");// 表示android发送的请求，固定值0
        map.put("title", goodsName);
        map.put("price", goodsPrice);
        map.put("body", goodsDesc);

        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("game_appid", ChannelAndGame.getInstance().getGameAppId());
        map.put("code", code);
        map.put("extend", extend);

        if (!TextUtils.isEmpty(serverName) && !TextUtils.isEmpty(roleName)) {
            map.put("server_name", serverName);
            map.put("game_player_name", roleName);
        }

        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());

        String param = RequestParamUtil.getRequestParamString(map);

        RequestParams params = new RequestParams();

        PTBPayRequest ptbPayRequest = new PTBPayRequest(handler);
        ptbPayRequest.post(Constant.ptb_order_url, param);

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
