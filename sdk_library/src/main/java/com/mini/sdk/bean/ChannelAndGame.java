package com.mini.sdk.bean;

import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.FileUtil;

import android.text.TextUtils;

public class ChannelAndGame {

	private static ChannelAndGame instance;

	public static ChannelAndGame getInstance() {
		if (null == instance) {
			instance = new ChannelAndGame();
		}
		return instance;
	}

	private String gameId;
	private String gameName;
	private String gameAppId;

	private String channelId = "0";
	private String channelAccount = "自然注册";

	public ChannelAndGame() {
		FileUtil fileUtil = new FileUtil();

		gameId = fileUtil.getGameId();
		if (TextUtils.isEmpty(gameId)) {
			gameId = String.valueOf(Constant.getInstance().getGameId());
		}

		gameName = fileUtil.getGameName();
		if (TextUtils.isEmpty(gameName)) {
			gameName = Constant.getInstance().getGameName();
		}

		gameAppId = fileUtil.getGameAppId();
		if (TextUtils.isEmpty(gameAppId)) {
			gameAppId = Constant.getInstance().getGameAppId();
		}

		channelId = fileUtil.getPromoteId();
		if (TextUtils.isEmpty(channelId)) {
			channelId = "0";
		}

		channelAccount = fileUtil.getPromoteAccount();
		if (TextUtils.isEmpty(channelAccount)) {
			channelAccount = "自然注册";
		}

	}

	public String getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameAppId() {
		return gameAppId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getChannelAccount() {
		return channelAccount;
	}

	public boolean haveRead() {
		if (TextUtils.isEmpty(gameId) || TextUtils.isEmpty(gameName) || TextUtils.isEmpty(gameAppId)
				|| TextUtils.isEmpty(channelId) || TextUtils.isEmpty(channelAccount)) {
			return false;
		}
		return true;
	}
}
