package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.AddToActivity;
import com.smart.kitchen.smartkitchen.adapters.AddToAdapter;
import com.smart.kitchen.smartkitchen.adapters.AddToAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddToFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "AddToFragment";
    private AddToAdapter adapter;
    private isOk is;
    private View line1;
    private View line2;
    private List<OrderGoods> list = new ArrayList();
    private ListView lvAddto;
    private TextView tvAddto;
    private TextView tvCancle;
    private TextView tvTotalCount;
    private TextView tvTotalMoney;

    public interface isOk {
        void isOk();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_addto, null);
    }

    public void change() {
        this.list.clear();
        Double valueOf = Double.valueOf(0.0d);
        int i = 0;
        for (int i2 = 0; i2 < AddToActivity.shoppingAll.size(); i2++) {
            OrderGoods orderGoods = (OrderGoods) AddToActivity.shoppingAll.get(i2);
            i += orderGoods.getCount();
            LogUtils.e(TAG, "change: " + orderGoods.toString());
            if (orderGoods.getGoods().getMoney() != null) {
                valueOf = Double.valueOf(valueOf.doubleValue() + (orderGoods.getGoods().getMoney().doubleValue() * ((double) orderGoods.getCount())));
            }
            this.list.add(orderGoods);
        }
        this.tvTotalCount.setText("合计：" + i);
        this.tvTotalMoney.setText("总额: " + new DecimalFormat("###,##0.00").format(valueOf));
        if (this.adapter == null) {
            this.adapter = new AddToAdapter(this.context, this.list);
            this.adapter.setListener(this);
            this.lvAddto.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    protected void initView(View view) {
        this.lvAddto = (ListView) view.findViewById(R.id.lv_listView_addto);
        this.tvTotalMoney = (TextView) view.findViewById(R.id.tv_totalMoney_addto);
        this.tvTotalCount = (TextView) view.findViewById(R.id.tv_totalCount_addto);
        this.tvCancle = (TextView) view.findViewById(R.id.tv_addto_cancle);
        this.tvAddto = (TextView) view.findViewById(R.id.tv_addto);
        this.line1 = view.findViewById(R.id.line_1);
        this.line2 = view.findViewById(R.id.line_2);
        this.line1.setVisibility(0);
        this.line2.setVisibility(0);
    }

    protected void initEvent() {
        this.tvCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                AddToFragment.this.dialogUtils.showConfirm(view2, "确定要取消吗？", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        AddToActivity.clear();
                        AddToFragment.this.getActivity().finish();
                    }
                }, null);
            }
        });
        this.tvAddto.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AddToFragment.this.is != null) {
                    AddToFragment.this.is.isOk();
                }
            }
        });
    }

    public void setIsOk(isOk com_smart_kitchen_smartkitchen_fragments_AddToFragment_isOk) {
        this.is = com_smart_kitchen_smartkitchen_fragments_AddToFragment_isOk;
    }

    protected void initData() {
        change();
    }

    public void onItemClick(int i) {
        OrderGoods orderGoods = (OrderGoods) this.list.get(i);
        int count = orderGoods.getCount() - 1;
        orderGoods.setCount(count);
        if (count <= 0) {
            AddToActivity.removeGoods(orderGoods);
        } else {
            AddToActivity.removeGoodsCount(orderGoods);
        }
    }
}
