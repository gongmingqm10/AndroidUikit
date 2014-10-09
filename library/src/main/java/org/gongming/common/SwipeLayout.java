package org.gongming.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SwipeLayout extends FrameLayout {

    //TODO this view should support more than two children and care more about the margin of its children
    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int SWIPE_THRESHOLD;
    private int currentIndex = -1;

    private int totalHeight;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        totalHeight = bottom - top;
        if (currentIndex == -1) currentIndex = getChildCount() - 1;
        SWIPE_THRESHOLD = totalHeight / 4;
    }


    private float lastRawX, lastRawY;
    private float touchDownX = 0f, touchDownY = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //TODO This method need largely refactored.

        float deltaX, deltaY;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getRawX();
                touchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = event.getRawX() - lastRawX;
                deltaY = event.getRawY() - lastRawY;
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    View currentChild = getChildAt(currentIndex);
                    LayoutParams currentLayoutParams = (LayoutParams) currentChild.getLayoutParams();
                    if (deltaY > 0) {
                        if (currentIndex + 1 < getChildCount()) {
                            View nextChild = getChildAt(currentIndex + 1);
                            LayoutParams nextLayoutParams = (LayoutParams) nextChild.getLayoutParams();
                            currentLayoutParams.topMargin += deltaY;
                            currentLayoutParams.bottomMargin -= deltaY;
                            nextLayoutParams.bottomMargin = totalHeight - currentLayoutParams.topMargin;
                            nextLayoutParams.topMargin = currentLayoutParams.topMargin - totalHeight;

                            nextChild.setLayoutParams(nextLayoutParams);

                            currentChild.setLayoutParams(currentLayoutParams);

                        } else {
                            //TODO View touches the bottom. No child for next.
                        }
                    } else {
                        if (currentIndex > 0) {
                            View previousChild = getChildAt(currentIndex - 1);
                            LayoutParams previousLayoutParams = (LayoutParams) previousChild.getLayoutParams();
                            currentLayoutParams.bottomMargin -= deltaY;
                            currentLayoutParams.topMargin += deltaY;

                            previousLayoutParams.topMargin = totalHeight - currentLayoutParams.bottomMargin;
                            previousLayoutParams.bottomMargin = currentLayoutParams.bottomMargin - totalHeight;

                            previousChild.setLayoutParams(previousLayoutParams);

                            currentChild.setLayoutParams(currentLayoutParams);

                        } else {
                            //TODO View touches the top. No child for previous.
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                deltaX = event.getRawX() - touchDownX;
                deltaY = event.getRawY() - touchDownY;
                if (Math.abs(deltaY) < Math.abs(deltaX)) return true;

                if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                    if (deltaY > 0) {
                        swipeNext();
                    } else {
                        swipePrevious();
                    }
                } else {
                    //TODO restore to currentView position
                    if (deltaY > 0) {

                    } else {

                    }
                }
                break;
        }

        lastRawX = event.getRawX();
        lastRawY = event.getRawY();

        return true;
    }

    private static final String TAG = "gongmingqm10";

    public void swipePrevious() {
        if (currentIndex < 1) return;

        final View currentChild = getChildAt(currentIndex);
        final View previousChild = getChildAt(currentIndex - 1);

        final LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
        final LayoutParams previousParams = (LayoutParams) previousChild.getLayoutParams();
        TranslateAnimation currentAnimation = new TranslateAnimation(0, 0, previousParams.topMargin, 0);
        TranslateAnimation previousAnimation = new TranslateAnimation(0, 0, previousParams.topMargin, 0);

        currentAnimation.setDuration(300);
        previousAnimation.setDuration(300);
        currentAnimation.setFillAfter(false);
        previousAnimation.setFillAfter(false);


        currentParams.topMargin = -totalHeight;
        currentParams.bottomMargin = totalHeight;
        currentChild.setLayoutParams(currentParams);
        currentChild.startAnimation(currentAnimation);

        previousParams.topMargin = 0;
        previousParams.bottomMargin = 0;
        previousChild.setLayoutParams(previousParams);
        previousChild.startAnimation(previousAnimation);

        currentIndex -= 1;
    }

    public void swipeNext() {
        if (currentIndex + 1 >= getChildCount()) return;

        final View currentChild = getChildAt(currentIndex);
        final View nextChild = getChildAt(currentIndex + 1);

        final LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
        final LayoutParams nextParams = (LayoutParams) nextChild.getLayoutParams();

        final TranslateAnimation currentAnimation = new TranslateAnimation(0, 0, nextParams.topMargin, 0);
        TranslateAnimation nextAnimation = new TranslateAnimation(0, 0, nextParams.topMargin, 0);

        currentAnimation.setDuration(300);
        currentAnimation.setFillAfter(false);
        nextAnimation.setDuration(300);
        nextAnimation.setFillAfter(false);

        currentParams.topMargin = totalHeight;
        currentParams.bottomMargin = -totalHeight;
        currentChild.setLayoutParams(currentParams);
        currentChild.startAnimation(currentAnimation);

        nextParams.topMargin = 0;
        nextParams.bottomMargin = 0;
        nextChild.setLayoutParams(nextParams);
        nextChild.startAnimation(nextAnimation);

        currentIndex += 1;
    }

}
