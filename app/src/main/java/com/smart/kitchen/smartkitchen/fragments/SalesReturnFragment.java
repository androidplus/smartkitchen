package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
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
import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.activity.SalesReturnActivity;
import com.smart.kitchen.smartkitchen.adapters.SalesReturnAdapter;
import com.smart.kitchen.smartkitchen.adapters.SalesReturnAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.OrderNumber;
import com.smart.kitchen.smartkitchen.utils.OrderUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesReturnFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "SalesReturnFragment";
    private SalesReturnAdapter adapter;
    private EditText etSalesreReturn;
    private View line1;
    private View line2;
    private List<OrderGoods> list = new ArrayList();
    private ListView lvSales;
    private OrderInfo orderInfo;
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
        for (int size = MainActivity.shoppingCarMap.size() - 1; size >= 0; size--) {
            OrderGoods orderGoods = (OrderGoods) MainActivity.shoppingCarMap.get(size);
            i += orderGoods.getCount();
            valueOf = Double.valueOf(valueOf.doubleValue() + (orderGoods.getGoods().getMoney().doubleValue() * ((double) orderGoods.getCount())));
            LogUtils.e(TAG, "change: " + orderGoods.toString());
            this.list.add(orderGoods);
        }
        this.salesTotalCount.setText("合计：" + i);
        this.salesTotalMoney.setText("总额: " + new DecimalFormat("###,##0.00").format(valueOf));
        if (this.adapter == null) {
            this.adapter = new SalesReturnAdapter(this.context, this.list);
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
    }

    protected void initEvent() {
        this.tvSalesCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                SalesReturnFragment.this.dialogUtils.showConfirm(view2, "确定要取消吗？", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        MainActivity.clear();
                        SalesReturnFragment.this.etSalesreReturn.setText("");
                    }
                }, null);
            }
        });
        this.tvSalesRefund.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.shoppingCarMap.size() == 0) {
                    Toasts.showLong(SalesReturnFragment.this.context, (CharSequence) "请添加菜品");
                } else if (TextUtils.isEmpty(SalesReturnFragment.this.getMark())) {
                    Toasts.showLong(SalesReturnFragment.this.context, (CharSequence) "请添加备注");
                } else {
                    SalesReturnFragment.this.getOrderInfo();
                    Intent intent = new Intent(SalesReturnFragment.this.getActivity(), SalesReturnActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ORD", SalesReturnFragment.this.orderInfo);
                    intent.putExtras(bundle);
                    SalesReturnFragment.this.startActivity(intent);
                    SalesReturnFragment.this.etSalesreReturn.setText("");
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
            MainActivity.removeGoods(orderGoods);
        } else {
            MainActivity.removeGoodsCount(orderGoods);
        }
    }

    public void clearEdittext() {
        if (!Contants.isEmpty(getMark())) {
            this.etSalesreReturn.setText("");
        }
    }

    public String getMark() {
        return this.etSalesreReturn.getText().toString().trim();
    }

    private OrderInfo getOrderInfo() {
        this.orderInfo = new OrderInfo();
        String orderNumber = OrderNumber.getInstance().getOrderNumber(this.context);
        this.orderInfo.setOrderid(orderNumber);
        this.orderInfo.setOrderfrom("tuihuo");
        this.orderInfo.setOrderdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        this.orderInfo.setOrderstatus("4");
        for (int i = 0; i < MainActivity.shoppingCarMap.size(); i++) {
            ((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoods().setOrderid(orderNumber);
        }
        this.orderInfo.setGoodslist(JSON.toJSONString(MainActivity.shoppingCarMap));
        this.orderInfo.setTotalprice(new OrderUtils().getTotalmoney(MainActivity.shoppingCarMap));
        this.orderInfo.setWaiterid(new UserInfoDaoManager().getNowUserInfo().getUserid());
        this.orderInfo.setPay_status(Integer.valueOf(0));
        this.orderInfo.setMark(getMark());
        return this.orderInfo;
    }
}
