package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.NewsParticularsAdapter;
import com.smart.kitchen.smartkitchen.entitys.DeliverySite;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.presenter.NewsParticularsPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.NewsParticularsView;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.ArrayList;
import java.util.List;

public class NewsParticularsActivity extends BaseFragmentActivity implements OnClickListener, NewsParticularsView {
    private static final String TAG = "NewsParticularsActivity";
    private MessageCenter center;
    private ImageView imgHeadPortrait;
    private ImageView imgWaimaiLogo;
    private LinearLayout llNewsBlack;
    private LinearLayout llNewsButton;
    private LinearLayout llNewsInformation;
    private LinearLayout llNewsTakeout;
    private LinearLayout llNewsWechat;
    private LinearLayout llWaimaiOrderStatus;
    private NewsParticularsAdapter mAdapter;
    private List<OrderGoods> mList = new ArrayList();
    private ListView mListView;
    private OrderInfo orderInfo;
    private NewsParticularsPresenter presenter;
    private boolean printTag = false;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private TextView tvCustomerAddress;
    private TextView tvCustomerName;
    private TextView tvCustomerPhone;
    private TextView tvInformationContent;
    private TextView tvInformationTitle;
    private TextView tvOrderReturn;
    private TextView tvOrderTime;
    private TextView tvOrderTurndown;
    private TextView tvPayType;
    private TextView tvQudan;
    private TextView tvRefund;
    private TextView tvServiceTime;
    private TextView tvTakeoutTitle;
    private TextView tvTotal;
    private TextView tvTotalMoney;
    private TextView tvWaimaiAccept;
    private TextView tvWaimaiAdd;
    private TextView tvWaimaiCancle;
    private TextView tvWaimaiDisAccept;
    private TextView tvWaimaiOrderStatus;
    private TextView tvWaimaiStatus;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_news);
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.imgHeadPortrait = (ImageView) findViewById(R.id.img_head_portrait);
        this.tvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
        this.tvCustomerPhone = (TextView) findViewById(R.id.tv_customer_phone);
        this.tvCustomerAddress = (TextView) findViewById(R.id.tv_customer_address);
        this.tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        this.imgWaimaiLogo = (ImageView) findViewById(R.id.img_waimai_logo);
        this.llWaimaiOrderStatus = (LinearLayout) findViewById(R.id.ll_waimai_order_status);
        this.tvWaimaiOrderStatus = (TextView) findViewById(R.id.tv_waimai_order_status);
        this.tvServiceTime = (TextView) findViewById(R.id.tv_service_time);
        this.tvWaimaiStatus = (TextView) findViewById(R.id.tv_waimai_status);
        this.mListView = (ListView) findViewById(R.id.listView);
        this.tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        this.tvWaimaiAdd = (TextView) findViewById(R.id.tv_waimai_add);
        this.tvTotal = (TextView) findViewById(R.id.tv_total);
        this.tvTotalMoney = (TextView) findViewById(R.id.tv_total_money);
        this.tvWaimaiAccept = (TextView) findViewById(R.id.tv_waimai_accept);
        this.tvWaimaiDisAccept = (TextView) findViewById(R.id.tv_waimai_disAccept);
        this.tvWaimaiCancle = (TextView) findViewById(R.id.tv_waimai_cancle);
        this.llNewsBlack = (LinearLayout) findViewById(R.id.ll_news_black);
        this.llNewsTakeout = (LinearLayout) findViewById(R.id.ll_news_takeout);
        this.llNewsInformation = (LinearLayout) findViewById(R.id.ll_news_information);
        this.tvInformationTitle = (TextView) findViewById(R.id.tv_information_title);
        this.tvInformationContent = (TextView) findViewById(R.id.tv_information_content);
        this.tvTakeoutTitle = (TextView) findViewById(R.id.tv_takeout_title);
        this.llNewsWechat = (LinearLayout) findViewById(R.id.ll_news_wechat);
        this.llNewsButton = (LinearLayout) findViewById(R.id.ll_news_button);
        this.tvOrderReturn = (TextView) findViewById(R.id.tv_order_return);
        this.tvOrderTurndown = (TextView) findViewById(R.id.tv_order_turndown);
        this.tvRefund = (TextView) findViewById(R.id.tv_refund);
        this.tvQudan = (TextView) findViewById(R.id.tv_qudan);
    }

    @Override
    protected void initEvent() {
        this.presenter = new NewsParticularsPresenter(this.context, this, this.progressDialog);
        this.center = (MessageCenter) getIntent().getSerializableExtra("MessageCenter");
        this.tvWaimaiAccept.setOnClickListener(this);
        this.tvWaimaiDisAccept.setOnClickListener(this);
        this.llNewsBlack.setOnClickListener(this);
        this.tvWaimaiCancle.setOnClickListener(this);
        this.tvOrderReturn.setOnClickListener(this);
        this.tvOrderTurndown.setOnClickListener(this);
        this.tvQudan.setOnClickListener(this);
        if (this.center.getMsg_type() != null) {
            if (this.center.getMsg_type().intValue() != 9) {
                this.presenter.takeOut(this.center.getMsg_id() + "");
            }
            if (this.center.getMsg_type().intValue() == 9) {
                this.llNewsInformation.setVisibility(View.VISIBLE);
                this.llNewsTakeout.setVisibility(View.GONE);
                this.tvInformationContent.setText(this.center.getMsg_content());
            }
        }
    }

    @Override
    protected void initData() {
        this.mAdapter = new NewsParticularsAdapter(this.context, this.mList);
        this.mListView.setAdapter(this.mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_news_black:
                finish();
                return;
            case R.id.tv_order_return:
                this.presenter.confirmRefund("" + this.orderInfo.getId());
                return;
            case R.id.tv_order_turndown:
                this.presenter.rejectRefund("" + this.orderInfo.getId());
                return;
            case R.id.tv_waimai_accept:
                if (this.orderInfo == null) {
                    return;
                }
                if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
                    return;
                }
                this.printTag = true;
                this.presenter.updateStatus("" + this.orderInfo.getId(), 1, this.orderInfo.getOrderfrom());
                return;
            case R.id.tv_waimai_disAccept:
                if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
                    return;
                } else if (this.orderInfo != null) {
                    this.printTag = false;
                    this.presenter.updateStatus("" + this.orderInfo.getId(), 5, this.orderInfo.getOrderfrom());
                    return;
                } else {
                    return;
                }
            case R.id.tv_qudan:
                startActivity(new Intent(this.context, OrderActivity.class));
                finish();
                return;
            case R.id.tv_waimai_cancle:
                finish();
                return;
            default:
                return;
        }
    }

    private void showRejectRefundBtn() {
        if (this.orderInfo.getIs_need_refund() == 1 && this.orderInfo.getRefund_opera() == 0) {
            this.tvOrderReturn.setVisibility(View.VISIBLE);
            this.tvOrderTurndown.setVisibility(View.VISIBLE);
            return;
        }
        this.tvRefund.setText("");
        this.tvOrderReturn.setVisibility(View.GONE);
        this.tvOrderTurndown.setVisibility(View.GONE);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                orderInfo =  JSON.parseObject(str, new TypeReference<OrderInfo>() {
                }, new Feature[0]);
                showRejectRefundBtn();
                List<OrderGoods> list = JSON.parseObject(orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
                }, new Feature[0]);
                if (this.center.getMsg_type().intValue() != 9) {
                    this.llNewsTakeout.setVisibility(View.VISIBLE);
                    this.llNewsInformation.setVisibility(View.GONE);
                    this.tvTakeoutTitle.setText(this.center.getMsg_content());
                    if (this.center.getMsg_type().intValue() == 0 || this.center.getMsg_type().intValue() == 5) {
                        this.tvCustomerName.setText("订单号 :" + this.orderInfo.getOrderid());
                        if (this.orderInfo.getOrderdate() != null) {
                            this.tvOrderTime.setText("下单时间 :" + this.orderInfo.getOrderdate());
                        } else {
                            this.tvOrderTime.setText("下单时间 :");
                        }
                        this.imgWaimaiLogo.setVisibility(View.GONE);
                        this.llWaimaiOrderStatus.setVisibility(View.GONE);
                        this.tvCustomerPhone.setVisibility(View.INVISIBLE);
                        this.tvQudan.setVisibility(View.VISIBLE);
                        List<TableNumber> list2 = JSON.parseObject(this.orderInfo.getTablenumber(), new TypeReference<List<TableNumber>>() {
                        }, new Feature[0]);
                        StringBuffer stringBuffer = new StringBuffer();
                        if (list2.size() > 1) {
                            stringBuffer.append(( list2.get(0)).getArea_name() + ( list2.get(0)).getTable_name());
                            this.tvCustomerAddress.setText(stringBuffer);
                        }
                        this.tvServiceTime.setVisibility(View.INVISIBLE);
                        this.tvWaimaiStatus.setVisibility(View.INVISIBLE);
                        if (this.orderInfo.getPay_style() != null) {
                            this.tvPayType.setText("支付方式 :" + this.orderInfo.getPay_style());
                        } else {
                            this.tvPayType.setText("支付方式 :");
                        }
                        if (this.orderInfo.getMark() != null) {
                            this.tvWaimaiAdd.setText("备注 :" + this.orderInfo.getMark());
                        } else {
                            this.tvWaimaiAdd.setText("备注 :");
                        }
                        this.tvTotal.setText(list.size() + "");
                        this.tvTotalMoney.setText(this.orderInfo.getTotalprice() + "");
                        this.tvWaimaiAccept.setVisibility(View.GONE);
                        this.tvWaimaiDisAccept.setVisibility(View.GONE);
                    } else if (this.center.getMsg_type().intValue() == 1 || this.center.getMsg_type().intValue() == 2 || this.center.getMsg_type().intValue() == 3 || this.center.getMsg_type().intValue() == 4) {
                        if (this.center.getFlag().intValue() == 8) {
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                            if (this.orderInfo.getIs_need_refund() != 1) {
                                this.tvRefund.setText("");
                                this.tvOrderReturn.setVisibility(View.GONE);
                                this.tvOrderTurndown.setVisibility(View.GONE);
                            } else if (this.orderInfo.getRefund_opera() == 0) {
                                this.tvOrderReturn.setVisibility(View.VISIBLE);
                                this.tvOrderTurndown.setVisibility(View.VISIBLE);
                            } else {
                                this.tvOrderReturn.setVisibility(View.GONE);
                                this.tvOrderTurndown.setVisibility(View.GONE);
                                if (this.orderInfo.getRefund_res() == 0) {
                                    this.tvRefund.setText("已接受该订单退款");
                                } else if (this.orderInfo.getRefund_res() == 1) {
                                    this.tvRefund.setText("已拒绝该订单退款");
                                }
                            }
                        }
                        this.tvQudan.setVisibility(View.GONE);
                        DeliverySite deliverySite = JSON.parseObject(this.orderInfo.getUserinfo(), new TypeReference<DeliverySite>() {
                        }, new Feature[0]);
                        this.tvCustomerName.setText(deliverySite.getReceiver());
                        if (this.orderInfo.getOrderdate() != null) {
                            this.tvOrderTime.setText("下单时间 :" + this.orderInfo.getOrderdate().substring(0, 10));
                        } else {
                            this.tvOrderTime.setText("下单时间 :");
                        }
                        if ("wxwaimai".equals(this.orderInfo.getOrderfrom())) {
                            this.imgWaimaiLogo.setImageResource(R.mipmap.waimai_self);
                        } else if ("meituan".equals(this.orderInfo.getOrderfrom())) {
                            this.imgWaimaiLogo.setImageResource(R.mipmap.waimai_meituan);
                        } else if ("eleme".equals(this.orderInfo.getOrderfrom())) {
                            this.imgWaimaiLogo.setImageResource(R.mipmap.waimai_eleme);
                        } else if ("baidu".equals(this.orderInfo.getOrderfrom())) {
                            this.imgWaimaiLogo.setImageResource(R.mipmap.waimai_baidu);
                        }
                        if (this.orderInfo.getIs_yuyue().intValue() == 0) {
                            this.llWaimaiOrderStatus.setVisibility(View.INVISIBLE);
                        }
                        this.tvCustomerPhone.setText(deliverySite.getReceiverphone());
                        this.tvCustomerAddress.setText(deliverySite.getAddress());
                        if (this.orderInfo.getWant_time() != null) {
                            this.tvServiceTime.setVisibility(View.VISIBLE);
                            this.tvServiceTime.setText("期望送达时间 :" + this.orderInfo.getWant_time());
                        } else {
                            this.tvServiceTime.setVisibility(View.VISIBLE);
                            this.tvServiceTime.setText("期望送达时间 :");
                        }
                        if ("0".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("未接单");
                            this.tvWaimaiAccept.setVisibility(View.VISIBLE);
                            this.tvWaimaiDisAccept.setVisibility(View.VISIBLE);
                        } else if ("1".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("已接单");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("2".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("配送中");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("3".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("已完成");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("4".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("已退货");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("5".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("没有接单");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("6".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("已驳回");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        } else if ("9".equals(this.orderInfo.getOrderstatus())) {
                            this.tvWaimaiStatus.setText("已作废");
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        }
                        if (this.center.getMsg_type().intValue() == 5) {
                            this.tvWaimaiAccept.setVisibility(View.GONE);
                            this.tvWaimaiDisAccept.setVisibility(View.GONE);
                        }
                        if (this.orderInfo.getPay_style() != null) {
                            this.tvPayType.setText("支付方式 :" + this.orderInfo.getPay_style());
                        }
                        if (this.orderInfo.getMark() != null) {
                            this.tvWaimaiAdd.setText("备注 :" + this.orderInfo.getMark());
                        } else {
                            this.tvWaimaiAdd.setText("备注 :");
                        }
                        this.tvTotal.setText(list.size() + "");
                        this.tvTotalMoney.setText(this.orderInfo.getTotalprice() + "");
                    }
                    this.mList.clear();
                    this.mList.addAll(list);
                    this.mAdapter.notifyDataSetChanged();
                    return;
                } else if (this.center.getMsg_type().intValue() == 9) {
                    this.tvInformationTitle.setText("库存预警");
                    this.llNewsInformation.setVisibility(View.VISIBLE);
                    this.llNewsTakeout.setVisibility(View.GONE);
                    this.tvInformationContent.setText(this.center.getMsg_content());
                    return;
                } else {
                    return;
                }
            case 1:
                try {
                    if (this.printTag) {
                        this.printUtil.delayPrint(this.context, "", this.orderInfo, 1);
                        this.printTag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.presenter.takeOut(this.orderInfo.getId() + "");
                return;
            case 6:
                this.presenter.takeOut(this.orderInfo.getId() + "");
                return;
            case 7:
                this.presenter.takeOut(this.orderInfo.getId() + "");
                return;
            default:
                return;
        }
    }

    @Override
    public void onAlert(int i, String str) {
        switch (i) {
            case 0:
                this.llNewsInformation.setVisibility(View.VISIBLE);
                this.llNewsTakeout.setVisibility(View.GONE);
                this.llNewsButton.setVisibility(View.INVISIBLE);
                this.tvInformationTitle.setText("查询不到该订单");
                return;
            case 1:
                Toasts.show(this.context, "网络发生错误,请重新发送");
                return;
            case 6:
                Toasts.showShort(this.context, "退款提交失败");
                return;
            case 7:
                Toasts.showShort(this.context, "驳回失败");
                return;
            default:
                return;
        }
    }

    @Override
    public void onFailure(int i, String str) {
        switch (i) {
            case 0:
                this.llNewsInformation.setVisibility(View.VISIBLE);
                this.llNewsTakeout.setVisibility(View.GONE);
                this.llNewsButton.setVisibility(View.INVISIBLE);
                this.tvInformationTitle.setText("消息加载失败");
                return;
            case 1:
                Toasts.show(this.context, "网络发生错误,请重新发送");
                return;
            case 6:
                Toasts.showShort(this.context, "网络请求失败");
                return;
            case 7:
                Toasts.showShort(this.context, "网络请求失败");
                return;
            default:
                return;
        }
    }
}
