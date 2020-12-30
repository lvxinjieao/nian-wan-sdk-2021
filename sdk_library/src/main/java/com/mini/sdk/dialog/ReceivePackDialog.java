package com.mini.sdk.dialog;

import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Point;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ReceivePackDialog extends DialogFragment {

	/**
	 * 日志打印
	 */
	private static final String TAG = "ReceivePackDialog";
	private static final String KEY_PACK_NAME = "packname";
	private static final String KEY_PACK_CODE = "packcode";
	private static final String KEY_PACK_STATUS = "packstatus";

	/**
	 * 上下文
	 */
	private Context con;

	/**
	 * 返回键监听
	 */
	private OnKeyListener mDialogKeyListener;

	/**
	 * 礼包名称显示
	 */
	TextView txtPackName;

	/**
	 * 礼包码显示
	 */
	TextView txtPackCode;
	/**
	 * 礼包状态显示
	 */
	TextView txtPackStatus;

	/**
	 * 礼包码
	 */
	private String packCodes;

	/**
	 * 礼包是否领取
	 */
	private String packIsReceive;

	public ReceivePackDialog() {
	}

	@SuppressLint("ValidFragment")
	public ReceivePackDialog(Context con) {
		this.con = con;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View containerView = inflater.inflate(Resources.getLayoutId(con, "dialog_mch_receive_packs"), container, false);
		// 如果有信息,显示加载信息
		Bundle bundle = getArguments();
		if (bundle == null) {
			// 没有信息直接返回
			return containerView;
		}

		CharSequence packName = bundle.getCharSequence(KEY_PACK_NAME, "游戏礼包");
		txtPackName = (TextView) containerView.findViewById(Resources.getId(con, "tx_mch_receive_pack_name"));
		txtPackName.setText(packName);

		containerView.findViewById(Resources.getId(con, "rl_receive_pack_close"))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismissAllowingStateLoss();
					}
				});

		CharSequence packCode = bundle.getCharSequence(KEY_PACK_CODE, "");
		txtPackCode = (TextView) containerView.findViewById(Resources.getId(con, "tx_mch_receive_pack_code"));
		if ("null".equals(packCode.toString())) {
			packCode = "";
		}
		txtPackCode.setText(packCode);
		packCodes = packCode.toString();

		CharSequence packStatus = bundle.getCharSequence(KEY_PACK_STATUS, "0");
		txtPackStatus = (TextView) containerView.findViewById(Resources.getId(con, "tx_mch_receive_pack_status"));
		packIsReceive = packStatus.toString();
		if ("0".equals(packStatus)) {
			txtPackStatus.setText("当前礼包您已经领取过了");
		} else {
			txtPackStatus.setText("请复制游戏礼包码");
		}

		Button copyPackCode = (Button) (Button) containerView.findViewById(Resources.getId(con, "btn_mch_receive_pack"));
		copyPackCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
				if (!TextUtils.isEmpty(packCodes) && TextUtils.equals("1", packIsReceive)) {// 礼包码没有使用
					ClipboardManager cm = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本内容放到系统剪贴板里。
					cm.setText(packCodes);
					Toast.makeText(con, "已复制到剪切板",Toast.LENGTH_LONG).show();
				} else if (!TextUtils.isEmpty(packCodes) && TextUtils.equals("0", packIsReceive)) {// 礼包码已经使用
					ClipboardManager cm = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本内容放到系统剪贴板里。
					cm.setText(packCodes);
					Toast.makeText(con, "已复制到剪切板",Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(con, "复制失败",Toast.LENGTH_LONG).show();
				}
			}
		});

		ImageView ivClose = (ImageView) containerView.findViewById(Resources.getId(con, "iv_mch_close_receive"));
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
			}
		});

		setCancelable(true);

		this.getDialog().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismissAllowingStateLoss();
					if (null != mDialogKeyListener) {
						mDialogKeyListener.onKey(dialog, keyCode, event);
					}
					return true;
				} else {
					return false;
				}
			}
		});

		return containerView;
	}

	public void setmDialogKeyListener(OnKeyListener mDialogKeyListener) {
		this.mDialogKeyListener = mDialogKeyListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置对话框的样式
		setStyle(STYLE_NO_FRAME, Resources.getStyleId(con,  "MCCustomDialog"));
	}

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
			window.getAttributes().width = (int) (windowSize.y * size_y * 1.1);
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

	public static class Builder {
		/**
		 * 存放数据的容器
		 **/
		private Bundle mmBundle;

		private OnKeyListener mmDialogKeyListener;

		public Builder() {
			mmBundle = new Bundle();
		}

		public Builder setPackName(CharSequence packname) {
			mmBundle.putCharSequence(KEY_PACK_NAME, packname);
			return this;
		}

		public Builder setPackCode(CharSequence packcode) {
			mmBundle.putCharSequence(KEY_PACK_CODE, packcode);
			return this;
		}

		public Builder setPackStatus(CharSequence packestatus) {
			mmBundle.putCharSequence(KEY_PACK_STATUS, packestatus);
			return this;
		}

		public Builder setDialogKeyListener(OnKeyListener mmDialogKeyListener) {
			this.mmDialogKeyListener = mmDialogKeyListener;
			return this;
		}

		private ReceivePackDialog create(Context con) {
			final ReceivePackDialog dialog = new ReceivePackDialog(con);
			// 1,设置显示内容
			dialog.setArguments(mmBundle);
			dialog.setmDialogKeyListener(mmDialogKeyListener);

			return dialog;
		}

		public ReceivePackDialog show(Context con, FragmentManager fm) {
			if (fm == null) {
				Logs.e("show error : fragment manager is null.");
				return null;
			}
			ReceivePackDialog dialog = create(con);
			Logs.d("show SelectPTBTypeDialog.");
//			dialog.show(fm, TAG);

			FragmentTransaction ft = fm.beginTransaction();
			ft.add(dialog, TAG);
			ft.commitAllowingStateLoss();

			return dialog;
		}
	}

}
