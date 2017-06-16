package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.TableActivity;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.utils.SingletonSB;
import com.smart.kitchen.smartkitchen.utils.SingletonTableNumberList;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.List;

public class TableAdapter extends BaseAdapter {
    private static final String TAG = "TableAdapter";
    private Context context;
    private int indexPage;
    private List<TableNumber> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    private class ViewHolder {
        CardView cardView;
        RelativeLayout rl_container;
        TextView tv_person_count;
        TextView tv_table_number;
        TextView tv_table_type;

        public ViewHolder(View view) {
            this.cardView = (CardView) view.findViewById(R.id.cardView);
            this.rl_container = (RelativeLayout) view.findViewById(R.id.rl_container);
            this.tv_table_number = (TextView) view.findViewById(R.id.tv_table_number);
            this.tv_person_count = (TextView) view.findViewById(R.id.tv_person_count);
            this.tv_table_type = (TextView) view.findViewById(R.id.tv_table_type);
        }

        public void reset() {
            this.rl_container.setBackgroundResource(R.drawable.table_normal);
        }

        public void checked() {
            this.rl_container.setBackgroundResource(R.drawable.table_checked);
        }

        public void useing() {
            this.rl_container.setBackgroundResource(R.drawable.table_use);
        }
    }

    public TableAdapter(Context context, List<TableNumber> list, int i) {
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
            view = LayoutInflater.from(this.context).inflate(R.layout.table_item, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_table_number.setText(((TableNumber) this.list.get(i)).getTable_name());
        viewHolder.tv_person_count.setText(((TableNumber) this.list.get(i)).getTable_type_count() + "人");
        viewHolder.tv_table_type.setText(((TableNumber) this.list.get(i)).getEating_count() + "人桌");
        viewHolder.reset();
        if (((TableNumber) this.list.get(i)).getTable_person().intValue() != 0) {
            viewHolder.useing();
        }
        for (int i2 = 0; i2 < SingletonTableNumberList.getInstance().getSelectList().size(); i2++) {
            if (((TableNumber) SingletonTableNumberList.getInstance().getSelectList().get(i2)).getId().toString().equals(((TableNumber) this.list.get(i)).getId().toString())) {
                viewHolder.checked();
            }
        }
        viewHolder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if ("".equals(TableActivity.getETTablePeople())) {
                    Toasts.showShort(TableAdapter.this.context, (CharSequence) "请先输入人数");
                    return;
                }
                if (SingletonSB.getInstance().isChecked()) {
                    if (SingletonTableNumberList.getInstance().getSelectList().contains(TableAdapter.this.list.get(i))) {
                        SingletonTableNumberList.getInstance().getSelectList().remove(TableAdapter.this.list.get(i));
                    } else if (Integer.valueOf(TableActivity.getETTablePeople()).intValue() > SingletonTableNumberList.getInstance().getSelectList().size()) {
                        SingletonTableNumberList.getInstance().getSelectList().add(TableAdapter.this.list.get(i));
                    } else {
                        return;
                    }
                } else if (((TableNumber) TableAdapter.this.list.get(i)).getTable_person().intValue() == 0) {
                    if (SingletonTableNumberList.getInstance().getSelectList().contains(TableAdapter.this.list.get(i))) {
                        SingletonTableNumberList.getInstance().getSelectList().remove(TableAdapter.this.list.get(i));
                    } else {
                        SingletonTableNumberList.getInstance().getSelectList().clear();
                        SingletonTableNumberList.getInstance().getSelectList().add(TableAdapter.this.list.get(i));
                    }
                }
                TableActivity.notifyPeopleTable();
                TableAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void SetClear() {
        SingletonTableNumberList.getInstance().getSelectList().clear();
    }
}
