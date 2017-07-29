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
import com.smart.kitchen.smartkitchen.activity.AddToActivity;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.PropertyListener;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.ArrayList;
import java.util.List;

public class AddToGoodsAdapter extends BaseAdapter {
    private Context context;
    private int indexPage;
    private List<Goods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private  class ViewHolder {
        CardView cardView;
        TextView count;
        LinearLayout il_quantity_tag;
        ImageView iv_goods_greens;
        ImageView iv_icon;
        TextView money;
        TextView name;
        TextView tv_quantity;

        public ViewHolder(View view) {
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.il_quantity_tag = (LinearLayout) view.findViewById(R.id.il_quantity_tag);
            this.tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
            this.name = (TextView) view.findViewById(R.id.name);
            this.money = (TextView) view.findViewById(R.id.money);
            this.count = (TextView) view.findViewById(R.id.count);
            this.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            this.iv_goods_greens = (ImageView) view.findViewById(R.id.iv_goods_greens);
        }

        public void reset() {
            this.cardView.setCardBackgroundColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.il_quantity_tag.setVisibility(View.GONE);
            this.name.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.money.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.count.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.black));
            this.iv_icon.setImageResource(R.mipmap.layers);
        }

        public void checked() {
            this.cardView.setCardBackgroundColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.colorAccent));
            this.il_quantity_tag.setVisibility(View.VISIBLE);
            this.name.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.money.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.count.setTextColor(AddToGoodsAdapter.this.context.getResources().getColor(R.color.white));
            this.iv_icon.setImageResource(R.mipmap.layers_w);
        }
    }

    public AddToGoodsAdapter(Context context, List<Goods> list, int i) {
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
        int i2 = 0;
        while (i2 < AddToActivity.shoppingAll.size()) {
            if (i2 < AddToActivity.indexOf && String.valueOf( AddToActivity.shoppingAll.get(i2).getGoods().getId()).equals(String.valueOf((list.get(i)).getId()))) {
                viewHolder.checked();
            }
            i2++;
        }
        if (AddToActivity.getGoodsCount(String.valueOf(( list.get(i)).getId())) > 0) {
            viewHolder.checked();
            viewHolder.tv_quantity.setText("" + AddToActivity.getGoodsCount(String.valueOf(((Goods) this.list.get(i)).getId())));
        }
        viewHolder.count.setText(((Goods) this.list.get(i)).getCount() + "");
        viewHolder.name.setText(((Goods) this.list.get(i)).getName());
        viewHolder.money.setText("￥" + ( this.list.get(i)).getMoney());
        Glide.with(this.context).load(( this.list.get(i)).getGoods_image_url()).placeholder( R.mipmap.test).into(viewHolder.iv_goods_greens);
        viewHolder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((Goods) AddToGoodsAdapter.this.list.get(i)).getCount().intValue() == 0) {
                    Toasts.show(AddToGoodsAdapter.this.context, "该商品已经没货了");
                } else if (((Goods) AddToGoodsAdapter.this.list.get(i)).getGoods_size() != null) {
                    int i = 0;
                    final List list = (List) JSON.parseObject(((Goods) AddToGoodsAdapter.this.list.get(i)).getGoods_size(), new TypeReference<List<GoodSize>>() {
                    }, new Feature[0]);
                    final List list2 = (List) JSON.parseObject(((Goods) AddToGoodsAdapter.this.list.get(i)).getTaste(), new TypeReference<List<GoodTaste>>() {
                    }, new Feature[0]);
                    List arrayList = new ArrayList();
                    if (list.size() > 0) {
                        for (i = 0; i < list.size(); i++) {
                            arrayList.add(list.get(i));
                        }
                    }
                    List arrayList2 = new ArrayList();
                    if (list2.size() > 0) {
                        for (i = 0; i < list2.size(); i++) {
                            arrayList2.add(list2.get(i));
                        }
                    }
                    final DialogUtils dialogUtils = new DialogUtils((AddToActivity) AddToGoodsAdapter.this.context);
                    final OrderGoods orderGoods = new OrderGoods();
                    dialogUtils.showBeiZhu(view, ((Goods) AddToGoodsAdapter.this.list.get(i)).getId(), AddToActivity.shoppingCarMap, 0, arrayList, arrayList2, new DialogUtils.OnClickListener() {
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
                                    Goods goods = (Goods) JSON.parseObject(JSON.toJSONString(AddToGoodsAdapter.this.list.get(i)), new TypeReference<Goods>() {
                                    }, new Feature[0]);
                                    if (list.size() > 0) {
                                        goods.setMoney(((GoodSize) list.get(i)).getSale_price());
                                    } else {
                                        goods.setMoney(((Goods) AddToGoodsAdapter.this.list.get(i)).getMoney());
                                    }
                                    orderGoods.setGoods(goods);
                                    AddToActivity.addTo(orderGoods);
                                }
                            });
                        }
                    });
                    AddToGoodsAdapter.this.notifyDataSetChanged();
                    if (AddToGoodsAdapter.this.listener != null) {
                        AddToGoodsAdapter.this.listener.onItemClick(i);
                    }
                }
            }
        });
        return view;
    }
}
