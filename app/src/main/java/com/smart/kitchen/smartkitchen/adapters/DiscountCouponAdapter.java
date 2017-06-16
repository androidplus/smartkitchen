package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.Conpon;
import com.smart.kitchen.smartkitchen.utils.Contants;
import java.util.List;

public class DiscountCouponAdapter extends BaseAdapter {
    private Context context;
    private List<Conpon> list;
    private int select = -1;

    private class ViewHolder {
        TextView text;

        public ViewHolder(View view) {
            this.text = (TextView) view.findViewById(R.id.text);
        }

        public void reset() {
            this.text.setTextColor(Contants.getColor(DiscountCouponAdapter.this.context, R.color.black));
            this.text.setBackgroundColor(Contants.getColor(DiscountCouponAdapter.this.context, R.color.gray));
        }

        public void checked() {
            this.text.setTextColor(Contants.getColor(DiscountCouponAdapter.this.context, R.color.white));
            this.text.setBackgroundColor(Contants.getColor(DiscountCouponAdapter.this.context, R.color.red));
        }
    }

    public DiscountCouponAdapter(List<Conpon> list, Context context) {
        this.list = list;
        this.context = context;
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
        viewHolder.text.setText(((Conpon) this.list.get(i)).getName());
        return view;
    }
}
