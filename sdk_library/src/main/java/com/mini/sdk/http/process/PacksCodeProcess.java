package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.BaseProcess;
import com.mini.sdk.http.request.PacksCodeRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 领取礼包码
 *
 * @author Administrator
 */
public class PacksCodeProcess extends BaseProcess {

    private static final String TAG = "PacksCodeProcess";

    private String giftId;
    private String packName;

    @Override
    public String getParamStr() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("game_name", ChannelAndGame.getInstance().getGameName());
        map.put("gift_id", giftId);
        map.put("account", PluginApi.userLogin.getAccount());
        map.put("user_id", PluginApi.userLogin.getAccountId());
        Logs.e("fun#ptb_pay params:" + map.toString());

        return RequestParamUtil.getRequestParamString(map);
    }

    @Override
    public void post(Handler handler) {

        PacksCodeRequest packcodeRequest = new PacksCodeRequest(handler);
        packcodeRequest.post(Constant.game_packages_code_url, getParamStr(), packName);

    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

}
