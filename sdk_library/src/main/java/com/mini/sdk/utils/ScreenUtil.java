package com.mini.sdk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class ScreenUtil {

	/**
	 *  获取当前屏幕截图，不包含状态栏
	 */
	@SuppressLint("NewApi")
	public static Bitmap screenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();

		Bitmap bmp = view.getDrawingCache();
		if (bmp == null) {
			return null;
		}

		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高

		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();

		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight());
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(false);
		return bitmap;
	}

	public static float[] getDeviceDisplaySize(Context context) {
		android.content.res.Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		float[] size = new float[2];
		size[0] = width;
		size[1] = height;
		return size;
	}

	public static int getStatusBarHeight(Context context) {
		int height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}

	/**
	 * 保存图片到文件File。
	 */
	public static void save(Context context, Bitmap bitmap) {
		FileOutputStream fileOutputStream = null;

		String name = "nianwan.png";
		File appDir = new File(Environment.getExternalStorageDirectory(), "image");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		File file = new File(appDir, name);

		try {
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, null);
			Toast.makeText(context, "截图已保持至： " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
	}

	/**
	 * Bitmap对象是否为空。
	 */
	public static boolean isEmptyBitmap(Bitmap src) {
		return src == null || src.getWidth() == 0 || src.getHeight() == 0;
	}

}
