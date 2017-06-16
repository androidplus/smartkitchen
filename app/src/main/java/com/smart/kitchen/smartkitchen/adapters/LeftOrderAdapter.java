package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.utils.Contants;
import java.util.List;

public class LeftOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> list;
    private int select = 0;

    private class ViewHolder {
        CardView cardView;
        TextView ivNewOrderNotification;
        ImageView ivOrderinfoLeft;
        TextView tvArea1;
        TextView tvArea2;
        TextView tvMoney;
        TextView tvNumber;
        TextView tvOrder;
        TextView tvOrderinfoLeft;
        TextView tvTime;

        public ViewHolder(View view) {
            this.ivNewOrderNotification = (TextView) view.findViewById(R.id.iv_new_order_notification);
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.tvMoney = (TextView) view.findViewById(R.id.tv_money);
            this.tvTime = (TextView) view.findViewById(R.id.tv_time);
            this.tvOrder = (TextView) view.findViewById(R.id.tv_order);
            this.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            this.tvOrderinfoLeft = (TextView) view.findViewById(R.id.tv_orderinfo_left);
            this.ivOrderinfoLeft = (ImageView) view.findViewById(R.id.iv_orderinfo_left);
            this.tvArea1 = (TextView) view.findViewById(R.id.tv_area1);
            this.tvArea2 = (TextView) view.findViewById(R.id.tv_area2);
        }

        public void reset() {
            this.cardView.setCardBackgroundColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.white));
            this.tvMoney.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.black));
            this.tvTime.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.black));
            this.tvOrder.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.black));
            this.tvNumber.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.black));
        }

        public void check() {
            this.cardView.setCardBackgroundColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.red));
            this.tvMoney.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.white));
            this.tvTime.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.white));
            this.tvOrder.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.white));
            this.tvNumber.setTextColor(Contants.getColor(LeftOrderAdapter.this.context, R.color.white));
        }
    }

    public LeftOrderAdapter(Context context, List list) {
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
        int i2 = 0;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.left_orderinfo_item, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (this.select == i) {
            viewHolder.check();
        }
        viewHolder.tvNumber.setText((i + 1) + "");
        viewHolder.tvOrder.setText("订单编号：" + ((OrderInfo) this.list.get(i)).getOrderid());
        viewHolder.tvTime.setText(((OrderInfo) this.list.get(i)).getOrderdate());
        viewHolder.tvMoney.setText("￥" + ((OrderInfo) this.list.get(i)).getTotalprice().toString());
        if (((OrderInfo) this.list.get(i)).getIs_new().intValue() == 0) {
            viewHolder.ivNewOrderNotification.setVisibility(0);
        } else {
            viewHolder.ivNewOrderNotification.setVisibility(4);
        }
        if ("tangchi".equals(((OrderInfo) this.list.get(i)).getOrderfrom()) || "wxtangchi".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
            List list;
            viewHolder.tvOrderinfoLeft.setVisibility(0);
            viewHolder.ivOrderinfoLeft.setVisibility(8);
            viewHolder.tvArea1.setVisibility(4);
            viewHolder.tvArea2.setVisibility(4);
            if (!Contants.isEmpty(((OrderInfo) this.list.get(i)).getTablenumber())) {
                list = (List) JSON.parseObject(((OrderInfo) this.list.get(i)).getTablenumber(), new TypeReference<List<TableNumber>>() {
                }, new Feature[0]);
                if (list.size() > 0) {
                    viewHolder.tvArea1.setVisibility(0);
                    viewHolder.tvArea1.setText(((TableNumber) list.get(0)).getArea_name() + "区" + ((TableNumber) list.get(0)).getTable_name() + "桌");
                }
                if (list.size() > 1) {
                    viewHolder.tvArea2.setVisibility(0);
                    viewHolder.tvArea2.setText(((TableNumber) list.get(1)).getArea_name() + "区" + ((TableNumber) list.get(1)).getTable_name() + "桌");
                }
            }
            list = (List) JSON.parseObject(((OrderInfo) this.list.get(i)).getGoodslist(), new TypeReference<List<OrderGoods>>() {
            }, new Feature[0]);
            int i3 = 0;
            while (i2 < list.size()) {
                if (((OrderGoods) list.get(i2)).getStatus() == 0) {
                    i3 = 1;
                }
                i2++;
            }
            if (i3 != 0) {
                viewHolder.tvOrderinfoLeft.setText("待");
                viewHolder.tvOrderinfoLeft.setBackgroundResource(R.mipmap.top_trink_red);
            } else {
                viewHolder.tvOrderinfoLeft.setText("出");
                viewHolder.tvOrderinfoLeft.setBackgroundResource(R.mipmap.top_trink_gray);
            }
        } else if ("wxwaimai".equals(((OrderInfo) this.list.get(i)).getOrderfrom()) || "meituan".equals(((OrderInfo) this.list.get(i)).getOrderfrom()) || "eleme".equals(((OrderInfo) this.list.get(i)).getOrderfrom()) || "baidu".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
            viewHolder.tvOrderinfoLeft.setVisibility(8);
            viewHolder.ivOrderinfoLeft.setVisibility(0);
            viewHolder.tvArea1.setVisibility(4);
            viewHolder.tvArea2.setVisibility(4);
            if ("baidu".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
                viewHolder.ivOrderinfoLeft.setImageResource(R.mipmap.waimai_baidu);
            } else if ("meituan".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
                viewHolder.ivOrderinfoLeft.setImageResource(R.mipmap.waimai_meituan);
            } else if ("eleme".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
                viewHolder.ivOrderinfoLeft.setImageResource(R.mipmap.waimai_eleme);
            } else if ("wxwaimai".equals(((OrderInfo) this.list.get(i)).getOrderfrom())) {
                viewHolder.ivOrderinfoLeft.setImageResource(R.mipmap.waimai_self);
            }
        }
        return view;
    }
}
