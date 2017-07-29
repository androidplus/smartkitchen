package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.OrderActivity;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import java.util.List;

public class RightOrderAdapter1 extends BaseAdapter {
    private Context context;
    private List<OrderGoods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i, int i2);
    }

    private class ViewHolder {
        TextView tvOrderCaipin;
        TextView tvOrderCount;
        TextView tvOrderMoney;
        TextView tvOrderStatus;

        public ViewHolder(View view) {
            this.tvOrderCaipin = (TextView) view.findViewById(R.id.tv_order_caipin);
            this.tvOrderCount = (TextView) view.findViewById(R.id.tv_order_count);
            this.tvOrderMoney = (TextView) view.findViewById(R.id.tv_order_money);
            this.tvOrderStatus = (TextView) view.findViewById(R.id.tv_order_status);
        }
    }

    public RightOrderAdapter1(Context context, List<OrderGoods> list) {
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
            view = LayoutInflater.from(this.context).inflate(R.layout.order_right_item1, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OrderGoods orderGoods = this.list.get(i);
        if (TextUtils.isEmpty(orderGoods.getMark()) || orderGoods.getMark() == null) {
            viewHolder.tvOrderCaipin.setText(orderGoods.getGoods().getName());
        } else if (orderGoods.getMark().length() < 10) {
            viewHolder.tvOrderCaipin.setText(orderGoods.getGoods().getName() + "(" + orderGoods.getMark() + ")");
        } else {
            viewHolder.tvOrderCaipin.setText(orderGoods.getGoods().getName() + "(" + orderGoods.getMark().substring(0, 10) + "..." + ")");
        }
        viewHolder.tvOrderCount.setText(orderGoods.getCount() + "");
        Log.e("getViewsss", "getView: " + orderGoods.getGoodsize());
        if (orderGoods.getGoodsize() == null) {
            viewHolder.tvOrderMoney.setText(String.valueOf(orderGoods.getGoods().getMoney()));
        } else {
            viewHolder.tvOrderMoney.setText(String.valueOf(orderGoods.getGoodsize().getSale_price()));
        }
        final int status = orderGoods.getStatus();
        if (status == 0) {
            viewHolder.tvOrderStatus.setText("未出单");
            viewHolder.tvOrderStatus.setBackgroundResource(R.drawable.corner_red);
        } else if (status == 1) {
            viewHolder.tvOrderStatus.setText("已上菜");
            viewHolder.tvOrderStatus.setBackgroundResource(R.drawable.corner_gray);
        } else {
            viewHolder.tvOrderStatus.setText("已退菜");
            viewHolder.tvOrderStatus.setBackgroundResource(R.drawable.corner_white);
        }
        viewHolder.tvOrderStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (status != 4 && RightOrderAdapter1.this.listener != null) {
                    RightOrderAdapter1.this.listener.onItemClick(i, status);
                }
            }
        });
        viewHolder.tvOrderCaipin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RightOrderAdapter1.this.list.get(i) != null) {
                    new DialogUtils((OrderActivity) RightOrderAdapter1.this.context).showParticulars(view, (OrderGoods) RightOrderAdapter1.this.list.get(i));
                }
            }
        });
        return view;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int i) {
        return i % 2;
    }

    public boolean isEnabled(int i) {
        if ((list.get(i)).getStatus() == 4) {
            return false;
        }
        return true;
    }
}
