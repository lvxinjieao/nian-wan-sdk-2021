package com.mini.sdk.floats;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.mini.sdk.floats.utils.SystemUtils;
import com.mini.sdk.utils.DensityUtil;


public class FloatingMagnetView extends FrameLayout {

    public static final int MARGIN_EDGE = 13;
    /**
     * 移动后的X
     */
    private float mOriginalRawX;
    /**
     * 移动后的Y
     */
    private float mOriginalRawY;

    /**
     * X开始坐标
     */
    private float mOriginalX;
    /**
     * Y开始坐标
     */
    private float mOriginalY;


    private static final int TOUCH_TIME_THRESHOLD = 150;
    private long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;

    /**
     * 屏幕宽
     */
    protected int mScreenWidth;
    /**
     * 屏幕高
     */
    private int mScreenHeight;

    /**
     * barHeight
     */
    private int mStatusBarHeight;

    private boolean isNearestLeft = true;

    private MagnetViewListener mMagnetViewListener;

    public void setMagnetViewListener(MagnetViewListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public FloatingMagnetView(Context context) {
        this(context, null);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mMoveAnimator = new MoveAnimator();
        this.mStatusBarHeight = SystemUtils.getStatusBarHeight(getContext());
        setClickable(true);
        updateSize();
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event == null) {
            return false;
        }

        switch (event.getAction()) {
            case 0:
                changeOriginalTouchParams(event);
                updateSize();
                this.mMoveAnimator.stop();
                break;
            case 2:
                updateViewPosition(event);
                break;
            case 1:
                moveToEdge();
                if (isOnClickEvent()) {
                    dealClickEvent();
                }
                break;
        }
        return true;
    }

    protected void dealClickEvent() {
        if (this.mMagnetViewListener != null) {
            this.mMagnetViewListener.onClick(this);
        }
    }

    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - this.mLastTouchDownTime < 150L;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            float moveDistance = isNearestLeft() ? -DensityUtil.dip2px(getContext(), 28) / 2 : mScreenWidth + DensityUtil.dip2px(getContext(), 28) / 2;
            mMoveAnimator.start(moveDistance, getY());
        }
    };

    private void updateViewPosition(MotionEvent event) {
        setX(this.mOriginalX + event.getRawX() - this.mOriginalRawX);

        float desY = this.mOriginalY + event.getRawY() - this.mOriginalRawY;
        if (desY < this.mStatusBarHeight) {
            desY = this.mStatusBarHeight;
        }
        if (desY > this.mScreenHeight - getHeight()) {
            desY = this.mScreenHeight - getHeight();
        }
        setY(desY);


        handler.sendEmptyMessageDelayed(0, 3000);

    }

    private void changeOriginalTouchParams(MotionEvent event) {
        this.mOriginalX = getX();
        this.mOriginalY = getY();
        this.mOriginalRawX = event.getRawX();
        this.mOriginalRawY = event.getRawY();
        this.mLastTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        this.mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - getWidth());
        this.mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    public void moveToEdge() {
        moveToEdge(isNearestLeft());
    }

    public void moveToEdge(boolean isLeft) {
        float moveDistance = isLeft ? 10 : this.mScreenWidth - 10;
        this.mMoveAnimator.start(moveDistance, getY());
    }

    /**
     * 靠左
     */
    protected boolean isNearestLeft() {
        int middle = this.mScreenWidth / 2;
        this.isNearestLeft = (getX() < middle);
        return this.isNearestLeft;
    }

    public void onRemove() {
        if (this.mMagnetViewListener != null) {
            this.mMagnetViewListener.onRemove(this);
        }
    }

    protected class MoveAnimator implements Runnable {
        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        protected MoveAnimator() {
        }

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            this.startingTime = System.currentTimeMillis();
            this.handler.post(this);
        }

        public void run() {
            if ((FloatingMagnetView.this.getRootView() == null) || (FloatingMagnetView.this.getRootView().getParent() == null)) {
                return;
            }
            float progress = Math.min(1.0F, (float) (System.currentTimeMillis() - this.startingTime) / 400.0F);
            float deltaX = (this.destinationX - FloatingMagnetView.this.getX()) * progress;
            float deltaY = (this.destinationY - FloatingMagnetView.this.getY()) * progress;
            FloatingMagnetView.this.move(deltaX, deltaY);
            if (progress < 1.0F) {
                this.handler.post(this);
            }
        }

        private void stop() {
            this.handler.removeCallbacks(this);
        }
    }

    private void move(float deltaX, float deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateSize();
        moveToEdge(this.isNearestLeft);
    }
}
