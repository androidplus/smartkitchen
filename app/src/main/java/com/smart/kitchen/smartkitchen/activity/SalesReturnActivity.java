package com.smart.kitchen.smartkitchen.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.BuildConfig;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.SalesReturnPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.SalesReturnView;
import com.smart.kitchen.smartkitchen.utils.ArithmeticUtils;
import com.smart.kitchen.smartkitchen.utils.CashierInputFilter;
import com.smart.kitchen.smartkitchen.utils.DiscountInputFilter;
import com.smart.kitchen.smartkitchen.utils.DiscountPay;
import com.smart.kitchen.smartkitchen.utils.DoubleUtils;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;

import java.util.ArrayList;
import java.util.List;

public class SalesReturnActivity extends BaseFragmentActivity implements OnClickListener, SalesReturnView {
    public List<TextView> Textlist;
    public CheckBox ckSalesReturn;
    public EditText etAggregateSales;
    public EditText etDiscountSales;
    public EditText etIncomeSales;
    public List<View> list = new ArrayList();
    public LinearLayout llDiscountSales;
    public String money;
    public OrderInfo orderInfo;
    private SalesReturnPresenter presenter;
    public TextView tvFavorableSales;
    public TextView tvQuitSales;
    public TextView tvSales1;
    public TextView tvSales10;
    public TextView tvSales2;
    public TextView tvSales3;
    public TextView tvSales4;
    public TextView tvSales5;
    public TextView tvSales6;
    public TextView tvSales7;
    public TextView tvSales8;
    public TextView tvSales9;
    public TextView tvSettleSales;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_sales_return);
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.etAggregateSales = (EditText) findViewById(R.id.et_aggregate_sales);
        this.etIncomeSales = (EditText) findViewById(R.id.et_income_sales);
        this.etDiscountSales = (EditText) findViewById(R.id.et_zhekou_sales);
        this.tvFavorableSales = (TextView) findViewById(R.id.tv_favorable_sales);
        this.ckSalesReturn = (CheckBox) findViewById(R.id.ck_sales_return);
        this.llDiscountSales = (LinearLayout) findViewById(R.id.ll_zhekou_sales);
        this.tvSales1 = (TextView) findViewById(R.id.tv_sales1);
        this.tvSales2 = (TextView) findViewById(R.id.tv_sales2);
        this.tvSales3 = (TextView) findViewById(R.id.tv_sales3);
        this.tvSales4 = (TextView) findViewById(R.id.tv_sales4);
        this.tvSales5 = (TextView) findViewById(R.id.tv_sales5);
        this.tvSales6 = (TextView) findViewById(R.id.tv_sales6);
        this.tvSales7 = (TextView) findViewById(R.id.tv_sales7);
        this.tvSales8 = (TextView) findViewById(R.id.tv_sales8);
        this.tvSales9 = (TextView) findViewById(R.id.tv_sales9);
        this.tvSales10 = (TextView) findViewById(R.id.tv_sales10);
        this.tvSettleSales = (TextView) findViewById(R.id.tv_settle_sales);
        this.tvQuitSales = (TextView) findViewById(R.id.tv_quit_sales);
    }

    @Override
    protected void initEvent() {
        this.presenter = new SalesReturnPresenter(this.context, this);
        this.orderInfo = (OrderInfo) getIntent().getSerializableExtra("ORD");
        this.money = String.valueOf(this.orderInfo.getTotalprice());
    }

    @Override
    protected void initData() {
        this.etDiscountSales.setText("10.0");
        this.tvSales10.setSelected(true);
        this.etAggregateSales.setText(this.money);
        this.etIncomeSales.setText(this.money);
        this.Textlist = new ArrayList();
        this.Textlist.add(this.tvSales1);
        this.Textlist.add(this.tvSales2);
        this.Textlist.add(this.tvSales3);
        this.Textlist.add(this.tvSales4);
        this.Textlist.add(this.tvSales5);
        this.Textlist.add(this.tvSales6);
        this.Textlist.add(this.tvSales7);
        this.Textlist.add(this.tvSales8);
        this.Textlist.add(this.tvSales9);
        this.Textlist.add(this.tvSales10);
        this.list.add(this.tvSales1);
        this.list.add(this.tvSales2);
        this.list.add(this.tvSales3);
        this.list.add(this.tvSales4);
        this.list.add(this.tvSales5);
        this.list.add(this.tvSales6);
        this.list.add(this.tvSales7);
        this.list.add(this.tvSales8);
        this.list.add(this.tvSales9);
        this.list.add(this.tvSales10);
        this.list.add(this.tvFavorableSales);
        this.tvQuitSales.setOnClickListener(this);
        this.tvSettleSales.setOnClickListener(this);
        this.tvSales1.setOnClickListener(this);
        this.tvSales2.setOnClickListener(this);
        this.tvSales3.setOnClickListener(this);
        this.tvSales4.setOnClickListener(this);
        this.tvSales5.setOnClickListener(this);
        this.tvSales6.setOnClickListener(this);
        this.tvSales7.setOnClickListener(this);
        this.tvSales8.setOnClickListener(this);
        this.tvSales9.setOnClickListener(this);
        this.tvSales10.setOnClickListener(this);
        this.ckSalesReturn.setOnClickListener(this);
        this.tvFavorableSales.setOnClickListener(this);
        inputEdit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_favorable_sales:
                if (this.llDiscountSales.getVisibility() == View.VISIBLE) {
                    this.llDiscountSales.setVisibility(View.GONE);
                    return;
                } else {
                    this.llDiscountSales.setVisibility(View.VISIBLE);
                    return;
                }
            case R.id.tv_settle_sales:
                if (!new DoubleUtils().pandun(getEtIncome()) || !new DoubleUtils().pandun(getEtAggregate())) {
                    Toasts.show(this.context, "请输入正确的金额");
                    return;
                } else if (TextUtils.isEmpty(getEtIncome()) && ArithmeticUtils.compare(getEtAggregate(), getEtIncome()) == -1) {
                    Toasts.show(this.context, "请输入正确的金额");
                    return;
                } else {
                    this.orderInfo.setTotalprice(Double.valueOf(getEtIncome()));
                    this.presenter.salesReturnConsumer(this.orderInfo.getOrderid(), String.valueOf(this.orderInfo.getTotalprice()), this.orderInfo.getMark(), this.orderInfo.getWaiterid(), SPUtils.getUserinfo(this.context, SPUtils.STORE_ID), this.orderInfo.getGoodslist());
                    return;
                }
            case R.id.tv_quit_sales:
                finish();
                return;
            case R.id.tv_sales1:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText(BuildConfig.VERSION_NAME);
                return;
            case R.id.tv_sales2:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("2.0");
                return;
            case R.id.tv_sales3:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("3.0");
                return;
            case R.id.tv_sales4:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("4.0");
                return;
            case R.id.tv_sales5:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("5.0");
                return;
            case R.id.tv_sales6:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("6.0");
                return;
            case R.id.tv_sales7:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("7.0");
                return;
            case R.id.tv_sales8:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("8.0");
                return;
            case R.id.tv_sales9:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("9.0");
                return;
            case R.id.tv_sales10:
                selectedSet(String.valueOf(view.getId()), this.Textlist);
                this.etDiscountSales.setText("10.0");
                return;
            case R.id.ck_sales_return:
                if (this.ckSalesReturn.isChecked()) {
                    setClick(true);
                    this.etDiscountSales.setEnabled(false);
                    this.etIncomeSales.setText(DiscountPay.getZhekou(this.money, this.etDiscountSales.getText().toString().trim()));
                    return;
                }
                this.etIncomeSales.setText(this.money);
                setClick(false);
                this.etDiscountSales.setEnabled(true);
                return;
            default:
                return;
        }
    }

    public void setClick(boolean z) {
        int i;
        if (z) {
            for (i = 0; i < this.list.size(); i++) {
                (this.list.get(i)).setOnClickListener(null);
            }
            return;
        }
        for (i = 0; i < this.list.size(); i++) {
            (this.list.get(i)).setOnClickListener(this);
        }
    }

    public void selectedSet(String str, List<TextView> list) {
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(String.valueOf(((TextView) list.get(i)).getId()))) {
                ((TextView) list.get(i)).setSelected(true);
            } else {
                ((TextView) list.get(i)).setSelected(false);
            }
        }
    }

    public void inputEdit() {
        this.etDiscountSales.setFilters(new InputFilter[]{new DiscountInputFilter()});
        this.etIncomeSales.setFilters(new InputFilter[]{new CashierInputFilter()});
    }

    private String getEtIncome() {
        return this.etIncomeSales.getText().toString().trim();
    }

    private String getEtAggregate() {
        return this.etAggregateSales.getText().toString().trim();
    }

    @Override
    public void onFailure(String str) {
        if ("salesReturnConsumer".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新操作");
        }
    }

    @Override
    public void onSuccess(String str) {
        if ("salesReturnConsumer".equals(str)) {
            finish();
            MainActivity.clear();
        }
    }

    @Override
    public void onAlert(String str) {
        if ("salesReturnConsumer".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新操作");
        }
    }
}
