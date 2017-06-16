package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import java.util.List;

public class StandardAdapter extends BaseAdapter {
    private static final String TAG = "StandardAdapter";
    private Context context;
    private int flag = 0;
    private List list;
    private int select;

    private class ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.item_text);
        }

        public void reset() {
            this.textView.setTextColor(StandardAdapter.this.context.getResources().getColor(R.color.black));
            this.textView.setBackground(StandardAdapter.this.context.getResources().getDrawable(R.drawable.corner_gray));
        }

        public void checked() {
            this.textView.setTextColor(StandardAdapter.this.context.getResources().getColor(R.color.white));
            this.textView.setBackground(StandardAdapter.this.context.getResources().getDrawable(R.drawable.corner_red));
        }
    }

    public StandardAdapter(Context context, List list, int i) {
        this.context = context;
        this.list = list;
        this.flag = i;
        if (this.list.size() > 0) {
            this.select = 0;
        }
    }

    public void setSelectFoodType(int i) {
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
            view = LayoutInflater.from(this.context).inflate(R.layout.standar_item, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (this.select == i) {
            viewHolder.checked();
        }
        if (this.flag == 0) {
            viewHolder.textView.setText(((GoodSize) this.list.get(i)).getSpec_name());
            if (((GoodSize) this.list.get(i)).getCount().intValue() == 0) {
                viewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.white));
                viewHolder.textView.setBackground(this.context.getResources().getDrawable(R.drawable.corner_black));
            }
        } else {
            viewHolder.textView.setText(((GoodTaste) this.list.get(i)).getTastename());
        }
        return view;
    }
}
