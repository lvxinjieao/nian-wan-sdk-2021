package com.mini.sdk.floats;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract interface IFloatingView {

    public abstract FloatingView remove();

    public abstract FloatingView init();

    public abstract FloatingView attach(Activity paramActivity);

    public abstract FloatingView attach(FrameLayout paramFrameLayout);

    public abstract FloatingView detach(Activity paramActivity);

    public abstract FloatingView detach(FrameLayout paramFrameLayout);

    public abstract FloatingMagnetView getView();

    public abstract FloatingView icon(int paramInt);

    public abstract FloatingView customView(FloatingMagnetView paramFloatingMagnetView);

    public abstract FloatingView customView(int paramInt);

    public abstract FloatingView layoutParams(ViewGroup.LayoutParams paramLayoutParams);

    public abstract FloatingView listener(MagnetViewListener paramMagnetViewListener);
}
