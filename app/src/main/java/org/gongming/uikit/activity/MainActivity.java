package org.gongming.uikit.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.gongming.uikit.R;

import butterknife.ButterKnife;
import butterknife.InjectViews;

public class MainActivity extends FragmentActivity {

    @InjectViews({R.id.grandParent, R.id.parent, R.id.son})
    View [] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        ButterKnife.inject(this);
        views[0].setOnTouchListener(new TouchListener("grantParent"));
        views[1].setOnTouchListener(new TouchListener("parent"));
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureListener());

        views[2].setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("gongmingqm10", "--onTouch()--" + "son");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    private class TouchListener implements View.OnTouchListener {

        private final String flag;

        public TouchListener(String flag) {
            this.flag = flag;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i("gongmingqm10", "--onTouch()--" + flag);
            return true;

        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;
        private static final int SWIPE_THRESHOLD_VELOCITY = 20;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.i("gongmingqm10", "--onFling()--");

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if ( Math.abs(diffX) > Math.abs(diffY) &&
                    Math.abs(diffX) > SWIPE_THRESHOLD &&
                    velocityX > SWIPE_THRESHOLD_VELOCITY) {
                if (diffX > 0) {
                    Toast.makeText(MainActivity.this, "swipe to left", Toast.LENGTH_SHORT).show();
                    Log.i("gongmingqm10", "--swipeLeft()--");
                } else {
                    Toast.makeText(MainActivity.this, "swipe to right", Toast.LENGTH_SHORT).show();
                    Log.i("gongmingqm10", "--swipeRight()--");
                }
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("gongmingqm10", "--onDown()--");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("gongmingqm10", "--onLongPress()--");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("gongmingqm10", "--onScroll()--");
            return false;
        }

    }

}
