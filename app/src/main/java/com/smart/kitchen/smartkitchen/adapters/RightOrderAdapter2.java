package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import java.util.List;

public class RightOrderAdapter2 extends BaseAdapter {
    private Context context;
    private List<OrderGoods> list;

    private class ViewHolder {
        TextView tv_order_right_caipin;
        TextView tv_order_right_count;
        TextView tv_order_right_money;

        public ViewHolder(View view) {
            this.tv_order_right_caipin = (TextView) view.findViewById(R.id.tv_order_right_caipin);
            this.tv_order_right_count = (TextView) view.findViewById(R.id.tv_order_right_count);
            this.tv_order_right_money = (TextView) view.findViewById(R.id.tv_order_right_money);
        }
    }

    public RightOrderAdapter2(Context context, List<OrderGoods> list) {
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
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.order_right_item2, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OrderGoods orderGoods = this.list.get(i);
        if (TextUtils.isEmpty(orderGoods.getMark()) || orderGoods.getMark() == null) {
            viewHolder.tv_order_right_caipin.setText(orderGoods.getGoods().getName());
        } else if (orderGoods.getMark().length() < 10) {
            viewHolder.tv_order_right_caipin.setText(orderGoods.getGoods().getName() + "(" + orderGoods.getMark() + ")");
        } else {
            viewHolder.tv_order_right_caipin.setText(orderGoods.getGoods().getName() + "(" + orderGoods.getMark().substring(0, 10) + "..." + ")");
        }
        viewHolder.tv_order_right_count.setText(orderGoods.getCount() + "");
        if (orderGoods.getGoodsize() == null) {
            viewHolder.tv_order_right_money.setText(String.valueOf(orderGoods.getGoods().getMoney()));
        } else {
            viewHolder.tv_order_right_money.setText(String.valueOf(orderGoods.getGoodsize().getSale_price()));
        }
        return view;
    }
}
