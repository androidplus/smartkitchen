package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.utils.Contants;
import java.util.List;

public class MsgAdapter extends BaseAdapter {
    private Context context;
    private boolean is_read;
    private List<MessageCenter> list;

    private class ViewHolder {
        CardView cardView;
        TextView text;

        public ViewHolder(View view) {
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.text = (TextView) view.findViewById(R.id.text);
        }

        public void checked() {
            this.cardView.setCardBackgroundColor(Contants.getColor(MsgAdapter.this.context, R.color.colorPrimary));
            this.text.setTextColor(Contants.getColor(MsgAdapter.this.context, R.color.white));
        }
    }

    public MsgAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        this.is_read = true;
    }

    public MsgAdapter(Context context, List list, boolean z) {
        this.context = context;
        this.list = list;
        this.is_read = z;
    }

    public int getCount() {
        return this.list.size();
    }

    public MessageCenter getItem(int i) {
        return (MessageCenter) this.list.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.msg_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!this.is_read) {
            viewHolder.checked();
        }
        viewHolder.text.setText(getItem(i).getMsg_content());
        return view;
    }
}
