package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.AddToActivity;
import com.smart.kitchen.smartkitchen.activity.PayActivity;
import com.smart.kitchen.smartkitchen.activity.TableActivity;
import com.smart.kitchen.smartkitchen.adapters.RightOrderAdapter1;
import com.smart.kitchen.smartkitchen.adapters.RightOrderAdapter1.OnItemClickListener;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.presenter.OrderLeftPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.OrderLeftView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.DistoryPopuListener;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.SwipeListUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderRightFragment1 extends BaseFragment implements OnClickListener, OnItemClickListener, OrderLeftView {
    private static final String TAG = "OrderRightFragment1";
    private RightOrderAdapter1 adapter1;
    private boolean isInit = false;
    private List<OrderGoods> list;
    private SwipeMenuListView listView;
    private OnListenerOnChange mListener;
    private int mPapertype = 0;
    private OrderInfo ord;
    private String orderDataStr;
    private OrderLeftPrestener prestener;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private boolean servingTag = true;
    private List<TextView> tableList;
    private TextView tvIncome;
    private TextView tvOederrightOrderpersonnel;
    private TextView tvOrderAdd;
    private TextView tvOrderCancel;
    private TextView tvOrderChangeTable;
    private TextView tvOrderTotal;
    private TextView tvOrderTotalMoney;
    private TextView tvOrderrightPay;
    private TextView tvOrderrightPeoples;
    private TextView tvOrderrightRemark;
    private TextView tvOrderrightTablenumber1;
    private TextView tvOrderrightTablenumber2;
    private TextView tvOrderrightTablenumber3;
    private TextView tvOrderrightTablenumber4;
    private TextView tvOrderrightTablenumber5;
    private TextView tvPrint;

    public interface OnListenerOnChange {
        void onChange(String str);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.orderrightfragment1, null);
    }

    protected void initView(View view) {
        this.listView = (SwipeMenuListView) view.findViewById(R.id.listView);
        this.tvOrderTotal = (TextView) view.findViewById(R.id.tv_order_total);
        this.tvOrderTotalMoney = (TextView) view.findViewById(R.id.tv_order_total_money);
        this.tvOrderAdd = (TextView) view.findViewById(R.id.tv_order_add);
        this.tvOrderChangeTable = (TextView) view.findViewById(R.id.tv_order_change_table);
        this.tvOrderCancel = (TextView) view.findViewById(R.id.tv_order_cancel);
        this.tvIncome = (TextView) view.findViewById(R.id.tv_income);
        this.tvPrint = (TextView) view.findViewById(R.id.tv_print);
        this.tvOederrightOrderpersonnel = (TextView) view.findViewById(R.id.tv_oederright_orderpersonnel);
        this.tvOrderrightTablenumber1 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber1);
        this.tvOrderrightTablenumber2 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber2);
        this.tvOrderrightTablenumber3 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber3);
        this.tvOrderrightTablenumber4 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber4);
        this.tvOrderrightTablenumber5 = (TextView) view.findViewById(R.id.tv_orderright_tablenumber5);
        this.tvOrderrightPeoples = (TextView) view.findViewById(R.id.tv_orderright_peoples);
        this.tvOrderrightRemark = (TextView) view.findViewById(R.id.tv_orderright_remark);
        this.tvOrderrightPay = (TextView) view.findViewById(R.id.tv_oederright_pay);
        this.isInit = true;
        initSetData(this.orderDataStr);
    }

    protected void initEvent() {
        this.prestener = new OrderLeftPrestener(this, this.context, this.progressDialog);
        this.tvPrint.setOnClickListener(this);
        this.tvOrderAdd.setOnClickListener(this);
        this.tvOrderChangeTable.setOnClickListener(this);
        this.tvOrderCancel.setOnClickListener(this);
        this.tvIncome.setOnClickListener(this);
        this.tableList = new ArrayList();
        this.tableList.add(this.tvOrderrightTablenumber1);
        this.tableList.add(this.tvOrderrightTablenumber2);
        this.tableList.add(this.tvOrderrightTablenumber3);
        this.tableList.add(this.tvOrderrightTablenumber4);
        this.tableList.add(this.tvOrderrightTablenumber5);
    }

    protected void initData() {
        if (-1 != SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE)) {
            this.mPapertype = SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        this.list = new ArrayList();
        initViewList();
        String[] strArr = new String[]{"退货"};
        SwipeListUtils.addMenuItem(this.context, new String[][]{strArr}, new int[]{R.drawable.corner_black}, this.listView, new OnMenuItemClickListener() {
            public boolean onMenuItemClick(final int i, com.baoyz.swipemenulistview.SwipeMenu aVar, int i2) {
                if (OrderRightFragment1.this.ord.getPay_status().intValue() == 1) {
                    Toasts.show(OrderRightFragment1.this.context, "该订单已支付,不可作废");
                } else if (OrderRightFragment1.this.ord.getIs_pay_his() == 1) {
                    Toasts.show(OrderRightFragment1.this.context, "该订单已支付过,不可作废");
                } else if (((OrderGoods) OrderRightFragment1.this.list.get(i)).getStatus() != 4) {
                    OrderRightFragment1.this.dialogUtils.showDistory2(OrderRightFragment1.this.tvOrderAdd, ((OrderGoods) OrderRightFragment1.this.list.get(i)).getCount(), new DialogUtils.OnClickListener() {
                        public void onClick(Object obj) {
                            OrderRightFragment1.this.dialogUtils.setDistoryPopuMessage(new DistoryPopuListener() {
                                public void sendMessage(String str, String str2) {
                                    OrderGoods orderGoods = (OrderGoods) OrderRightFragment1.this.list.get(i);
                                    OrderRightFragment1.this.prestener.salesReturn(orderGoods.getOrderDetailId(), OrderRightFragment1.this.ord.getOrderid(), Integer.valueOf(str2).intValue(), JSON.toJSONString(orderGoods), str, new UserInfoDaoManager().getNowUserInfo().getUserid(), SPUtils.getUserinfo(OrderRightFragment1.this.context, SPUtils.STORE_ID));
                                }
                            });
                        }
                    }, null);
                }
                return true;
            }
        });
    }

    private void initViewList() {
        this.adapter1 = new RightOrderAdapter1(this.context, this.list);
        this.adapter1.setListener(this);
        this.listView.setAdapter(this.adapter1);
    }

    public void onClick(View view) {
        if (this.ord == null) {
            Toasts.show(this.context, "没有订单");
            return;
        }
        Intent intent;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.tv_order_add:
                if (this.ord.getPay_status().intValue() == 1) {
                    Toasts.show(this.context, "该订单已支付");
                    return;
                } else if (this.ord.getIs_pay_his() == 1) {
                    Toasts.show(this.context, "该订单已支付过,不可追加");
                    return;
                } else {
                    intent = new Intent(getActivity(), AddToActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("ORD", this.ord);
                    intent.putExtras(bundle);
                    intent.putExtra("TYPE", "OrderRight");
                    startActivity(intent);
                    return;
                }
            case R.id.tv_order_cancel:
                if (AuthorityUtil.getInstance().getRoleFlag() != 2 && !AuthorityUtil.getInstance().permitBreakOrder()) {
                    Toasts.show(this.context, getActivity().getResources().getString(R.string.no_authority));
                    return;
                } else if (this.ord.getPay_status().intValue() == 1) {
                    Toasts.show(this.context, "该订单已支付,不可作废");
                    return;
                } else if (this.ord.getIs_pay_his() == 1) {
                    Toasts.show(this.context, "该订单已支付过,不可作废");
                    return;
                } else {
                    this.dialogUtils.showDistory(this.tvOrderAdd, "作废", new DialogUtils.OnClickListener() {
                        public void onClick(Object obj) {
                            OrderRightFragment1.this.dialogUtils.setDistoryPopuMessage(new DistoryPopuListener() {
                                public void sendMessage(String str, String str2) {
                                    OrderRightFragment1.this.prestener.onCancellation(OrderRightFragment1.this.ord.getOrderid(), new UserInfoDaoManager().getNowUserInfo().getUserid(), str);
                                }
                            });
                        }
                    }, null);
                    return;
                }
            case R.id.tv_order_change_table:
                intent = new Intent(getActivity(), TableActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("ORD", this.ord);
                intent.putExtras(bundle);
                intent.putExtra("TYPE", "OrderRight");
                startActivity(intent);
                return;
            case R.id.tv_income:
                if (AuthorityUtil.getInstance().getRoleFlag() != 2 && !AuthorityUtil.getInstance().permitCollectMoney()) {
                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
                    return;
                } else if (this.ord.getPay_status().intValue() == 1) {
                    Toasts.show(this.context, "该订单已支付");
                    return;
                } else {
                    intent = new Intent(getActivity(), PayActivity.class);
                    bundle = new Bundle();
                    bundle.putSerializable("ORD", this.ord);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
            case R.id.tv_print:
                if (AuthorityUtil.getInstance().getRoleFlag() == 2 || AuthorityUtil.getInstance().permitPrint()) {
                    try {
                        this.printUtil.delayPrint(this.context, "", this.ord, 0);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                return;
            default:
                return;
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
            this.tvOederrightOrderpersonnel.setText(this.ord.getWaiterid());
            if (this.ord.getUsersnum() != null) {
                this.tvOrderrightPeoples.setText(this.ord.getUsersnum() + "人");
            }
            setTableList();
            if (!Contants.isEmpty(this.ord.getTablenumber())) {
                List list = (List) JSON.parseObject(this.ord.getTablenumber(), new TypeReference<List<TableNumber>>() {
                }, new Feature[0]);
                if (this.tableList != null) {
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        ((TextView) this.tableList.get(i2)).setVisibility(View.VISIBLE);
                        ((TextView) this.tableList.get(i2)).setText(((TableNumber) list.get(i2)).getArea_name() + "区" + ((TableNumber) list.get(i2)).getTable_name() + "桌");
                        if (i2 == 4) {
                            break;
                        }
                    }
                }
            }
            if (this.ord.getPay_status().intValue() == 0) {
                this.tvOrderrightPay.setText("未支付");
            } else if (this.ord.getPay_status().intValue() == 1) {
                this.tvOrderrightPay.setText("已支付");
            }
            if (this.ord.getMark() != null) {
                this.tvOrderrightRemark.setText(this.ord.getMark());
            } else {
                this.tvOrderrightRemark.setText("");
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
            this.adapter1.notifyDataSetChanged();
        }
    }

    public void setData(String str) {
        this.orderDataStr = str;
        if (this.isInit) {
            initSetData(str);
        }
    }

    public void setTableList() {
        for (int i = 0; i < this.tableList.size(); i++) {
            ((TextView) this.tableList.get(i)).setVisibility(View.INVISIBLE);
        }
    }

    private void getOffTheStocks() {
        for (int i = 0; i < this.list.size(); i++) {
            if (((OrderGoods) this.list.get(i)).getStatus() == 0) {
                this.servingTag = false;
                break;
            }
        }
        if (this.ord.getPay_status().intValue() == 1 && this.servingTag) {
            this.prestener.finishOrders(this.ord.getOrderid(), new UserInfoDaoManager().getNowUserInfo().getUserid());
        }
    }

    public void notifyFragment() {
        if (this.ord != null) {
            this.ord = null;
            this.list.clear();
            this.adapter1.notifyDataSetChanged();
            this.tvOederrightOrderpersonnel.setText("");
            this.tvOrderrightPeoples.setText("0人");
            for (int i = 0; i < this.tableList.size(); i++) {
                ((TextView) this.tableList.get(i)).setVisibility(View.INVISIBLE);
            }
            this.tvOrderTotal.setText("0");
            this.tvOrderTotalMoney.setText("0");
            this.tvOrderrightRemark.setText("");
        }
    }

    public void onItemClick(final int i, int i2) {
        if (i2 == 0) {
            this.prestener.updateorderGoods(((OrderGoods) this.list.get(i)).getOrderDetailId(), "1");
            return;
        }
        this.dialogUtils.showConfirm(this.listView, "确认设置吗", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
            public void onClick(Object obj) {
                OrderRightFragment1.this.prestener.updateorderGoods(((OrderGoods) OrderRightFragment1.this.list.get(i)).getOrderDetailId(), "0");
            }
        }, null);
    }

    public void onFial(String str) {
    }

    public void onSuccess(List<OrderInfo> list) {
    }

    public void showOrderList(List<OrderInfo> list) {
    }

    public void inform(String str) {
        if ("onSuccess".equals(str)) {
            Toasts.showShort(this.context, (CharSequence) "作废成功");
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "作废失败,请重新提交");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "作废失败,请重新提交");
        }
        if ("onSuccessSalesReturn".equals(str)) {
            Toasts.showShort(this.context, (CharSequence) "退菜成功");
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if ("onAlertSalesReturn".equals(str)) {
            Toasts.show(this.context, "退菜失败,请重新提交");
        } else if ("onFailureSalesReturn".equals(str)) {
            Toasts.show(this.context, "退菜失败,请重新提交");
        }
        if ("onSuccessUpdateorderGoods".equals(str)) {
            Toasts.showShort(this.context, (CharSequence) "修改成功");
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if ("onAlertUpdateorderGoods".equals(str)) {
            Toasts.show(this.context, "修改失败,请重新提交");
        } else if ("onFailureUpdateorderGoods".equals(str)) {
            Toasts.show(this.context, "修改失败,请重新提交");
        }
        if ("onSuccessFinishOrders".equals(str)) {
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if (!"onAlertFinishOrders".equals(str) && "onFailureFinishOrders".equals(str)) {
        }
    }

    public void onListenerOnChange(OnListenerOnChange onListenerOnChange) {
        this.mListener = onListenerOnChange;
    }
}
