package org.gongming.uikit.fragment;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.gongming.common.PullToRefreshListView;
import org.gongming.uikit.R;
import org.gongming.uikit.adapter.RootListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RootListFragment extends Fragment {

    @InjectView(R.id.listView)
    PullToRefreshListView listView;

    private RootListAdapter adapter;

    private Animation textFadeOut, textFadeIn, btnFadeIn, btnFadeOut;

    private final static String[] data = new String[]{
            "This is the Item",
            "I have to make breakfast",
            "What will tomorrow happen",
            "Do you think it's suitable",
            "God bless me",
            "This is the Item",
            "I have to make breakfast",
            "What will tomorrow happen",
            "Do you think it's suitable",
            "God bless me",
            "This is the Item",
            "I have to make breakfast",
            "What will tomorrow happen",
            "Do you think it's suitable"

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_list_layout, null);
        ButterKnife.inject(this, view);
//        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));

        adapter = new RootListAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        textFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.text_move_out);
        btnFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_move_in);
        textFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.text_move_in);
        btnFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_move_out);


        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureListener());
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });

        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        listView.post(new Runnable() {
                           @Override
                           public void run() {
                               listView.onRefreshComplete();
                           }
                       }) ;
                    }
                }).start();
            }
        });



        return view;
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 150;
        private static final int SWIPE_THRESHOLD_VELOCITY = 30;

        private View mDownView;

        private View mainLayout, buttonLayout;
        private boolean isMoving = false;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("gongmingqm10", "--onFling()--");


            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY) &&
                    Math.abs(diffX) > SWIPE_THRESHOLD &&
                    Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                TextView textView = (TextView) mDownView.findViewById(R.id.textView);

                mainLayout = mDownView.findViewById(R.id.mainLayout);
                buttonLayout = mDownView.findViewById(R.id.buttonLayout);
//                Toast.makeText(getActivity(), textView.getText().toString(), Toast.LENGTH_SHORT).show();

                if (diffX > 0) {
//                    Toast.makeText(getActivity(), "swipe to right", Toast.LENGTH_SHORT).show();

//                    Log.i("gongmingqm10", "--swipeRight()--");
                } else {
//                    Toast.makeText(getActivity(), "swipe to left", Toast.LENGTH_SHORT).show();
                    mainLayout.startAnimation(textFadeOut);
                    buttonLayout.startAnimation(btnFadeIn);
                    isMoving = true;

//                    Log.i("gongmingqm10", "--swipeLeft()--");
                }
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Rect rect = new Rect();
            int[] location = new int[2];
            listView.getLocationOnScreen(location);
            int x = (int) e.getRawX() - location[0];
            int y = (int) e.getRawY() - location[1];
            View child;
            for (int i = 0; i < listView.getChildCount(); i++) {
                child = listView.getChildAt(i);
                child.getHitRect(rect);
                if (rect.contains(x, y)) {
                    mDownView = child;
                    break;
                }
            }

            if (mDownView != null && mainLayout != null && isMoving) {
                mainLayout.startAnimation(textFadeIn);
                buttonLayout.startAnimation(btnFadeOut);
            }
            isMoving = false;

            return false;
        }
    }

}
