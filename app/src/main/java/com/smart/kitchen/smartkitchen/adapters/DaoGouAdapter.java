package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.utils.Contants;
import java.util.List;

public class DaoGouAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> list;
    private int select = -1;

    private class ViewHolder {
        TextView emp_number;

        public ViewHolder(View view) {
            this.emp_number = (TextView) view.findViewById(R.id.emp_number);
        }

        public void reset() {
            this.emp_number.setTextColor(Contants.getColor(DaoGouAdapter.this.context, R.color.black));
            this.emp_number.setBackgroundColor(Contants.getColor(DaoGouAdapter.this.context, R.color.white));
        }

        public void checked() {
            this.emp_number.setTextColor(Contants.getColor(DaoGouAdapter.this.context, R.color.white));
            this.emp_number.setBackgroundColor(Contants.getColor(DaoGouAdapter.this.context, R.color.red));
        }
    }

    public DaoGouAdapter(List<UserInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setSelect(int i) {
        this.select = i;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public UserInfo getItem(int i) {
        return (UserInfo) this.list.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.daogou_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.reset();
        if (this.select == i) {
            viewHolder.checked();
        }
        viewHolder.emp_number.setText(getItem(i).getRealname());
        return view;
    }
}
