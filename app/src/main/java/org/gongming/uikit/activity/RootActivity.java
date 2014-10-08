package org.gongming.uikit.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.gongming.uikit.R;
import org.gongming.uikit.fragment.RootFlipperFragment;
import org.gongming.uikit.fragment.RootListFragment;
import org.gongming.uikit.fragment.RootSwipeFragment;

import butterknife.ButterKnife;
import butterknife.InjectViews;


public class RootActivity extends FragmentActivity {

    @InjectViews({R.id.tab_btn1, R.id.tab_btn2, R.id.tab_btn3})
    View[] tabBtns;

    private RootFlipperFragment flipperFragment;
    private RootListFragment listFragment;
    private RootSwipeFragment swipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        for (View view : tabBtns) {
            view.setOnClickListener(new TabClickListener());
        }
        findViewById(R.id.tab_btn1).performClick();
    }

    private class TabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            transactFragment(view.getId());
            for (View tabBtn : tabBtns) {
                tabBtn.setSelected(view == tabBtn);
            }

        }
    }

    private void transactFragment(int id) {
        Fragment fragment = getFragmentById(id);
        if (fragment == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    private Fragment getFragmentById(int id) {
        switch (id) {
            case R.id.tab_btn1:
                if (flipperFragment == null) {
                    flipperFragment = new RootFlipperFragment();
                }
                return flipperFragment;
            case R.id.tab_btn2:
                if (listFragment == null) {
                    listFragment = new RootListFragment();
                }
                return listFragment;
            case R.id.tab_btn3:
                if (swipeFragment == null) {
                    swipeFragment = new RootSwipeFragment();
                }
                return swipeFragment;
        }
        return null;
    }


}
