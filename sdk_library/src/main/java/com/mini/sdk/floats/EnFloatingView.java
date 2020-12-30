package com.mini.sdk.floats;

import android.content.Context;
import android.widget.ImageView;

import com.mini.sdk.utils.Resources;

public class EnFloatingView extends FloatingMagnetView {

    private final ImageView mIcon;

    public EnFloatingView(Context context) {
        this(context, Resources.getLayoutId(context, "en_floating_view"));
    }

    public EnFloatingView(Context context, int resource) {
        super(context, null);
        inflate(context, resource, this);
        mIcon = findViewById(Resources.getId(context, "icon"));
    }

    public void setIconImage(int resId) {
        mIcon.setImageResource(resId);
    }


}
