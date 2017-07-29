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
import com.smart.kitchen.smartkitchen.activity.CheckActivity;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.CheckListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoodsCheckAdapter extends BaseAdapter {
    private Context context;
    private List<Goods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class ViewHolder {
        CardView cardView;
        TextView count;
        LinearLayout il_quantity_tag;
        ImageView iv_goods_greens;
        ImageView iv_icon;
        TextView name;

        public ViewHolder(View view) {
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.il_quantity_tag = (LinearLayout) view.findViewById(R.id.il_quantity_tag);
            this.name = (TextView) view.findViewById(R.id.name);
            this.count = (TextView) view.findViewById(R.id.count);
            this.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            this.iv_goods_greens = (ImageView) view.findViewById(R.id.iv_goods_greens);
        }

        public void reset() {
            this.cardView.setCardBackgroundColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.white));
            this.il_quantity_tag.setVisibility(View.GONE);
            this.name.setTextColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.black));
            this.count.setTextColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.black));
            this.iv_icon.setImageResource(R.mipmap.layers);
        }

        public void checked() {
            this.cardView.setCardBackgroundColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.colorAccent));
            this.il_quantity_tag.setVisibility(View.VISIBLE);
            this.name.setTextColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.white));
            this.count.setTextColor(GoodsCheckAdapter.this.context.getResources().getColor(R.color.white));
            this.iv_icon.setImageResource(R.mipmap.layers_w);
        }
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public GoodsCheckAdapter(Context context, List<Goods> list, int i) {
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

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_check, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (CheckActivity.get(String.valueOf( list.get(i).getId())) != null) {
            viewHolder.checked();
        }
        viewHolder.name.setText(( this.list.get(i)).getName());
        viewHolder.count.setText(( this.list.get(i)).getCount() + "份");
        Glide.with(this.context).load(( this.list.get(i)).getGoods_image_url()).placeholder( R.mipmap.test).into(viewHolder.iv_goods_greens);
        viewHolder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final List list = (List) JSON.parseObject(((Goods) GoodsCheckAdapter.this.list.get(i)).getGoods_size(), new TypeReference<List<GoodSize>>() {
                }, new Feature[0]);
                final DialogUtils dialogUtils = new DialogUtils((CheckActivity) GoodsCheckAdapter.this.context);
                dialogUtils.showCheck(view, list, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        dialogUtils.propertyListener(new CheckListener() {
                            public void property(Map<String, String> map) {
                                List arrayList = new ArrayList();
                                for (int i = 0; i < list.size(); i++) {
                                    GoodSize goodSize = new GoodSize(1l,"",1.0,"");
                                    goodSize.setId(((GoodSize) list.get(i)).getId());
                                    goodSize.setCount(((GoodSize) list.get(i)).getCount());
                                    goodSize.setGoodsid(((GoodSize) list.get(i)).getGoodsid());
                                    goodSize.setSale_price(((GoodSize) list.get(i)).getSale_price());
                                    goodSize.setSpec_name(((GoodSize) list.get(i)).getSpec_name());
                                    arrayList.add(goodSize);
                                }
                                Goods goods =  JSON.parseObject(JSON.toJSONString(GoodsCheckAdapter.this.list.get(i)), new TypeReference<Goods>() {
                                }, new Feature[0]);
                                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                    if (map.get(((GoodSize) arrayList.get(i2)).getId() + "") != null) {
                                        ((GoodSize) arrayList.get(i2)).setCount(Integer.valueOf(map.get(((GoodSize) arrayList.get(i2)).getId() + "")));
                                    }
                                }
                                goods.setGoods_size(JSON.toJSONString(arrayList));
                                CheckActivity.add(String.valueOf(goods.getId()), goods);
                            }
                        });
                    }
                });
                GoodsCheckAdapter.this.notifyDataSetChanged();
                if (GoodsCheckAdapter.this.listener != null) {
                    GoodsCheckAdapter.this.listener.onItemClick(i);
                }
            }
        });
        return view;
    }
}
