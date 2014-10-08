package org.gongming.common;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SwipeLayout extends FrameLayout {

    private Animation upAnimOut, upAnimIn, downAnimIn, downAnimOut;
    private Context context;

    //TODO this view should support more than two children and care more about the margin of its children
    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(Context context) {
        upAnimOut = AnimationUtils.loadAnimation(context, R.anim.swipe_up_out);
        upAnimIn = AnimationUtils.loadAnimation(context, R.anim.swipe_up_in);
        downAnimIn = AnimationUtils.loadAnimation(context, R.anim.swipe_down_in);
        downAnimOut = AnimationUtils.loadAnimation(context, R.anim.swipe_down_out);
        this.context = context;
    }



    private float previousX, previousY;
    private int SWIPE_THRESHOLD;
    private int currentIndex;

    private int totalWidth, totalHeight;
    private LayoutParams layoutParams;



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        currentIndex = getChildCount() - 1;
        totalWidth = right - left;
        totalHeight = bottom - top;
        layoutParams = (LayoutParams) getLayoutParams();
        SWIPE_THRESHOLD = totalHeight / 4;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //TODO This method need largely refactored.

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                previousX = event.getRawX();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                float deltaY = event.getRawY() - previousY;
                float deltaX = event.getRawX() - previousX;
                if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > SWIPE_THRESHOLD) {
                    if (deltaY > 0)
                        swipeNext();
                    else
                        swipePrevious();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float distanceY = event.getRawY() - previousY;
                float distanceX = event.getRawX() - previousX;

                if (Math.abs(distanceY) > Math.abs(distanceX)) {
                    if (distanceY > 0) {
                        View currentChild = getChildAt(0);
                        View nextChild = getChildAt(1);

                        LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
                        LayoutParams nextParams = (LayoutParams) nextChild.getLayoutParams();

                        currentParams.topMargin = 0 + (int)distanceY;
                        nextParams.bottomMargin = totalHeight - (int) distanceY;

                        currentChild.setLayoutParams(currentParams);
                        nextChild.setLayoutParams(nextParams);


                    } else {
                        View currentChild = getChildAt(1);
                        View previousChild = getChildAt(0);
                        LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
                        LayoutParams previousParams = (LayoutParams) previousChild.getLayoutParams();
                        currentParams.bottomMargin = 0 + (int)(-distanceY);
                        currentParams.topMargin = (int)distanceY;

                        previousParams.topMargin = totalHeight - (int)(-distanceY);
                        currentChild.setLayoutParams(currentParams);
                        previousChild.setLayoutParams(previousParams);
                    }
                }

                invalidate();

                break;
        }
        return true;
    }

    //TODO swipePrevious and swipeNext should be refactored.

    public void swipePrevious() {
//        if (currentIndex > 0) {
//            getChildAt(currentIndex).startAnimation(upAnimOut);
//            currentIndex -= 1;
//            getChildAt(currentIndex).startAnimation(upAnimIn);
//        }

        View currentChild = getChildAt(1);
        View previousChild = getChildAt(0);

        LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
        LayoutParams previousParams = (LayoutParams) previousChild.getLayoutParams();
        TranslateAnimation currentAnimation = new TranslateAnimation(0, 0, currentParams.topMargin, totalHeight);
        TranslateAnimation previousAnimation = new TranslateAnimation(0, 0, previousParams.topMargin, 0);

        currentAnimation.setDuration(400);
        previousAnimation.setDuration(400);
        currentAnimation.setFillAfter(true);
        previousAnimation.setFillAfter(true);

        currentChild.startAnimation(currentAnimation);
        previousChild.startAnimation(previousAnimation);

        currentParams.topMargin = totalHeight;
        previousParams.topMargin = 0;
//
        currentChild.setLayoutParams(currentParams);
        previousChild.setLayoutParams(previousParams);



    }

    public void swipeNext() {
//        if (currentIndex + 1 < getChildCount()) {
//            getChildAt(currentIndex).startAnimation(downAnimOut);
//            currentIndex += 1;
//            getChildAt(currentIndex).startAnimation(downAnimIn);
//        }

        View currentChild = getChildAt(0);
        View nextChild = getChildAt(1);

        LayoutParams currentParams = (LayoutParams) currentChild.getLayoutParams();
        LayoutParams nextParams = (LayoutParams) nextChild.getLayoutParams();

        TranslateAnimation currentAnimation = new TranslateAnimation(0, 0, currentParams.topMargin, totalHeight);
        TranslateAnimation nextAnimation = new TranslateAnimation(0, 0, nextParams.topMargin, 0);

        currentAnimation.setDuration(400);
        currentAnimation.setFillAfter(true);
        nextAnimation.setDuration(400);
        nextAnimation.setFillAfter(true);

        currentChild.startAnimation(currentAnimation);
        nextChild.startAnimation(nextAnimation);


        //TODO move this set position method to AnimationEnd listener
        currentParams.topMargin = totalHeight;
        nextParams.topMargin = 0;

        currentChild.setLayoutParams(currentParams);
        nextChild.setLayoutParams(nextParams);

    }

}
