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

public class TableShopAdapter extends BaseAdapter {
    private Context context;
    private List<OrderGoods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class ViewHolder {
        TextView shop_count;
        TextView shop_money;
        TextView shop_name;

        public ViewHolder(View view) {
            this.shop_money = (TextView) view.findViewById(R.id.shop_money);
            this.shop_name = (TextView) view.findViewById(R.id.shop_name);
            this.shop_count = (TextView) view.findViewById(R.id.shop_count);
            this.shop_money.setTextSize(14.0f);
            this.shop_name.setTextSize(14.0f);
            this.shop_count.setTextSize(14.0f);
        }
    }

    public TableShopAdapter(Context context, List<OrderGoods> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
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
            view = LayoutInflater.from(this.context).inflate(R.layout.item_shop_adapter, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (((OrderGoods) this.list.get(i)).getGoodsize() == null) {
            viewHolder.shop_money.setText(String.valueOf(((OrderGoods) this.list.get(i)).getGoods().getMoney()));
        } else {
            viewHolder.shop_money.setText(String.valueOf(((OrderGoods) this.list.get(i)).getGoodsize().getSale_price()));
        }
        viewHolder.shop_count.setText("" + ((OrderGoods) this.list.get(i)).getCount());
        viewHolder.shop_name.setText(((OrderGoods) this.list.get(i)).getGoods().getName());
        return view;
    }
}
