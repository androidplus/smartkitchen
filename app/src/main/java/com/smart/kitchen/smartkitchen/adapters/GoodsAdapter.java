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
import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.PropertyListener;
import com.smart.kitchen.smartkitchen.utils.Toasts;

import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends BaseAdapter {
    private final String TAG = "GoodsAdapter";
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
            this.cardView.setCardBackgroundColor(GoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.ilQuantityTag.setVisibility(View.GONE);
            this.name.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.money.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.count.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.ivIcon.setImageResource(R.mipmap.layers);
        }

        public void checked() {
            this.cardView.setCardBackgroundColor(GoodsAdapter.this.context.getResources().getColor(R.color.colorAccent));
            this.ilQuantityTag.setVisibility(View.VISIBLE);
            this.name.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.money.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.count.setTextColor(GoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.ivIcon.setImageResource(R.mipmap.layers_w);
        }
    }

    public GoodsAdapter(Context context, List<Goods> list, int i) {
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
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        Goods goods = this.list.get(i);
        if (MainActivity.getGoodsCount(String.valueOf(goods.getId())) > 0) {
            viewHolder.checked();
            viewHolder.tvQuantity.setText("" + MainActivity.getGoodsCount(String.valueOf(goods.getId())));
        }
        viewHolder.count.setText(goods.getCount() + "");
        viewHolder.name.setText(goods.getName());
        if (goods.getMoney() != null) {
            viewHolder.money.setText("￥" + goods.getMoney());
        } else {
            viewHolder.money.setText("￥");
        }
        Glide.with(this.context).load(goods.getGoods_image_url()).placeholder(R.mipmap.test).into(viewHolder.ivGoodsGreens);
        viewHolder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int i = 0;
                Goods goods1 = GoodsAdapter.this.list.get(i);
                if (MainActivity.selectTAG == 0) {
                    if (goods1.getCount().intValue() == 0) {
                        Toasts.show(GoodsAdapter.this.context, "该商品已经没货了");
                        return;
                    } else if (goods1.getGoods_size() == null) {
                        return;
                    }
                }
                final List<GoodSize> list = JSON.parseObject(goods1.getGoods_size(), new TypeReference<List<GoodSize>>() {
                }, new Feature[0]);
                final List<GoodTaste> list2 = JSON.parseObject(goods1.getTaste(), new TypeReference<List<GoodTaste>>() {
                }, new Feature[0]);
                List<GoodSize> arrayList = new ArrayList();
                if (list.size() > 0) {
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        arrayList.add(list.get(i2));
                    }
                }
                List<GoodTaste> arrayList2 = new ArrayList();
                if (list2.size() > 0) {
                    while (i < list2.size()) {
                        arrayList2.add(list2.get(i));
                        i++;
                    }
                }
                final DialogUtils dialogUtils = new DialogUtils((MainActivity) GoodsAdapter.this.context);
                final OrderGoods orderGoods = new OrderGoods();
                dialogUtils.showBeiZhu(view, goods1.getId(), MainActivity.shoppingCarMap, MainActivity.selectTAG, arrayList, arrayList2, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        dialogUtils.PropertyListener(new PropertyListener() {
                            public void dataTransmission(int i, int i2, int i3, String str) {
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
                                Goods goods = (Goods) JSON.parseObject(JSON.toJSONString(GoodsAdapter.this.list.get(i)), new TypeReference<Goods>() {
                                }, new Feature[0]);
                                if (list.size() > 0) {
                                    goods.setMoney(((GoodSize) list.get(i)).getSale_price());
                                } else {
                                    goods.setMoney(((Goods) GoodsAdapter.this.list.get(i)).getMoney());
                                }
                                orderGoods.setGoods(goods);
                                MainActivity.addTo(orderGoods);
                            }
                        });
                    }
                });
                GoodsAdapter.this.notifyDataSetChanged();
                if (GoodsAdapter.this.listener != null) {
                    GoodsAdapter.this.listener.onItemClick(i);
                }
            }
        });
        return view;
    }
}
