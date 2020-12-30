package com.mini.sdk.view.util;

public class TimeFactory {
	/**
	 * @param type 倒计类型
	 */
	public static TimeUtil creator(int type) {
		if (0 == type) {// 忘记密码
			return FrogetPwdTime.getTimeUtil();
		}  else if (2 == type) {// 绑定手机
			return BindPhoneTime.getTimeUtil();
		} else if (3 == type) {// 解绑手机
			return UnBindPhoneTime.getTimeUtil();
		} else {
			return TimeUtil.getTimeUtil();
		}
	}
}
