package com.mini.sdk.callback;

import android.view.View;

public interface SelectPTBTypeCallback {

	/**
	 * 平台币支付回调
	 * @param isGameType true 选择绑定游戏币
	 */
	public void selectPTBPayType(View v, boolean isGameType);
}
