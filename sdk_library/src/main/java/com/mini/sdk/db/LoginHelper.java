package com.mini.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginHelper extends SQLiteOpenHelper {
	public static final String TABLENAME = "userlogin";

	public static final String ACCOUNT = "account";
	public static final String PASSWORD = "password";

	public LoginHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) { // 保存登录的用户信息
		db.execSQL("create table if not exists " + TABLENAME + "(_id integer primary key autoincrement, " + ACCOUNT + " String, " + PASSWORD + " String)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
