package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.WXPayResultRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：获取微信官方支付结果
 */
public class WXPayResultProgress {

    private String orderNo;

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("orderno", orderNo);

        String param = RequestParamUtil.getRequestParamString(map);


        WXPayResultRequest request = new WXPayResultRequest(handler);
        request.post(Constant.wx_pay_result_url, param);

    }
}
