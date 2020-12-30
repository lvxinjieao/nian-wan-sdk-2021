package com.mini.sdk.db;

import java.util.ArrayList;
import java.util.List;

import com.mini.sdk.entity.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginDao {

	private LoginHelper loginHelper = null;
	private static LoginDao loginDao;
	public static final String TABLENAME = "userlogin";
	public static final String ACCOUNT = "account";
	public static final String PASSWORD = "password";

	private LoginDao(Context context) {
		loginHelper = new LoginHelper(context, "userlogin.db", null, 1);
	}

	public static LoginDao getInstance(Context context) {
		if (null == loginDao) {
			loginDao = new LoginDao(context);
		}
		return loginDao;
	}

	public List<User> getUsers() {
		List<User> users = null;
		SQLiteDatabase r_db = loginHelper.getReadableDatabase();
		if (r_db.isOpen()) {
			Cursor cursor = r_db.rawQuery("select * from " + TABLENAME, null);
			users = new ArrayList<User>();
			User user = null;

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				user = new User();
				user.setAccount(cursor.getString(cursor.getColumnIndex(ACCOUNT)));
				user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
				users.add(user);
				user = null;
			}
			cursor.close();
		}
		r_db.close();
		r_db = null;
		return users;
	}

	public void saveUser(User user) {
		SQLiteDatabase w_db = loginHelper.getWritableDatabase();
		if (w_db.isOpen()) {
			w_db.execSQL("insert into " + TABLENAME + "(" + ACCOUNT + "," + PASSWORD + ") values(?,?)",new Object[] {user.getAccount(), user.getPassword() });
		}
		w_db.close();
		w_db = null;
	}

	public void updatePassword(User user) {
		SQLiteDatabase w_db = loginHelper.getWritableDatabase();
		if (w_db.isOpen()) {
			w_db.execSQL("update " + TABLENAME + " set " + PASSWORD + " = ?" + " where " + ACCOUNT + " =?",new String[] { user.getPassword(), user.getAccount()});
		}
		w_db.close();
		w_db = null;
	}

	public boolean findUserByName(String account) {
		boolean flag = false;
		SQLiteDatabase r_db = loginHelper.getReadableDatabase();
		if (r_db.isOpen()) {
			Cursor cursor = r_db.rawQuery("select * from " + TABLENAME + " where " + ACCOUNT + " =?", new String[] { account });
			if (cursor.moveToNext()) {
				flag = true;
			}
			cursor.close();
			cursor = null;
		}
		r_db.close();
		r_db = null;
		return flag;
	}

	public void deleteUserByName(String account) {
		SQLiteDatabase w_db = loginHelper.getWritableDatabase();
		if (w_db.isOpen()) {
			w_db.execSQL("delete from " + TABLENAME + " where " + ACCOUNT + "=?", new String[] { account });
		}
		w_db.close();
		w_db = null;
	}

	/**
	 * 清空表数据
	 */
	public void deleteUser() {
		SQLiteDatabase w_db = loginHelper.getWritableDatabase();
		if (w_db.isOpen()) {
			w_db.execSQL("delete from " + TABLENAME);
		}
		w_db.close();
		w_db = null;
	}

	/**
	 * 根据用户名获取密码
	 */
	public String getPasswordByUsername(String account) {
		SQLiteDatabase r_db = loginHelper.getReadableDatabase();
		String password = "";
		if (r_db.isOpen()) {
			Cursor cursor = r_db.rawQuery("select * from " + TABLENAME + " where " + ACCOUNT + " =?", new String[] { account });
			if (cursor.moveToNext()) {
				password = cursor.getString(cursor.getColumnIndex(PASSWORD));
			}
			cursor.close();
			cursor = null;
		}
		r_db.close();
		r_db = null;
		return password;
	}

	/**
	 * 获取上次登录成功的用户信息
	 */
	public User getUserLast() {
		User user = new User();
		SQLiteDatabase r_db = loginHelper.getReadableDatabase();
		if (r_db.isOpen()) {
			Cursor cursor = r_db.rawQuery("select * from " + TABLENAME, null);
			if (cursor.moveToLast()) {
				user.setAccount(cursor.getString(cursor.getColumnIndex(ACCOUNT)));
				user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
			}
			cursor.close();
			cursor = null;
		}
		r_db.close();
		r_db = null;
		return user;
	}

}
