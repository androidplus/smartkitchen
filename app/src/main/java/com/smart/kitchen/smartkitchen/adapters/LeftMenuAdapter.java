package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.LeftMenu;
import java.util.List;

public class LeftMenuAdapter extends BaseAdapter {
    private static final String TAG = "LeftMenuAdapter";
    private Context context;
    private List<LeftMenu> leftMenus;
    private int select = 0;

    private class ViewHolder {
        LinearLayout ll_container;
        TextView title;
        ImageView title_icon;

        public ViewHolder(View view) {
            this.title = (TextView) view.findViewById(R.id.title);
            this.title_icon = (ImageView) view.findViewById(R.id.title_icon);
            this.ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
        }

        public void reset(int i) {
            this.ll_container.setBackgroundResource(R.drawable.left_bg_normal);
            this.title.setTextColor(LeftMenuAdapter.this.context.getResources().getColor(R.color.black));
            this.title_icon.setImageResource(( LeftMenuAdapter.this.leftMenus.get(i)).getTitle_icon());
        }

        public void checked(int i) {
            this.ll_container.setBackgroundResource(R.drawable.left_bg_checked);
            this.title.setTextColor(LeftMenuAdapter.this.context.getResources().getColor(R.color.white));
            this.title_icon.setImageResource(( LeftMenuAdapter.this.leftMenus.get(i)).getTitle_icon_ckecked());
        }
    }

    public LeftMenuAdapter(List<LeftMenu> list, Context context) {
        this.leftMenus = list;
        this.context = context;
    }

    public void setSelect(int position) {
        this.select = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.leftMenus.size();
    }

    @Override
    public Object getItem(int i) {
        return this.leftMenus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.left_menu_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset(i);
        if (this.select == i) {
            viewHolder.checked(i);
        }
        viewHolder.title.setText(leftMenus.get(i).getTitle());
        return view;
    }
}
