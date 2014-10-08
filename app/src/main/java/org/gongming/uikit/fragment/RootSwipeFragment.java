package org.gongming.uikit.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.gongming.common.FlowLayout;
import org.gongming.uikit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RootSwipeFragment extends Fragment {

    @InjectView(R.id.flowLayout)
    FlowLayout flowLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_swipe_layout, null);
        ButterKnife.inject(this, view);
        flowLayout.invalidate();
        flowLayout.setOnItemClickListener(flowItemClickListener);
        return view;
    }

    private View.OnClickListener flowItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              if (v instanceof TextView) {
                  TextView view = (TextView) v;
                  Toast.makeText(getActivity(), "You clicked " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
              }
        }
    };
}
