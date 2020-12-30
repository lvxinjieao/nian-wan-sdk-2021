package com.mini.sdk.http.process;

import android.os.Handler;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.RequestParamUtil;
import com.mini.sdk.http.request.InitFloatingRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：初始化悬浮窗 作者：苏杭 时间: 2018-05-14 9:23
 */

public class InitFloatingProcess {
    private static final String TAG = "InitFloatingProcess";

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("promote_id", ChannelAndGame.getInstance().getChannelId());

        String param = RequestParamUtil.map2str(map);

        InitFloatingRequest request = new InitFloatingRequest(handler);
        request.post(Constant.INIT_FLOATING_URL, param);

    }

}
