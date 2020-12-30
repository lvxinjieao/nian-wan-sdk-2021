package com.mini.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

 
@SuppressLint("NewApi")
public class HEtUtils {
	private static HEtUtils instance;
	private int viewClearId = -1, viewVisibleId = -1;
	private EditText editText;
	private boolean isClose = true;
	private Context context;
	private ImageView ivEye;

	public HEtUtils() {
	}

	/**
	 * @param mContext
	 * @param editText
	 * @param viewClear
	 * @param viewVisible
	 * @param imageView
	 */
	public void etHandle(Context mContext, final EditText editText, final View viewClear, View viewVisible,
			ImageView imageView) {
		if (mContext == null) {
			Logs.e("Context is null");
			return;
		}
		context = mContext;
		if (editText == null) {
			Logs.e("EditText is null");
			return;
		}
		this.editText = editText;
		if (imageView != null) {
			ivEye = imageView;
		}
		if (viewClear != null) {
			viewClear.setOnClickListener(onClickListener);
			viewClearId = viewClear.getId();
		}
		if (viewVisible != null) {
			viewVisible.setOnClickListener(onClickListener);
			viewVisibleId = viewVisible.getId();
		}

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (viewClear != null) {
					if (!android.text.TextUtils.isEmpty(editText.getText().toString())
							&& editText.getText().toString().length() > 0) {
						viewClear.setVisibility(View.VISIBLE);
					} else {
						viewClear.setVisibility(View.GONE);
					}
				}
			}
		});

	}

	View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == viewClearId) {
				editText.setText("");
			}
			if (id == viewVisibleId) {
				if (isClose) {
					// 普通文本
					editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					ivEye.setImageResource(Resources.getDrawableId(context, "sdk_eye_open"));
					// 使光标始终在最后位置
					Selection.setSelection(editText.getText(), editText.length());
					isClose = false;
				} else {
					// 显示为密码
					editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					ivEye.setImageResource(Resources.getDrawableId(context, "sdk_eye_close"));
					// 使光标始终在最后位置
					Selection.setSelection(editText.getText(), editText.length());
					isClose = true;
				}
			}

		}
	};
}
