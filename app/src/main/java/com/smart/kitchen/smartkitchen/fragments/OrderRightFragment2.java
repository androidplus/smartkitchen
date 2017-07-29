package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.RightOrderAdapter2;
import com.smart.kitchen.smartkitchen.entitys.DeliverySite;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.OrderLeftPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.OrderLeftView;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderRightFragment2 extends BaseFragment implements OnClickListener, OrderLeftView {
    private RightOrderAdapter2 adapter2;
    private boolean isInit = false;
    private ImageView ivOrderrightHead;
    private ImageView ivOrderrightHead1;
    private ImageView ivOrderrightMerchant;
    private List<OrderGoods> list;
    private SwipeMenuListView listView;
    private LinearLayout llYuYue;
    private OnListenerOnChange mListener;
    private int mPapertype = 0;
    private OrderInfo ord;
    private String orderDataStr;
    private OrderLeftPrestener prestener;
    private boolean printTag = false;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private TextView tvMoney;
    private TextView tvOrderAdd;
    private TextView tvOrderPrint;
    private TextView tvOrderRefund;
    private TextView tvOrderReturn;
    private TextView tvOrderTurndown;
    private TextView tvOrderrightDelivery;
    private TextView tvOrderrightKnight;
    private TextView tvOrderrightName;
    private TextView tvOrderrightName1;
    private TextView tvOrderrightNumber;
    private TextView tvOrderrightNumber1;
    private TextView tvOrderrightOrderstate;
    private TextView tvOrderrightOrderstate1;
    private TextView tvOrderrightOrdertime;
    private TextView tvOrderrightOrdertime1;
    private TextView tvOrderrightSite;
    private TextView tvOrderrightState;
    private TextView tvPay;
    private TextView tvRefund;
    private TextView tvRemark;
    private TextView tvWaimaiNape;

    public interface OnListenerOnChange {
        void onChange(String str);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.orderrightfragment2, null);
    }

    protected void initView(View view) {
        this.prestener = new OrderLeftPrestener(this, this.context, this.progressDialog);
        this.listView = (SwipeMenuListView) view.findViewById(R.id.listView);
        this.ivOrderrightHead = (ImageView) view.findViewById(R.id.iv_orderright_head);
        this.tvOrderrightName = (TextView) view.findViewById(R.id.tv_orderright_name);
        this.tvOrderrightOrdertime = (TextView) view.findViewById(R.id.tv_orderright_ordertime);
        this.ivOrderrightMerchant = (ImageView) view.findViewById(R.id.iv_orderright_merchant);
        this.tvOrderrightOrderstate = (TextView) view.findViewById(R.id.tv_orderright_orderstate);
        this.tvOrderrightNumber = (TextView) view.findViewById(R.id.tv_orderright_number);
        this.tvOrderrightSite = (TextView) view.findViewById(R.id.tv_orderright_site);
        this.tvOrderrightDelivery = (TextView) view.findViewById(R.id.tv_orderright_delivery);
        this.tvOrderrightState = (TextView) view.findViewById(R.id.tv_orderright_state);
        this.ivOrderrightHead1 = (ImageView) view.findViewById(R.id.iv_orderright_head1);
        this.tvOrderrightKnight = (TextView) view.findViewById(R.id.tv_orderright_knight);
        this.tvOrderrightName1 = (TextView) view.findViewById(R.id.tv_orderright_name1);
        this.tvOrderrightNumber1 = (TextView) view.findViewById(R.id.tv_orderright_number1);
        this.tvOrderrightOrdertime1 = (TextView) view.findViewById(R.id.tv_orderright_ordertime1);
        this.tvOrderrightOrderstate1 = (TextView) view.findViewById(R.id.tv_orderright_orderstate1);
        this.tvOrderRefund = (TextView) view.findViewById(R.id.tv_order_refund);
        this.llYuYue = (LinearLayout) view.findViewById(R.id.ll_yuyue);
        this.tvPay = (TextView) view.findViewById(R.id.tv_pay);
        this.tvRemark = (TextView) view.findViewById(R.id.tv_remark);
        this.tvWaimaiNape = (TextView) view.findViewById(R.id.tv_order_waimai_nape);
        this.tvMoney = (TextView) view.findViewById(R.id.tv_order_waimai_money);
        this.tvOrderAdd = (TextView) view.findViewById(R.id.tv_order_add);
        this.tvOrderReturn = (TextView) view.findViewById(R.id.tv_order_return);
        this.tvOrderTurndown = (TextView) view.findViewById(R.id.tv_order_norefund);
        this.tvOrderPrint = (TextView) view.findViewById(R.id.tv_order_print);
        this.tvRefund = (TextView) view.findViewById(R.id.tv_refund);
        initViewList();
        this.isInit = true;
        initSetData(this.orderDataStr);
    }

    protected void initEvent() {
        this.tvOrderAdd.setOnClickListener(this);
        this.tvOrderReturn.setOnClickListener(this);
        this.tvOrderTurndown.setOnClickListener(this);
        this.tvOrderPrint.setOnClickListener(this);
        this.tvOrderRefund.setOnClickListener(this);
    }

    protected void initData() {
        if (-1 != SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE)) {
            this.mPapertype = SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
    }

    protected void initViewList() {
        this.list = new ArrayList();
        this.adapter2 = new RightOrderAdapter2(this.context, this.list);
        this.listView.setAdapter(this.adapter2);
    }

    public void setData(String str) {
        this.orderDataStr = str;
        if (this.isInit) {
            initSetData(str);
        }
    }

    private void showrejectRefundBtn() {
        if (this.ord.getIs_need_refund() == 1) {
            this.tvOrderAdd.setVisibility(8);
            this.tvOrderReturn.setVisibility(8);
            if (this.ord.getRefund_opera() == 0) {
                this.tvOrderRefund.setVisibility(0);
                this.tvOrderTurndown.setVisibility(0);
                return;
            }
            this.tvOrderRefund.setVisibility(8);
            this.tvOrderTurndown.setVisibility(8);
            if (this.ord.getRefund_res() == 0) {
                this.tvRefund.setText("已接受该订单退款");
                return;
            } else if (this.ord.getRefund_res() == 1) {
                this.tvRefund.setText("已拒绝该订单退款");
                return;
            } else {
                return;
            }
        }
        this.tvRefund.setText("");
        this.tvOrderRefund.setVisibility(8);
        this.tvOrderTurndown.setVisibility(8);
    }

    private void initSetData(String str) {
        if (str != null) {
            this.ord = (OrderInfo) JSON.parseObject(str, new TypeReference<OrderInfo>() {
            }, new Feature[0]);
            if (this.list == null) {
                this.list = new ArrayList();
            }
            this.list.clear();
            this.list.addAll((Collection) JSON.parseObject(this.ord.getGoodslist(), new TypeReference<List<OrderGoods>>() {
            }, new Feature[0]));
            if (this.ord.getUserinfo() != null) {
                DeliverySite deliverySite = (DeliverySite) JSON.parseObject(this.ord.getUserinfo(), new TypeReference<DeliverySite>() {
                }, new Feature[0]);
                if (deliverySite.getReceiver() != null) {
                    this.tvOrderrightName.setText(deliverySite.getReceiver());
                } else {
                    this.tvOrderrightName.setText("");
                }
                if (deliverySite.getReceiverphone() != null) {
                    this.tvOrderrightNumber.setText(deliverySite.getReceiverphone());
                } else {
                    this.tvOrderrightNumber.setText("");
                }
                if (deliverySite.getAddress() != null) {
                    this.tvOrderrightSite.setText(deliverySite.getAddress());
                } else {
                    this.tvOrderrightSite.setText("");
                }
            } else {
                this.tvOrderrightName.setText("");
                this.tvOrderrightNumber.setText("");
                this.tvOrderrightSite.setText("");
            }
            if (this.ord.getWant_time() != null) {
                this.tvOrderrightDelivery.setText("期望送达时间:" + this.ord.getWant_time());
            } else {
                this.tvOrderrightDelivery.setText("期望送达时间:");
            }
            if (this.ord.getOrderdate() != null) {
                this.tvOrderrightOrdertime.setText("下单时间:" + this.ord.getOrderdate());
            } else {
                this.tvOrderrightOrdertime.setText("下单时间:");
            }
            if ("wxwaimai".equals(this.ord.getOrderfrom())) {
                this.ivOrderrightMerchant.setImageResource(R.mipmap.waimai_self);
            } else if ("meituan".equals(this.ord.getOrderfrom())) {
                this.ivOrderrightMerchant.setImageResource(R.mipmap.waimai_meituan);
            } else if ("eleme".equals(this.ord.getOrderfrom())) {
                this.ivOrderrightMerchant.setImageResource(R.mipmap.waimai_eleme);
            } else if ("baidu".equals(this.ord.getOrderfrom())) {
                this.ivOrderrightMerchant.setImageResource(R.mipmap.waimai_baidu);
            }
            if (this.ord.getIs_yuyue().intValue() == 0) {
                this.llYuYue.setVisibility(4);
            } else if (this.ord.getIs_yuyue().intValue() == 1) {
                this.llYuYue.setVisibility(0);
            }
            if ("0".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("待接单");
                this.tvOrderAdd.setVisibility(0);
                this.tvOrderReturn.setVisibility(0);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            } else if ("1".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("已接单");
                this.tvOrderAdd.setVisibility(8);
                this.tvOrderReturn.setVisibility(8);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            } else if ("2".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("配送中");
                this.tvOrderAdd.setVisibility(8);
                this.tvOrderReturn.setVisibility(8);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            } else if ("3".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("已完成");
                this.tvOrderAdd.setVisibility(8);
                this.tvOrderReturn.setVisibility(8);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            } else if ("4".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("已退货");
                this.tvOrderAdd.setVisibility(8);
                this.tvOrderReturn.setVisibility(8);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            } else if ("9".equals(this.ord.getOrderstatus())) {
                this.tvOrderrightState.setText("已作废");
                this.tvOrderAdd.setVisibility(8);
                this.tvOrderReturn.setVisibility(8);
                this.tvOrderTurndown.setVisibility(8);
                this.tvOrderRefund.setVisibility(8);
            }
            showrejectRefundBtn();
            if (this.ord.getMark() != null) {
                this.tvRemark.setText(this.ord.getMark());
            } else {
                this.tvRemark.setText("");
            }
            if (this.ord.getPay_style() != null) {
                this.tvPay.setText(this.ord.getPay_style());
            } else {
                this.tvPay.setText("");
            }
            if (this.ord.getTotalprice() != null) {
                this.tvMoney.setText(new DecimalFormat("###,##0.00").format(this.ord.getTotalprice()) + "");
            }
            this.tvWaimaiNape.setText(this.list.size() + "");
            this.adapter2.notifyDataSetChanged();
        }
    }

    public void notifyFragment() {
        if (this.ord != null) {
            this.ord = null;
            this.list.clear();
            this.adapter2.notifyDataSetChanged();
        }
        this.tvOrderrightName.setText("");
        this.tvOrderrightOrdertime.setText("下单时间:");
        this.tvOrderrightNumber.setText("");
        this.tvOrderrightSite.setText("");
        this.tvOrderrightDelivery.setText("期望送达时间:");
        this.llYuYue.setVisibility(4);
        this.tvOrderrightState.setText("");
        this.tvOrderAdd.setVisibility(8);
        this.tvOrderReturn.setVisibility(8);
        this.tvOrderTurndown.setVisibility(8);
        this.tvOrderRefund.setVisibility(8);
        this.tvRemark.setText("");
        this.tvPay.setText("");
        this.tvMoney.setText("");
        this.tvWaimaiNape.setText("");
        this.tvRefund.setText("");
    }

    public void onClick(View view) {
        if (this.ord == null) {
            Toasts.show(this.context, "没有订单");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_order_return:
                if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
                    return;
                }
                this.printTag = false;
                this.prestener.updateStatus("" + this.ord.getId(), 5, this.ord.getOrderfrom());
                return;
            case R.id.tv_order_add:
                if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
                    return;
                }
                this.printTag = true;
                this.prestener.updateStatus("" + this.ord.getId(), 1, this.ord.getOrderfrom());
                return;
            case R.id.tv_order_print:
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
            case R.id.tv_order_refund:
                this.prestener.confirmRefund("" + this.ord.getId());
                return;
            case R.id.tv_order_norefund:
                this.prestener.rejectRefund("" + this.ord.getId());
                return;
            default:
                return;
        }
    }

    public void onFial(String str) {
    }

    public void onSuccess(List<OrderInfo> list) {
    }

    public void showOrderList(List<OrderInfo> list) {
    }

    public void inform(String str) {
        if ("onSuccessWaiMaiStatus".equals(str)) {
            if (this.printTag) {
                try {
                    this.printUtil.delayPrint(this.context, "", this.ord, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.printTag = false;
            }
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if ("onAlertWaiMaiStatus".equals(str)) {
            if (this.mListener != null) {
                this.mListener.onChange("onChange");
            }
        } else if (!"onFailureWaiMaiStatus".equals(str)) {
            if ("onSuccessconfirmRefund".equals(str)) {
                Toasts.show(this.context, "退款已提交");
                if (this.mListener != null) {
                    this.mListener.onChange("onChange");
                }
            } else if ("onSuccessrejectRefund".equals(str)) {
                Toasts.show(this.context, "已驳回");
                if (this.mListener != null) {
                    this.mListener.onChange("onChange");
                }
            } else if ("onAlertconfirmRefund".equals(str)) {
                if (this.mListener != null) {
                    this.mListener.onChange("onChange");
                }
                Toasts.show(this.context, "退款失败");
            } else if ("onAlertrejectRefund".equals(str)) {
                Toasts.show(this.context, "驳回失败");
            }
        }
    }

    public void onListenerOnChange(OnListenerOnChange onListenerOnChange) {
        this.mListener = onListenerOnChange;
    }
}
