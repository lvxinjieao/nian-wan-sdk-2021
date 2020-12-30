package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.BaseProcess;
import com.mini.sdk.http.request.VerificationOrderRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class VerificationOrderProcess extends BaseProcess {

    private String tradeNo;

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public void post(Handler handler) {

        VerificationOrderRequest verification = new VerificationOrderRequest(handler);
        verification.post(Constant.pay_result_verification_url, getParamStr());

    }

    @Override
    public String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("out_trade_no", tradeNo);
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        return RequestParamUtil.getRequestParamString(map);
    }

}
