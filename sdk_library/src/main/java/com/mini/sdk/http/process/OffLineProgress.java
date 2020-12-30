package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.RequestParamUtil;
import com.mini.sdk.http.request.OffLineAnnounceRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

import java.util.HashMap;
import java.util.Map;


/**
 * 下线
 */
public class OffLineProgress {

    private String account = PluginApi.userLogin.getAccount();
    private String game_id = ChannelAndGame.getInstance().getGameId();
    private String promote_id = ChannelAndGame.getInstance().getChannelAccount();
    private String sdk_version = "0";

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("下线 handler is null");
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("account", account);
        map.put("game_id", game_id);
        map.put("promote_id", promote_id);
        map.put("sdk_version", sdk_version);
        String param = RequestParamUtil.map2str(map);

        if (TextUtils.isEmpty(param)) {
            Logs.e("下线 post param is null");
            return;
        }

        OffLineAnnounceRequest offLineAnnounceRequest = new OffLineAnnounceRequest(handler);
        offLineAnnounceRequest.post(Constant.OFF_LINE_ANNOUCE_URL, param);

    }

}
