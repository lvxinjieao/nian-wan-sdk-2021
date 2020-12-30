package com.mini.sdk.utils;

import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mini.sdk.PluginApi;
import com.mini.sdk.entity.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

	public static final String SHARED_PREFS_FILE = "INTERACTIVE";
	public static final String USERNAME = "MENGCHUANG_USERNAME";

	/** 登录用户文件名 */
	private static final String LOGIN_USER = "login_user";

	/**
	 * 保存数据的方法
	 */
	public static void setFastLogin(Context context, Boolean bool) {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_USER, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("quick_login", bool);
		editor.commit();
	}

	public static boolean getFastLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_USER, Context.MODE_PRIVATE);
		PluginApi.quick_login = sp.getBoolean("quick_login", false);
		Logs.i("quick_login : " + PluginApi.quick_login);
		return PluginApi.quick_login;
	}

	/**
	 * 设置string值
	 *
	 * @param key
	 * @param value
	 * @param context
	 */
	public static void setString(String key, String value, Context context) {
		Editor editor = context.getSharedPreferences(Constant.PRE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 得到string值
	 *
	 * @param key
	 * @param context
	 * @return
	 */
	public static String getString(String key, Context context) {
		return context.getSharedPreferences(Constant.PRE_NAME, Context.MODE_PRIVATE).getString(key, "");
	}

	/**
	 * 存储list<bean>
	 */
	private static void setUsers(LinkedList<User> list, Context context) {
		Editor editor = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit();
		if (list == null || list.size() == 0) {
			editor.clear();
			editor.commit();
			return;
		}
		Gson gson = new Gson();
		// 转换成json数据，再保存
		String json = gson.toJson(list);
		editor.putString(USERNAME, json);
		editor.commit();
	}

	/**
	 * 获取用户信息列表
	 */
	public static LinkedList<User> getUsers(Context context) {
		LinkedList<User> list = new LinkedList<User>();
		String json = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).getString(USERNAME, "");
		Logs.i("用户信息列表 json : "+json);
		if (json == null) {
			return null;
		}
		Gson gson = new Gson();
		list = gson.fromJson(json, new TypeToken<LinkedList<User>>() {}.getType());
		return list;
	}

	/**
	 * 操作用户信息
	 */
	public static void saveUser(Context context, User user) {
		LinkedList<User> users = getUsers(context);

		if (null != users && users.size() != 0) {

			for (int i = 0; i < users.size(); i++) {
//				// 只保留最新登录的账号密码
//				users.get(i).setAccount(user.getAccount());
//				users.get(i).setPassword(user.getPassword());

				if (user.getAccount().equals(users.get(i).getAccount())) {
					users.remove(i);
				}
			}

			if (users.size() == 5) {// 最多保存5个用户名密码
				users.remove(4);
			}

			users.addFirst(user);
		} else {
			users = new LinkedList<User>();
			users.add(user);
		}

		setUsers(users, context);
	}

	/**
	 * 删除list某个项并保存
	 */
	public static void removeAndSaveUserInfoList(Context context, int position) {
		LinkedList<User> oldList = getUsers(context);
		oldList.remove(position);
		setUsers(oldList, context);
	}

}
