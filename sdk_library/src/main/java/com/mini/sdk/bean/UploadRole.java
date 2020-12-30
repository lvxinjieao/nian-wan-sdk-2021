package com.mini.sdk.bean;

import com.mini.sdk.PluginApi;
import com.mini.sdk.http.process.UploadRoleProcess;
import com.mini.sdk.listener.UpdateRoleListener;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class UploadRole {

	String gameId;
	String serverId;
	String serverName;
	String roleName;
	String roleLevel;
	
	UpdateRoleListener roleCallBack;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.UPLOAD_ROLE_SUCCESS:// 修改成功
				if (null != getRoleCallBack()) {
					getRoleCallBack().result("1");
				}
				break;
			case Constant.UPLOAD_ROLE_FAIL:// 修改失败
				if (null != getRoleCallBack()) {
					getRoleCallBack().result("0");
				}
				break;
			}
		}
	};

	public UploadRole(String gameId, String serverId, String serverName, String roleName, String roleLevel,
			UpdateRoleListener roleCallBack) {
		if (null == serverName || null == roleLevel) {
			Logs.e("fun#uploadRole userId、roleLevel、serverName must be not null\n" + "serverName\t" + serverName + "\n" + "roleName\t" + roleName + "\n" + "roleLevel\t" + roleLevel);
		}
		this.gameId = gameId;
		this.serverId = serverId;
		this.serverName = serverName;
		this.roleName = roleName;
		this.roleLevel = roleLevel;
		this.roleCallBack = roleCallBack;
	}

	public void upload() {
		if (TextUtils.isEmpty(PluginApi.userLogin.getAccount())) {
			Logs.w("请登录");
			return;
		}
		UploadRoleProcess roleProcess = new UploadRoleProcess();
		roleProcess.setServerName(this.serverName);
		roleProcess.setRoleName(this.roleName);
		roleProcess.setRoleLevel(this.roleLevel);
		roleProcess.setServerId(this.serverId);
		roleProcess.post(mHandler);
	}

	public UpdateRoleListener getRoleCallBack() {
		if (roleCallBack != null) {
			return roleCallBack;
		}
		return null;
	}
}
