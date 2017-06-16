package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.bumptech.glide.Glide;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.BreakageActivity;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.PropertyListener2;
import java.util.ArrayList;
import java.util.List;

public class GoodsBreakageAdapter extends BaseAdapter {
    private Context context;
    private int indexPage;
    private List<Goods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class ViewHolder {
        CardView cardView;
        TextView count;
        LinearLayout ilQuantityTag;
        ImageView ivGoodsGreens;
        ImageView ivIcon;
        TextView money;
        TextView name;
        TextView tvQuantity;

        public ViewHolder(View view) {
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.ilQuantityTag = (LinearLayout) view.findViewById(R.id.il_quantity_tag);
            this.tvQuantity = (TextView) view.findViewById(R.id.tv_quantity);
            this.name = (TextView) view.findViewById(R.id.name);
            this.money = (TextView) view.findViewById(R.id.money);
            this.count = (TextView) view.findViewById(R.id.count);
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.ivGoodsGreens = (ImageView) view.findViewById(R.id.iv_goods_greens);
        }

        public void reset() {
            this.cardView.setCardBackgroundColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.white));
            this.ilQuantityTag.setVisibility(View.GONE);
            this.name.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.black));
            this.money.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.black));
            this.count.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.black));
            this.ivIcon.setImageResource(R.mipmap.layers);
        }

        public void checked() {
            this.cardView.setCardBackgroundColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.colorAccent));
            this.ilQuantityTag.setVisibility(View.VISIBLE);
            this.name.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.white));
            this.money.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.white));
            this.count.setTextColor(GoodsBreakageAdapter.this.context.getResources().getColor(R.color.white));
            this.ivIcon.setImageResource(R.mipmap.layers_w);
        }
    }

    public GoodsBreakageAdapter(Context context, List<Goods> list, int i) {
        this.context = context;
        this.list = list;
        this.indexPage = i;
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
            view = LayoutInflater.from(this.context).inflate(R.layout.item_goods, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (BreakageActivity.getGoodsCount(String.valueOf(((Goods) this.list.get(i)).getId())) > 0) {
            viewHolder.checked();
            viewHolder.tvQuantity.setText("" + BreakageActivity.getGoodsCount(String.valueOf(((Goods) this.list.get(i)).getId())));
        }
        viewHolder.count.setText(((Goods) this.list.get(i)).getCount() + "");
        viewHolder.name.setText(((Goods) this.list.get(i)).getName());
        viewHolder.money.setText("￥" + ((Goods) this.list.get(i)).getMoney());
        Glide.with(this.context).load(((Goods) this.list.get(i)).getGoods_image_url()).placeholder((int) R.mipmap.test).into(viewHolder.ivGoodsGreens);
        viewHolder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int i = 0;
                final List list = (List) JSON.parseObject(((Goods) GoodsBreakageAdapter.this.list.get(i)).getGoods_size(), new TypeReference<List<GoodSize>>() {
                }, new Feature[0]);
                final List list2 = (List) JSON.parseObject(((Goods) GoodsBreakageAdapter.this.list.get(i)).getTaste(), new TypeReference<List<GoodTaste>>() {
                }, new Feature[0]);
                List arrayList = new ArrayList();
                if (list.size() > 0) {
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        arrayList.add(list.get(i2));
                    }
                }
                List arrayList2 = new ArrayList();
                if (list2.size() > 0) {
                    while (i < list2.size()) {
                        arrayList2.add(list2.get(i));
                        i++;
                    }
                }
                final DialogUtils dialogUtils = new DialogUtils((BreakageActivity) GoodsBreakageAdapter.this.context);
                final OrderGoods orderGoods = new OrderGoods();
                dialogUtils.showBeiZhu2(view, arrayList, arrayList2, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        dialogUtils.PropertyListener2(new PropertyListener2() {
                            public void dataTransmission2(int i, int i2, int i3, String str) {
                                if (list.size() > 0) {
                                    orderGoods.setGoodsize((GoodSize) list.get(i));
                                }
                                if (list2.size() > 0) {
                                    orderGoods.setGoodtaste((GoodTaste) list2.get(i2));
                                }
                                if (str != null) {
                                    orderGoods.setMark(str);
                                }
                                orderGoods.setCount(i3);
                                Goods goods = (Goods) JSON.parseObject(JSON.toJSONString(GoodsBreakageAdapter.this.list.get(i)), new TypeReference<Goods>() {
                                }, new Feature[0]);
                                if (list.size() > 0) {
                                    goods.setMoney(((GoodSize) list.get(i)).getSale_price());
                                } else {
                                    goods.setMoney(((Goods) GoodsBreakageAdapter.this.list.get(i)).getMoney());
                                }
                                orderGoods.setGoods(goods);
                                BreakageActivity.addTo(orderGoods);
                            }
                        });
                    }
                });
                GoodsBreakageAdapter.this.notifyDataSetChanged();
                if (GoodsBreakageAdapter.this.listener != null) {
                    GoodsBreakageAdapter.this.listener.onItemClick(i);
                }
            }
        });
        return view;
    }
}
