package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.BreakageActivity;
import com.smart.kitchen.smartkitchen.adapters.BreakageAdapter;
import com.smart.kitchen.smartkitchen.adapters.BreakageAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.mvp.presenter.BreakagePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.BreakageView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.OrderUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BreakageFragment extends BaseFragment implements OnItemClickListener, BreakageView {
    private static final String TAG = "SalesReturnFragment";
    private BreakageAdapter adapter;
    private EditText etSalesreReturn;
    private View line1;
    private View line2;
    private List<OrderGoods> list = new ArrayList();
    private ListView lvSales;
    private BreakagePresenter presenter;
    private TextView salesTotalCount;
    private TextView salesTotalMoney;
    private TextView tvSalesCancle;
    private TextView tvSalesRefund;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_salesreturn, null);
    }

    public void change() {
        this.list.clear();
        int i = 0;
        Double valueOf = Double.valueOf(0.0d);
        for (int size = BreakageActivity.shoppingCarMap.size() - 1; size >= 0; size--) {
            OrderGoods orderGoods = (OrderGoods) BreakageActivity.shoppingCarMap.get(size);
            i += orderGoods.getCount();
            valueOf = Double.valueOf(valueOf.doubleValue() + (orderGoods.getGoods().getMoney().doubleValue() * ((double) orderGoods.getCount())));
            LogUtils.e(TAG, "change: " + orderGoods.toString());
            this.list.add(orderGoods);
        }
        this.salesTotalCount.setText("合计：" + i);
        this.salesTotalMoney.setText("总额: " + new DecimalFormat("###,##0.00").format(valueOf));
        if (this.adapter == null) {
            this.adapter = new BreakageAdapter(this.context, this.list);
            this.adapter.setListener(this);
            this.lvSales.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    protected void initView(View view) {
        this.lvSales = (ListView) view.findViewById(R.id.sales_listView);
        this.salesTotalMoney = (TextView) view.findViewById(R.id.sales_totalMoney);
        this.salesTotalCount = (TextView) view.findViewById(R.id.sales_totalCount);
        this.tvSalesCancle = (TextView) view.findViewById(R.id.tv_sales_cancle);
        this.tvSalesRefund = (TextView) view.findViewById(R.id.tv_sales_refund);
        this.etSalesreReturn = (EditText) view.findViewById(R.id.et_salesre_return);
        this.line1 = view.findViewById(R.id.line_1);
        this.line2 = view.findViewById(R.id.line_2);
        this.line1.setVisibility(0);
        this.line2.setVisibility(0);
        this.tvSalesRefund.setText("提交");
    }

    protected void initEvent() {
        this.presenter = new BreakagePresenter(this.context, this);
        this.tvSalesCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                BreakageFragment.this.dialogUtils.showConfirm(view2, "确定要取消吗？", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        BreakageActivity.clear();
                    }
                }, null);
            }
        });
        this.tvSalesRefund.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(BreakageFragment.this.etSalesreReturn.getText().toString().trim())) {
                    Toasts.show(BreakageFragment.this.context, "备注不能为空");
                } else {
                    BreakageFragment.this.presenter.breakage(new UserInfoDaoManager().getNowUserInfo().getUserid(), new OrderUtils().getTotalmoney(BreakageActivity.shoppingCarMap).doubleValue(), JSON.toJSONString(BreakageActivity.shoppingCarMap), BreakageFragment.this.etSalesreReturn.getText().toString().trim());
                }
            }
        });
    }

    protected void initData() {
        change();
    }

    public void onItemClick(int i) {
        OrderGoods orderGoods = (OrderGoods) this.list.get(i);
        int count = orderGoods.getCount() - 1;
        orderGoods.setCount(count);
        if (count <= 0) {
            BreakageActivity.removeGoods(orderGoods);
        } else {
            BreakageActivity.removeGoodsCount(orderGoods);
        }
    }

    public void onSuccess(List<FoodType> list) {
    }

    public void onFail() {
    }

    public void ShowFoodType(List<FoodType> list) {
    }

    public void inFrom(String str) {
        if ("onSuccess".equals(str)) {
            Toasts.show(this.context, "退货成功");
            BreakageActivity.clear();
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "网络出错,请重新提交");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "网络出错,请重新提交");
        }
    }
}
