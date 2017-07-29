package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import java.util.List;

public class BasicDialogAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mList;

    private class ViewHolder {
        TextView tvText;

        private ViewHolder() {
        }
    }

    public BasicDialogAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int i) {
        return this.mList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.basic_dialog_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvText = (TextView) view.findViewById(R.id.tv_basic_dialog_item_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvText.setText(mList.get(i));
        return view;
    }
}
