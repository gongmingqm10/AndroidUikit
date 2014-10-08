package org.gongming.common.swipelist;

import android.content.Context;
import android.graphics.Canvas;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by minggong on 9/25/14.
 */
public class CustomView extends View {

    private static final String TAG = "gongmingqm10";

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "--onDraw--");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "--onLayout--" + left + " - " + top + " - " + right + " - " + bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "--onMeasure--" + widthMeasureSpec + " - " + heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch(widthMode) {
            case MeasureSpec.EXACTLY:
                Log.i(TAG, "--Exactly--width--" + width);
                break;
            case MeasureSpec.AT_MOST:
                Log.i(TAG, "--AtMost--width--" + width);
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.i(TAG, "-UnSpecified--width--" + width);
                break;
        }

        switch(heightMode) {
            case MeasureSpec.EXACTLY:
                Log.i(TAG, "--Exactly--height--" + height);
                break;
            case MeasureSpec.AT_MOST:
                Log.i(TAG, "--AtMost--height--" + height);
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.i(TAG, "-UnSpecified--height--" + height);
                break;
        }

        setMeasuredDimension(width, height);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "--onTouchEvent--" + event.getX() + " - " + event.getY());
        return super.onTouchEvent(event);
    }
}
