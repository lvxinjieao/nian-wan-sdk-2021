package com.mini.sdk.utils;

import android.util.Log;

public class Logs {

	public static boolean isDebug = false;
	private static final String TAG = "nian_wan";

	public static void i(String i) {
		if (isDebug) {
			Log.i(TAG, i);
		}
	}

	public static void d(String i) {
		if (isDebug) {
			Log.d(TAG, i);
		}
	}

	public static void w(String i) {
		if (isDebug) {
			Log.w(TAG, i);
		}
	}

	public static void e(String i) {
		if (isDebug) {
			Log.e(TAG, i);
		}
	}

	public static void v(String i) {
		if (isDebug) {
			Log.v(TAG, i);
		}
	}
}
