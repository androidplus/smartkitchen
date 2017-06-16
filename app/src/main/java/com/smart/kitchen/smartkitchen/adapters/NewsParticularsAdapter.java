package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import java.util.List;

public class NewsParticularsAdapter extends BaseAdapter {
    private Context context;
    private List<OrderGoods> list;

    private class ViewHolder {
        TextView tvOrderCaipin;
        TextView tvOrderCount;
        TextView tvOrderMoney;

        public ViewHolder(View view) {
            this.tvOrderCaipin = (TextView) view.findViewById(R.id.tv_order_right_caipin);
            this.tvOrderCount = (TextView) view.findViewById(R.id.tv_order_right_count);
            this.tvOrderMoney = (TextView) view.findViewById(R.id.tv_order_right_money);
        }
    }

    public NewsParticularsAdapter(Context context, List<OrderGoods> list) {
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
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (((OrderGoods) this.list.get(i)).getGoods().getName() != null) {
            viewHolder.tvOrderCaipin.setText(((OrderGoods) this.list.get(i)).getGoods().getName());
        }
        viewHolder.tvOrderCount.setText(((OrderGoods) this.list.get(i)).getCount() + "");
        if (((OrderGoods) this.list.get(i)).getGoodsize() != null) {
            viewHolder.tvOrderMoney.setText(((OrderGoods) this.list.get(i)).getGoodsize().getSale_price() + "");
        } else {
            viewHolder.tvOrderMoney.setText(((OrderGoods) this.list.get(i)).getGoods().getMoney().toString());
        }
        return view;
    }
}
