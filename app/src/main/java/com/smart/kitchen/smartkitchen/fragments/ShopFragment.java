package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.activity.OrderActivity;
import com.smart.kitchen.smartkitchen.activity.PayActivity;
import com.smart.kitchen.smartkitchen.activity.TableActivity;
import com.smart.kitchen.smartkitchen.adapters.ShopAdapter;
import com.smart.kitchen.smartkitchen.adapters.ShopAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.TablePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.TableView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.DistoryPopuListener;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.OrderNumber;
import com.smart.kitchen.smartkitchen.utils.OrderUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopFragment extends BaseFragment implements OnItemClickListener, TableView {
    private static final String TAG = "ShopFragment";
    private ShopAdapter adapter;
    private TextView buttonShop;
    private View line1;
    private View line2;
    private List<OrderGoods> list = new ArrayList();
    private ListView listView;
    private LinearLayout llCheckout;
    private LinearLayout llContainer;
    private OrderInfo orderInfo;
    private TablePresenter presenter;
    private String remark = "";
    private TextView shopCollectMoney;
    private TextView totalCount;
    private TextView totalMoney;
    private TextView tvClear;
    private TextView tvGuadan;
    private TextView tvQudan;
    private TextView tvShopIntegral;
    private TextView tvShopVipId;
    private TextView tvShopVipMoney;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_shop, null);
    }

    public void change() {
        if (MainActivity.shoppingCarMap.size() == 0) {
            this.llContainer.setVisibility(8);
            this.tvQudan.setVisibility(0);
        } else {
            this.llContainer.setVisibility(0);
            this.tvQudan.setVisibility(8);
        }
        LogUtils.e(TAG, "change: ");
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
        this.totalCount.setText("合计：" + i);
        this.totalMoney.setText("总额: " + new DecimalFormat("###,##0.00").format(valueOf));
        if (this.adapter == null) {
            this.adapter = new ShopAdapter(this.context, this.list);
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
        this.tvGuadan = (TextView) view.findViewById(R.id.tv_guadan);
        this.tvQudan = (TextView) view.findViewById(R.id.tv_qudan);
        this.tvClear = (TextView) view.findViewById(R.id.tv_clear);
        this.shopCollectMoney = (TextView) view.findViewById(R.id.shop_collectMoney);
        this.llContainer = (LinearLayout) view.findViewById(R.id.ll_guadan_container);
        this.llCheckout = (LinearLayout) view.findViewById(R.id.ll_checkout);
        this.tvShopVipId = (TextView) view.findViewById(R.id.tv_shop_vipid);
        this.tvShopVipMoney = (TextView) view.findViewById(R.id.tv_shop_vipmoney);
        this.tvShopIntegral = (TextView) view.findViewById(R.id.tv_shop_integral);
        this.buttonShop = (TextView) view.findViewById(R.id.button_shop);
        this.line1 = view.findViewById(R.id.line_1);
        this.line2 = view.findViewById(R.id.line_2);
        this.line1.setVisibility(0);
        this.line2.setVisibility(0);
    }

    protected void initEvent() {
        this.presenter = new TablePresenter(this.context, this, this.progressDialog);
        this.tvClear.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                ShopFragment.this.dialogUtils.showConfirm(view2, "确定要清空吗？", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        MainActivity.clear();
                    }
                }, null);
            }
        });
        this.tvQudan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShopFragment.this.getArea()) {
                    Toasts.show(ShopFragment.this.context, "请先至设置界面选择区域");
                    return;
                }
                ShopFragment.this.startActivity(new Intent(ShopFragment.this.context, OrderActivity.class));
            }
        });
        this.tvGuadan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShopFragment.this.getArea()) {
                    Toasts.show(ShopFragment.this.context, "请先至设置界面选择区域");
                    return;
                }
                ShopFragment.this.getOrderInfo();
                Intent intent = new Intent(ShopFragment.this.getActivity(), TableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ORD", ShopFragment.this.orderInfo);
                intent.putExtras(bundle);
                intent.putExtra("TYPE", "Shop");
                ShopFragment.this.startActivity(intent);
            }
        });
        this.shopCollectMoney.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ShopFragment.this.getArea()) {
                    Toasts.show(ShopFragment.this.context, "请先至设置界面选择区域");
                } else if (AuthorityUtil.getInstance().getRoleFlag() != 2 && !AuthorityUtil.getInstance().permitCollectMoney()) {
                    Toasts.show(ShopFragment.this.context, ShopFragment.this.getResources().getString(R.string.no_authority));
                } else if (MainActivity.shoppingCarMap.size() == 0) {
                    Toasts.showLong(ShopFragment.this.getContext(), (CharSequence) "请先添加菜品");
                } else {
                    ShopFragment.this.getOrderInfo();
                    ShopFragment.this.presenter.submitIndents(ShopFragment.this.orderInfo);
                }
            }
        });
        this.buttonShop.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.shoppingCarMap.size() == 0) {
                    Toasts.showLong(ShopFragment.this.getContext(), (CharSequence) "请先添加菜品");
                } else {
                    ShopFragment.this.dialogUtils.showDistory(ShopFragment.this.buttonShop, "添加备注", new DialogUtils.OnClickListener() {
                        public void onClick(Object obj) {
                            ShopFragment.this.dialogUtils.setDistoryPopuMessage(new DistoryPopuListener() {
                                public void sendMessage(String str, String str2) {
                                    ShopFragment.this.remark = str;
                                }
                            });
                        }
                    }, null);
                }
            }
        });
    }

    private OrderInfo getOrderInfo() {
        this.orderInfo = new OrderInfo();
        String orderNumber = OrderNumber.getInstance().getOrderNumber(this.context);
        this.orderInfo.setOrderid(orderNumber);
        this.orderInfo.setOrderfrom("tangchi");
        this.orderInfo.setOrderdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        this.orderInfo.setOrderstatus("0");
        for (int i = 0; i < MainActivity.shoppingCarMap.size(); i++) {
            ((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoods().setOrderid(orderNumber);
        }
        if (!TextUtils.isEmpty(this.remark)) {
            this.orderInfo.setMark(this.remark);
        }
        this.orderInfo.setGoodslist(JSON.toJSONString(MainActivity.shoppingCarMap));
        this.orderInfo.setTotalprice(new OrderUtils().getTotalmoney(MainActivity.shoppingCarMap));
        this.orderInfo.setWaiterid(new UserInfoDaoManager().getNowUserInfo().getUserid());
        this.orderInfo.setPay_status(Integer.valueOf(0));
        return this.orderInfo;
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

    public void setVipInfo(VipInfo vipInfo, boolean z) {
        if (z) {
            this.tvShopVipId.setText(vipInfo.getUsername());
            this.tvShopVipMoney.setText("余额 : " + vipInfo.getMoney());
            this.tvShopIntegral.setText("积分 : " + vipInfo.getJf());
            return;
        }
        this.tvShopVipId.setText("");
        this.tvShopVipMoney.setText("余额 : ");
        this.tvShopIntegral.setText("积分 : ");
    }

    public boolean getArea() {
        return TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA));
    }

    public void onSuccess(List<TableArea> list) {
    }

    public void onFail() {
    }

    public void ShowTableArea(List<TableArea> list) {
    }

    public void isSubmitIndent(String str) {
        if ("settleAccountsOnSuccess".equals(str)) {
            Intent intent = new Intent(getActivity(), PayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORD", this.orderInfo);
            intent.putExtras(bundle);
            startActivity(intent);
            MainActivity.clear();
        } else if ("settleAccountsOnAlert".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新点击");
        } else if ("settleAccountsOnFailure".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新点击");
        }
    }
}
