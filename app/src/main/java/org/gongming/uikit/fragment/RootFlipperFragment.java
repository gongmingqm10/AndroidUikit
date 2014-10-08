package org.gongming.uikit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.gongming.common.swipelist.SwipeTouchListener;
import org.gongming.uikit.R;

/**
 * Created by minggong on 9/25/14.
 */
public class RootFlipperFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_flipper_layout, null);
        view.setOnTouchListener(swipeListener);
        return view;
    }

    private SwipeTouchListener swipeListener = new SwipeTouchListener() {

        @Override
        public void swipeLeft() {
            Toast.makeText(getActivity(), "Swipe to left", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void swipeRight() {
            Toast.makeText(getActivity(), "Swipe to right", Toast.LENGTH_SHORT).show();
        }
    };



}
