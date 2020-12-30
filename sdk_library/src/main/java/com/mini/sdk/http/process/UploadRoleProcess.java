package com.mini.sdk.http.process;

import android.os.Handler;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.http.request.UploadRoleRequest;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.RequestParamUtil;

import java.util.HashMap;
import java.util.Map;

public class UploadRoleProcess {

    // 区服名称
    String serverName;
    // 游戏内昵称
    String roleName;
    // 等级
    String roleLevel;
    // 游戏id
    String gameId;
    // 服务器id
    String serverId;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void post(Handler handler) {
        if (null == handler) {
            Logs.e("fun#post handler is null");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", PluginApi.userLogin.getAccountId());
        map.put("game_id", ChannelAndGame.getInstance().getGameId());
        map.put("server_id", this.serverId);
        map.put("server_name", this.serverName);
        map.put("game_player_name", this.roleName);
        map.put("promote_id", ChannelAndGame.getInstance().getChannelId());
        map.put("role_level", this.roleLevel);
        map.put("sdk_version", "1");

        for (Map.Entry entry : map.entrySet()) {
            Logs.e("->>" + entry.getKey() + "\t\t" + entry.getValue() + "\n");
        }

        String param = RequestParamUtil.getRequestParamString(map);
        if (TextUtils.isEmpty(param)) {
            Logs.e("fun#post param is null");
            return;
        }

        RequestParams params = new RequestParams();

        UploadRoleRequest uploadRoleRequest = new UploadRoleRequest(handler, null);
        uploadRoleRequest.post(Constant.upload_role_url, param);


    }
}
