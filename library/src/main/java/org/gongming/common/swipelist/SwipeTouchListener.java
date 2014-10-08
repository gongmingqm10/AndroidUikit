package org.gongming.common.swipelist;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by minggong on 9/25/14.
 */
public abstract class SwipeTouchListener implements View.OnTouchListener{

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        GestureDetector gestureDetector = new GestureDetector(view.getContext(), new GestureListener());
        return !gestureDetector.onTouchEvent(motionEvent);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;
        private static final int SWIPE_THRESHOLD_VELOCITY = 20;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if ( Math.abs(diffX) > Math.abs(diffY) &&
                    Math.abs(diffX) > SWIPE_THRESHOLD &&
                    velocityX > SWIPE_THRESHOLD_VELOCITY) {
                if (diffX > 0) {
                    swipeLeft();
                } else {
                    swipeRight();
                }
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("gongmingqm10", "--onTouch()--");
            return false;
        }
    }

    public abstract void swipeLeft();

    public abstract void swipeRight();

}
