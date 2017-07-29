package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import java.util.List;

public class CheckAdapter extends BaseAdapter {
    private Context context;
    private List<Goods> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    protected class ViewHolder {
        private LinearLayout ivDec;
        private TextView tvCheckCount;
        private TextView tvCheckName;
        private TextView tvCheckPrice;

        public ViewHolder(View view) {
            this.tvCheckName = (TextView) view.findViewById(R.id.tv_check_name);
            this.tvCheckPrice = (TextView) view.findViewById(R.id.tv_check_price);
            this.tvCheckCount = (TextView) view.findViewById(R.id.tv_check_count);
            this.ivDec = (LinearLayout) view.findViewById(R.id.iv_dec);
        }
    }

    public CheckAdapter(Context context, List<Goods> list) {
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
        int i2;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.adapter_check, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Goods goods = this.list.get(i);
        viewHolder.tvCheckName.setText(goods.getName() + "");
        viewHolder.tvCheckPrice.setText(goods.getMoney() + "");
        List list = (List) JSON.parseObject(goods.getGoods_size(), new TypeReference<List<GoodSize>>() {
        }, new Feature[0]);
        if (list.size() > 0) {
            i2 = 0;
            for (int i3 = 0; i3 < list.size(); i3++) {
                i2 += ((GoodSize) list.get(i3)).getCount().intValue();
            }
        } else {
            i2 = 0;
        }
        viewHolder.tvCheckCount.setText(i2 + "");
        viewHolder.ivDec.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CheckAdapter.this.listener != null) {
                    CheckAdapter.this.listener.onItemClick(i);
                }
            }
        });
        return view;
    }
}
