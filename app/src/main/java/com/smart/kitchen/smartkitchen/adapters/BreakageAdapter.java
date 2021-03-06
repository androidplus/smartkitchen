package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import java.util.List;

public class BreakageAdapter extends BaseAdapter {
    private Context context;
    private List<OrderGoods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class ViewHolder {
        LinearLayout iv_dec;
        TextView shop_count;
        TextView shop_money;
        TextView shop_name;

        public ViewHolder(View view) {
            this.iv_dec = (LinearLayout) view.findViewById(R.id.iv_dec);
            this.iv_dec.setVisibility(View.VISIBLE);
            this.shop_money = (TextView) view.findViewById(R.id.shop_money);
            this.shop_name = (TextView) view.findViewById(R.id.shop_name);
            this.shop_count = (TextView) view.findViewById(R.id.shop_count);
            this.shop_money.setTextSize(14.0f);
            this.shop_name.setTextSize(14.0f);
            this.shop_count.setTextSize(14.0f);
        }
    }

    public BreakageAdapter(Context context, List<OrderGoods> list) {
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

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_shop_adapter, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OrderGoods orderGoods = this.list.get(i);
        viewHolder.shop_count.setText("" + orderGoods.getCount());
        viewHolder.shop_money.setText("" + orderGoods.getGoods().getMoney());
        viewHolder.shop_name.setText(orderGoods.getGoods().getName());
        viewHolder.iv_dec.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BreakageAdapter.this.listener != null) {
                    BreakageAdapter.this.listener.onItemClick(i);
                }
            }
        });
        return view;
    }
}
