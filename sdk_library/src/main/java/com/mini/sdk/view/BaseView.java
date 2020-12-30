package com.mini.sdk.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseView implements OnClickListener {
    /**
     * 视图
     */
    protected View contentView;
    /**
     * 获得inflater对象
     */
    protected LayoutInflater inflater;

    /**
     * 获得view视图
     */
    public abstract View getContentView();
}
