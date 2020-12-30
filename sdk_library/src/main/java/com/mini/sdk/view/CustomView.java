package com.mini.sdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mini.sdk.utils.DensityUtil;

public class CustomView extends ImageView {

    private Context context;
    private Paint paint;
    private int lastX;
    private int lastY;

    private android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    handler.removeMessages(0);
                    layout(-DensityUtil.dip2px(context, 20), mi_top, getWidth() - DensityUtil.dip2px(context, 20), mi_bottom);
                    break;
                case 1:
                    handler.removeMessages(1);
                    layout(mi_width - getWidth() + DensityUtil.dip2px(context, 20), mi_top, mi_width + DensityUtil.dip2px(context, 20), mi_bottom);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public CustomView(Context context) {
        super(context);
        this.context = context;
        initDraw();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDraw();

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initDraw();
    }

    private void initDraw() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(1.0f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0, height / 2, width, height / 2, paint);
    }


    int mi_top;
    int mi_bottom;
    int mi_width;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        int offsetX = x - lastX;
        int offsetY = y - lastY;

        ViewGroup mViewGroup = (ViewGroup) getParent();

        int pWidth = mViewGroup.getWidth();
        int pHeight = mViewGroup.getHeight();

        int left = getLeft() + offsetX;
        left = left <= 0 ? 0 : left;

        int top = getTop() + offsetY;
        top = top <= 0 ? 0 : top;

        int right = getRight() + offsetX;
        right = right >= pWidth ? pWidth : right;

        int bottom = getBottom() + offsetY;
        bottom = bottom >= pHeight ? pHeight : bottom;

        if (top == 0) {
            bottom = getHeight();
        }
        if (left == 0) {
            right = getWidth();
        }
        if (right == pWidth) {
            left = pWidth - getWidth();
        }
        if (bottom == pHeight) {
            top = pHeight - getHeight();
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                layout(left, top, right, bottom);
                break;

            case MotionEvent.ACTION_UP:
                handler.removeMessages(0);
                handler.removeMessages(1);
                if (rawX <= pWidth / 2) {//左
                    layout(0, top, getWidth( ), bottom);
                    handler.sendEmptyMessageDelayed(0, 3000);
                } else {//右
                    layout(pWidth - getWidth(), top, pWidth, bottom);
                    handler.sendEmptyMessageDelayed(1, 3000);
                }
                mi_top = top;
                mi_bottom = bottom;
                mi_width = pWidth;

                break;

        }
        return true;
    }


}
