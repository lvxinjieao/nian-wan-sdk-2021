package com.mini.sdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class LoncentViewPager extends ViewPager {

	// ============================================================
	private boolean isCanScroll = true;// FALSE：禁止滑动 TRUE：可以滑动

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}
	// ============================================================

	public LoncentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				if (onClickListener != null) {
					onClickListener.click();
					return true;
				}
				break;
			default:
				break;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * ClickListener
	 */
	public OnClickListener onClickListener;

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public interface OnClickListener {
		public void click();
	}

}
