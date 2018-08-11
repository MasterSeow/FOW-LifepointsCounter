package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private List<Integer> entries;
    private Context context;

    public HistoryAdapter(List<Integer> entries, Context context) {
        this.entries = entries;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = convertView == null ? createView(parent) : convertView;
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        int entry = entries.get(position);

        viewHolder.textView.setText(String.valueOf(entry));

        if (position > 0) {
            if (entries.get(position - 1) < entry)
                viewHolder.textView.setBackgroundColor(Color.rgb(200, 255, 200));
            else if (entries.get(position - 1) > entry)
                viewHolder.textView.setBackgroundColor(Color.rgb(255, 200, 200));
        } else
            viewHolder.textView.setBackgroundColor(Color.rgb(200, 200, 255));

        return view;
    }

    private View createView(ViewGroup parent) {

        final View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.textView = view.findViewById(R.id.textview);

        view.setTag(viewHolder);

        return view;
    }

    private static class ViewHolder {
        TextView textView;
    }
}
