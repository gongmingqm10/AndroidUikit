package org.gongming.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout {

    private final static float TRANSLATE_SPEED = 4.0f;
    private OnSwipeEndListener swipeEndListener;
    private boolean isInit = true;
    private int SWIPE_THRESHOLD;
    private int currentIndex = -1;
    private int totalHeight;
    private float lastRawX, lastRawY;
    private float touchDownX = 0f, touchDownY = 0f;

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInit) {
            totalHeight = bottom - top;
            currentIndex = getChildCount() - 1;
            SWIPE_THRESHOLD = totalHeight / 4;
            isInit = !isInit;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float deltaX, deltaY;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getRawX();
                touchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = event.getRawX() - lastRawX;
                deltaY = event.getRawY() - lastRawY;
                if (Math.abs(deltaY) < Math.abs(deltaX))
                    return true;
                move(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                deltaX = event.getRawX() - touchDownX;
                deltaY = event.getRawY() - touchDownY;
                if (Math.abs(deltaY) < Math.abs(deltaX))
                    return true;
                if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                    swipe(deltaY);
                } else {
                    reset(deltaY);
                }
                break;
        }
        lastRawX = event.getRawX();
        lastRawY = event.getRawY();

        return true;
    }

    private void reset(float deltaY) {
        Direction direction = deltaY < 0 ? Direction.TOP : Direction.BOTTOM;
        if (reachedEnd(direction)) return;

        View currentChild = getChildAt(currentIndex);
        View nextChild = getChildAt(currentIndex + direction.getValue() * 1);
        currentChild.startAnimation(generateTranslateAnimation(deltaY));
        nextChild.startAnimation(generateTranslateAnimation(deltaY));
        LayoutParams currentLayoutParams = (LayoutParams) currentChild.getLayoutParams();
        LayoutParams nextLayoutParams = (LayoutParams) nextChild.getLayoutParams();

        currentLayoutParams.setMargins(0, 0, 0, 0);
        nextLayoutParams.setMargins(0, -totalHeight * direction.getValue(), 0, totalHeight * direction.getValue());

        currentChild.setLayoutParams(currentLayoutParams);
        nextChild.setLayoutParams(nextLayoutParams);
        currentChild.startAnimation(generateTranslateAnimation(deltaY));
        nextChild.startAnimation(generateTranslateAnimation(deltaY));

    }

    private void move(float deltaY) {
        Direction direction = deltaY < 0 ? Direction.TOP : Direction.BOTTOM;
        if (reachedEnd(direction)) return;

        View currentChild = getChildAt(currentIndex);
        View nextChild = getChildAt(currentIndex + direction.getValue() * 1);   //   -1   +1
        LayoutParams currentLayoutParams = (LayoutParams) currentChild.getLayoutParams();
        LayoutParams nextLayoutParams = (LayoutParams) nextChild.getLayoutParams();

        int relativePosition = (deltaY < 0) ? currentLayoutParams.bottomMargin : currentLayoutParams.topMargin;
        currentLayoutParams.topMargin += deltaY;
        currentLayoutParams.bottomMargin -= deltaY;
        nextLayoutParams.topMargin = (relativePosition - totalHeight) * direction.getValue();
        nextLayoutParams.bottomMargin = -nextLayoutParams.topMargin;

        currentChild.setLayoutParams(currentLayoutParams);
        nextChild.setLayoutParams(nextLayoutParams);
    }

    public void swipe(float deltaY) {
        Direction direction = (deltaY < 0) ? Direction.TOP : Direction.BOTTOM;

        if (reachedTop(direction)) {
            if (swipeEndListener != null) swipeEndListener.swipeToTop(this);
            return;
        }
        if (reachedBottom(direction)) {
            if (swipeEndListener != null) swipeEndListener.swipeToBottom(this);
            return;
        }

        final View currentChild = getChildAt(currentIndex);
        final View nextChild = getChildAt(currentIndex + direction.getValue() * 1);
        final LayoutParams currentLayoutParams = (LayoutParams) currentChild.getLayoutParams();
        final LayoutParams nextLayoutParams = (LayoutParams) nextChild.getLayoutParams();

        TranslateAnimation currentAnimation = generateTranslateAnimation(nextLayoutParams.topMargin);
        TranslateAnimation nextAnimation = generateTranslateAnimation(nextLayoutParams.topMargin);

        currentLayoutParams.setMargins(0, totalHeight * direction.getValue(), 0, -totalHeight * direction.getValue());
        currentChild.setLayoutParams(currentLayoutParams);
        nextLayoutParams.setMargins(0, 0, 0, 0);
        nextChild.setLayoutParams(nextLayoutParams);

        currentChild.startAnimation(currentAnimation);
        nextChild.startAnimation(nextAnimation);

        currentIndex += direction.getValue();

    }



    private boolean reachedBottom(Direction direction) {
        return direction == Direction.BOTTOM && currentIndex + 1 >= getChildCount();
    }

    private boolean reachedTop(Direction direction) {
        return direction == Direction.TOP && currentIndex < 1;
    }

    private boolean reachedEnd(Direction direction) {
        return reachedTop(direction) || reachedBottom(direction);
    }

    private TranslateAnimation generateTranslateAnimation(float fromYDelta) {
        TranslateAnimation currentAnimation = new TranslateAnimation(0, 0, fromYDelta, 0);
        int duration = (int) (Math.abs(fromYDelta) / TRANSLATE_SPEED);
        currentAnimation.setDuration(duration);
        currentAnimation.setFillAfter(false);
        return currentAnimation;
    }

    public void setOnSwipeEndListener(OnSwipeEndListener swipeEndListener) {
        this.swipeEndListener = swipeEndListener;
    }

    public enum Direction {
        TOP(-1), BOTTOM(1);

        private int value;

        private Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface OnSwipeEndListener {
        void swipeToTop(View view);

        void swipeToBottom(View view);
    }

}
