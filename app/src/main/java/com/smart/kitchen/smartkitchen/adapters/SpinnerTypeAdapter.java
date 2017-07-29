package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.FromType;
import com.smart.kitchen.smartkitchen.utils.Contants;
import java.util.ArrayList;
import java.util.List;

public class SpinnerTypeAdapter extends BaseAdapter {
    private Context context;
    private List<FromType> list = new ArrayList();
    private int select = -1;

    private class ViewHolder {
        TextView text;

        public ViewHolder(View view) {
            this.text = (TextView) view.findViewById(R.id.text);
        }

        public void reset() {
            this.text.setTextColor(Contants.getColor(SpinnerTypeAdapter.this.context, R.color.black));
            this.text.setBackgroundColor(Contants.getColor(SpinnerTypeAdapter.this.context, R.color.gray));
        }

        public void checked() {
            this.text.setTextColor(Contants.getColor(SpinnerTypeAdapter.this.context, R.color.white));
            this.text.setBackgroundColor(Contants.getColor(SpinnerTypeAdapter.this.context, R.color.red));
        }
    }

    public SpinnerTypeAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    public void setSelect(int i) {
        this.select = i;
        notifyDataSetChanged();
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
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.spinner_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (this.select == i) {
            viewHolder.checked();
        }
        viewHolder.text.setText("" + (list.get(i)).getName());
        return view;
    }
}
