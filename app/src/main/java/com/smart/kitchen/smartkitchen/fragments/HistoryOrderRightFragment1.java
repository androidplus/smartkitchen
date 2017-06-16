package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.HistoryRightOrderAdapter1;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.presenter.HistoryLeftPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.HistoryOrderView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.DistoryPopuListener;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HistoryOrderRightFragment1 extends BaseFragment implements HistoryOrderView {
    private boolean isInit = false;
    private ArrayList<OrderGoods> list;
    private OnChangeListener listener;
    private ListView lvHistoryOrder;
    private HistoryRightOrderAdapter1 mAdapter;
    private int mPapertype = 0;
    private OrderInfo ord;
    private String orderDataStr;
    private HistoryLeftPrestener prestener;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private List<TextView> tableList;
    private TextView tvBenefitInfo;
    private TextView tvBenefitTotal;
    private TextView tvOrder;
    private TextView tvOrderAdd;
    private TextView tvOrderCancel;
    private TextView tvOrderDelete;
    private TextView tvOrderTotal;
    private TextView tvOrderTotalMoney;
    private TextView tvStaffNumber;
    private TextView tvTableNumber1;
    private TextView tvTableNumber2;
    private TextView tvTableNumber3;
    private TextView tvTableNumber4;
    private TextView tvTableNumber5;

    public interface OnChangeListener {
        void onChange(String str);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.historyorderrightfragment1, null);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    protected void initView(View view) {
        this.lvHistoryOrder = (ListView) view.findViewById(R.id.lv_history_order);
        this.tvBenefitInfo = (TextView) view.findViewById(R.id.tv_benefit_info);
        this.tvBenefitTotal = (TextView) view.findViewById(R.id.tv_benefit_total);
        this.tvStaffNumber = (TextView) view.findViewById(R.id.tv_staff_number);
        this.tvOrder = (TextView) view.findViewById(R.id.tv_order_peason);
        this.tvTableNumber1 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber1);
        this.tvTableNumber2 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber2);
        this.tvTableNumber3 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber3);
        this.tvTableNumber4 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber4);
        this.tvTableNumber5 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber5);
        this.tvOrderAdd = (TextView) view.findViewById(R.id.tv_order_add);
        this.tvOrderTotal = (TextView) view.findViewById(R.id.tv_order_total);
        this.tvOrderTotalMoney = (TextView) view.findViewById(R.id.tv_order_total_money);
        this.tvOrderCancel = (TextView) view.findViewById(R.id.tv_order_cancel);
        this.tvOrderDelete = (TextView) view.findViewById(R.id.tv_order_delete);
        this.isInit = true;
        initSetData(this.orderDataStr);
    }

    protected void initEvent() {
        this.prestener = new HistoryLeftPrestener(this, this.context, this.progressDialog);
        if (-1 != SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE)) {
            this.mPapertype = SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        this.tableList = new ArrayList();
        this.tableList.add(this.tvTableNumber1);
        this.tableList.add(this.tvTableNumber2);
        this.tableList.add(this.tvTableNumber3);
        this.tableList.add(this.tvTableNumber4);
        this.tableList.add(this.tvTableNumber5);
        this.tvOrderCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (HistoryOrderRightFragment1.this.ord != null) {
                    if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                        Toasts.show(HistoryOrderRightFragment1.this.context, HistoryOrderRightFragment1.this.getResources().getString(R.string.no_authority));
                    } else {
                        HistoryOrderRightFragment1.this.dialogUtils.showDistory(HistoryOrderRightFragment1.this.tvOrderAdd, "退货", new DialogUtils.OnClickListener() {
                            public void onClick(Object obj) {
                                HistoryOrderRightFragment1.this.dialogUtils.setDistoryPopuMessage(new DistoryPopuListener() {
                                    public void sendMessage(String str, String str2) {
                                        HistoryOrderRightFragment1.this.prestener.salesReturnOrder("" + HistoryOrderRightFragment1.this.ord.getId(), str);
                                    }
                                });
                            }
                        }, null);
                    }
                }
            }
        });
        this.tvOrderDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (HistoryOrderRightFragment1.this.ord != null) {
                    if (AuthorityUtil.getInstance().getRoleFlag() == 2 || AuthorityUtil.getInstance().permitPrint()) {
                        try {
                            HistoryOrderRightFragment1.this.printUtil.delayPrint(HistoryOrderRightFragment1.this.context, "", HistoryOrderRightFragment1.this.ord, 0);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    Toasts.show(HistoryOrderRightFragment1.this.context, HistoryOrderRightFragment1.this.getResources().getString(R.string.no_authority));
                }
            }
        });
    }

    protected void initData() {
        this.list = new ArrayList();
        initViewList();
    }

    private void initViewList() {
        this.mAdapter = new HistoryRightOrderAdapter1(this.context, this.list);
        this.lvHistoryOrder.setAdapter(this.mAdapter);
    }

    public void setData(String str) {
        this.orderDataStr = str;
        if (this.isInit) {
            initSetData(str);
        }
    }

    private void initSetData(String str) {
        int i = 0;
        if (str != null) {
            this.ord = (OrderInfo) JSON.parseObject(str, new TypeReference<OrderInfo>() {
            }, new Feature[0]);
            if (this.list == null) {
                this.list = new ArrayList();
            }
            this.list.clear();
            this.list.addAll((Collection) JSON.parseObject(this.ord.getGoodslist(), new TypeReference<List<OrderGoods>>() {
            }, new Feature[0]));
            this.tvStaffNumber.setText(this.ord.getWaiterid());
            if (this.ord.getUsersnum() != null) {
                this.tvOrder.setText(this.ord.getUsersnum() + "人");
            } else {
                this.tvOrder.setText("0人");
            }
            setTableList();
            if (!Contants.isEmpty(this.ord.getTablenumber())) {
                List list = (List) JSON.parseObject(this.ord.getTablenumber(), new TypeReference<List<TableNumber>>() {
                }, new Feature[0]);
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (this.tableList != null) {
                        ((TextView) this.tableList.get(i2)).setVisibility(0);
                        ((TextView) this.tableList.get(i2)).setText(((TableNumber) list.get(i2)).getArea_name() + "区" + ((TableNumber) list.get(i2)).getTable_name() + "桌");
                    }
                    if (i2 == 4) {
                        break;
                    }
                }
            }
            if (this.ord.getMark() != null) {
                this.tvOrderAdd.setText(this.ord.getMark());
            } else {
                this.tvOrderAdd.setText("");
            }
            int i3 = 0;
            while (i < this.list.size()) {
                int count;
                if (((OrderGoods) this.list.get(i)).getStatus() != 4) {
                    count = ((OrderGoods) this.list.get(i)).getCount() + i3;
                } else {
                    count = i3;
                }
                i++;
                i3 = count;
            }
            this.tvOrderTotal.setText(String.valueOf(i3));
            if (this.ord.getTotalprice() != null) {
                this.tvOrderTotalMoney.setText(new DecimalFormat("###,##0.00").format(this.ord.getTotalprice()) + "");
            }
            if (this.ord.getConponContent() != null) {
                this.tvBenefitInfo.setText(this.ord.getConponContent());
            } else {
                this.tvBenefitInfo.setText("无");
            }
            if (this.ord.getConponMoney() != null) {
                this.tvBenefitTotal.setText("优惠金额 :" + this.ord.getConponMoney() + "元");
            } else {
                this.tvBenefitTotal.setText("优惠金额 :0元");
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setTableList() {
        for (int i = 0; i < this.tableList.size(); i++) {
            ((TextView) this.tableList.get(i)).setVisibility(4);
        }
    }

    public void onFial(String str) {
    }

    public void onSuccess(List<OrderInfo> list) {
    }

    public void showOrderList(List<OrderInfo> list) {
    }

    public void inform(String str) {
        if ("onSuccess".equals(str)) {
            Toasts.show(this.context, "退货成功");
            if (this.listener != null) {
                this.listener.onChange("onChange");
            }
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "出错,请重新提交");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "退货失败");
        }
    }

    public void onChangeListener(OnChangeListener onChangeListener) {
        this.listener = onChangeListener;
    }
}
