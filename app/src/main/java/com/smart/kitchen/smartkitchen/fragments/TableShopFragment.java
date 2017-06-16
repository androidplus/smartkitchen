package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.activity.TableActivity;
import com.smart.kitchen.smartkitchen.adapters.TableShopAdapter;
import com.smart.kitchen.smartkitchen.adapters.TableShopAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class TableShopFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "ShopFragment";
    private TableShopAdapter adapter;
    private View line1;
    private View line2;
    private List<OrderGoods> list = new ArrayList();
    private ListView listView;
    private TextView totalCount;
    private TextView totalMoney;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_tableshop, null);
    }

    public void change() {
        int i = 0;
        LogUtils.e(TAG, "change: ");
        this.list.clear();
        int i2 = 0;
        for (int size = TableActivity.shoppingCarMap.size() - 1; size >= 0; size--) {
            OrderGoods orderGoods = (OrderGoods) TableActivity.shoppingCarMap.get(size);
            i2 += orderGoods.getCount();
            LogUtils.e(TAG, "change: " + orderGoods.toString());
            if (orderGoods.getGoods().getMoney() != null) {
                i = (int) (((double) i) + (orderGoods.getGoods().getMoney().doubleValue() * ((double) orderGoods.getCount())));
            }
            this.list.add(orderGoods);
        }
        this.totalCount.setText("合计：" + i2);
        this.totalMoney.setText("总额：" + i);
        if (this.adapter == null) {
            this.adapter = new TableShopAdapter(this.context, this.list);
            this.adapter.setListener(this);
            this.listView.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    protected void initView(View view) {
        this.listView = (ListView) view.findViewById(R.id.listView);
        this.totalMoney = (TextView) view.findViewById(R.id.totalMoney);
        this.totalCount = (TextView) view.findViewById(R.id.totalCount);
        this.line1 = view.findViewById(R.id.line_1);
        this.line2 = view.findViewById(R.id.line_2);
        this.line1.setVisibility(0);
        this.line2.setVisibility(0);
    }

    protected void initEvent() {
    }

    protected void initData() {
        change();
    }

    public void onItemClick(int i) {
        OrderGoods orderGoods = (OrderGoods) this.list.get(i);
        int count = orderGoods.getCount() - 1;
        orderGoods.setCount(count);
        if (count <= 0) {
            MainActivity.removeGoods(orderGoods);
        } else {
            MainActivity.removeGoodsCount(orderGoods);
        }
    }
}
