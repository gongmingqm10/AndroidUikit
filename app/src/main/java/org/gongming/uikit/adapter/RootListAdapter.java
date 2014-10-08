package org.gongming.uikit.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.gongming.uikit.R;
import org.gongming.uikit.fragment.RootListFragment;

public class RootListAdapter extends BaseAdapter {

    private String[] data;
    private Activity activity;

    public RootListAdapter(Activity activity, String[] data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null? 0 : data.length;
    }

    @Override
    public Object getItem(int position) {
        return (data == null || position < 0 || position >= data.length) ? null : data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "You clicked delete button", Toast.LENGTH_SHORT).show();
            }
        });

        final String item = data[position];
        holder.textView.setText(item);

        return convertView;
    }

    private class ViewHolder{
        TextView textView;
        Button deleteBtn;
    }
}
