package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.smart.kitchen.smartkitchen.R;
import java.util.List;

public class SizeAdapter extends BaseAdapter {
    private Context context;
    private List list;

    private class ViewHolder {
        public ViewHolder(View view) {
        }
    }

    public SizeAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.beifen_item, null);
            view.setTag(new ViewHolder(view));
            return view;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        return view;
    }
}
