package org.gongming.uikit.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.gongming.common.cache.CacheData;
import org.gongming.common.cache.CacheManager;
import org.gongming.common.cache.CacheUtils;
import org.gongming.uikit.R;

import butterknife.ButterKnife;

public class RootFlipperFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_flipper_layout, null);

        final TextView textView = ButterKnife.findById(view, R.id.textView);
        final Button button = ButterKnife.findById(view, R.id.button);
        final Button testBtn = ButterKnife.findById(view, R.id.testBtn);

        CacheData<Integer> lastCached = CacheManager.getInstance().getCache(CacheUtils.KEY_TEST);
        if (lastCached != null) textView.setText("Last cache = " + lastCached.getData());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = (int) (Math.random() * 10);
                CacheData<Integer> cacheData = new CacheData<Integer>(CacheUtils.KEY_TEST, number, CacheUtils.EXPIRATION_HOUR);
                CacheManager.getInstance().addCache(cacheData);
                Toast.makeText(getActivity(), "This number is " + number, Toast.LENGTH_SHORT).show();
            }
        });


        initPopupWindow();


        final PopupWindow window = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(inflater.inflate(R.layout.popwindow, null));
        testBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (window.isShowing()) {
                    window.dismiss();
                    return;
                }
                window.getContentView().measure(0, 0);
                int xOffset =  testBtn.getMeasuredWidth() - window.getContentView().getMeasuredWidth();
                window.showAsDropDown(testBtn, xOffset, 5);
            }
        });


        return view;
    }

    private void initPopupWindow() {


    }


}
