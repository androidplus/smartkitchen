package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private static final String TAG = "MenuAdapter";
    private Context context;
    private List<FoodType> list;
    private FoodType selectFoodType;

    private class ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.item_text);
        }

        public void reset() {
            this.textView.setTextColor(MenuAdapter.this.context.getResources().getColor(R.color.white));
            this.textView.setBackgroundColor(MenuAdapter.this.context.getResources().getColor(17170445));
        }

        public void checked() {
            this.textView.setTextColor(MenuAdapter.this.context.getResources().getColor(R.color.black));
            this.textView.setBackground(MenuAdapter.this.context.getResources().getDrawable(R.drawable.corner_white));
        }
    }

    public MenuAdapter(Context context, List<FoodType> list) {
        this.context = context;
        this.list = list;
        if (this.list.size() > 0) {
            this.selectFoodType = (FoodType) this.list.get(0);
            LogUtils.e(TAG, "getView: false" + this.selectFoodType);
        }
    }

    public void setSelectFoodType(int i) {
        this.selectFoodType = (FoodType) this.list.get(i);
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
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.menu_item, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(((FoodType) this.list.get(i)).getCatalog());
        viewHolder.reset();
        if (this.list.get(i) == this.selectFoodType) {
            viewHolder.checked();
        }
        LogUtils.e(TAG, i + "getView: false" + this.list.get(i));
        return view;
    }
}
