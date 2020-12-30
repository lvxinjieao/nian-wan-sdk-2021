package com.mini.sdk.dialog;

import com.mini.sdk.PluginApi;
import com.mini.sdk.callback.UpdateNikeNameCallback;
import com.mini.sdk.utils.HEtUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * 描述：修改昵称dialog 作者：苏杭 时间: 2018-03-06 10:41
 */

@SuppressLint("NewApi")
public class UpdateNikeNameDialog extends DialogFragment {

	private static final String TAG = "UpdateNikeNameDialog";

	/**
	 * 上下文
	 */
	private Context context;

	private UpdateNikeNameCallback mSureListener;

	public UpdateNikeNameDialog() {
	}

	@SuppressLint("ValidFragment")
	public UpdateNikeNameDialog(Context context) {
		this.context = context;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View containerView = inflater.inflate(Resources.getLayoutId(context, "dialog_mch_update_nikename"), container,
				false);
		WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
		params.alpha = 0.5f;
		((Activity) context).getWindow().setAttributes(params);

		final EditText etNikeName = (EditText) containerView
				.findViewById(Resources.getId(context, "et_mch_update_nike"));
		RelativeLayout rlClear = (RelativeLayout) containerView
				.findViewById(Resources.getId(context, "rl_mch_update_nikename_clear"));
		Button btnCancel = (Button) containerView
				.findViewById(Resources.getId(context, "btn_mch_update_nikename_cancel"));
		Button btnSure = (Button) containerView.findViewById(Resources.getId(context, "btn_mch_update_nikename_sure"));

		if (!TextUtils.isEmpty(PluginApi.userLogin.getAccountId())) {
			etNikeName.setText(PluginApi.userLogin.getAccount());
			Selection.setSelection(etNikeName.getText(), etNikeName.length()); // 使光标始终在最后位置
			rlClear.setVisibility(View.VISIBLE);
		}

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
			}
		});

		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mSureListener) {
					dismissAllowingStateLoss();
					mSureListener.updateNikeName(etNikeName.getText().toString().trim());
				}
			}
		});
		new HEtUtils().etHandle(context, etNikeName, rlClear, null, null);

		setCancelable(false);
		this.getDialog().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismissAllowingStateLoss();
					return true;
				} else {
					return false;
				}
			}
		});
		return containerView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置对话框的样式
		setStyle(STYLE_NO_FRAME, Resources.getStyleId(context, "MCCustomDialog"));
	}

	@SuppressLint("NewApi")
	@Override
	public void onStart() {
		// 1, 设置对话框的大小
		Window window = getDialog().getWindow();
		WindowManager wm = window.getWindowManager();
		Point windowSize = new Point();
		wm.getDefaultDisplay().getSize(windowSize);
		float size_x = 0;
		float size_y = 0;
		int width = windowSize.x;
		int height = windowSize.y;
		if (width >= height) {// 横屏
			size_x = 0.7f;
			size_y = 0.8f;
			window.getAttributes().width = (int) (windowSize.y * 0.8);
			window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		} else {// 竖屏
			size_x = 0.9f;
			size_y = 0.855f;
			window.getAttributes().width = (int) (windowSize.x * 0.8);
			window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		window.setGravity(Gravity.CENTER);
		super.onStart();
	}

	public void setmSureClick(UpdateNikeNameCallback mSureListener) {
		this.mSureListener = mSureListener;
	}

	public static class Builder {
		/**
		 * 存放数据的容器
		 **/
		private Bundle mmBundle;
		private UpdateNikeNameCallback mmSureListener;

		public Builder() {
			mmBundle = new Bundle();
		}

		public Builder setSureClick(UpdateNikeNameCallback mmSureListener) {
			this.mmSureListener = mmSureListener;
			return this;
		}

		private UpdateNikeNameDialog create(Context context) {
			final UpdateNikeNameDialog dialog = new UpdateNikeNameDialog(context);
			// 1,设置显示内容
			dialog.setArguments(mmBundle);
			dialog.setmSureClick(mmSureListener);
			return dialog;
		}

		public UpdateNikeNameDialog show(Context context, FragmentManager fm) {
			if (fm == null) {
				Logs.e("show error : fragment manager is null.");
				return null;
			}

			UpdateNikeNameDialog dialog = create(context);
			Logs.d("show UpdateNikeNameDiaLog");
//			diaLogshow(fm, TAG);

			FragmentTransaction ft = fm.beginTransaction();
			ft.add(dialog, TAG);
			ft.show(dialog);
			ft.commitAllowingStateLoss();
			return dialog;
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
		params.alpha = 1f;
		((Activity) context).getWindow().setAttributes(params);
	}
}
