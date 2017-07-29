package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.HistoryOrderActivity;
import com.smart.kitchen.smartkitchen.adapters.HistoryLeftOrderAdapter;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.HistoryLeftPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.HistoryOrderView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.ScreenListener;
import com.smart.kitchen.smartkitchen.utils.orderScreenUtils;
import com.smart.kitchen.smartkitchen.view.SwipeRefreshView;
import com.smart.kitchen.smartkitchen.view.SwipeRefreshView.OnLoadListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.http.HttpStatus;

public class HistoryOrderLeftFragment extends BaseFragment implements OnRefreshListener, OnClickListener, HistoryOrderView, OnLoadListener {
    private static final String TAG = "HistoryOrderLeftFragmen";
    private HistoryLeftOrderAdapter adapter;
    private boolean isInit = false;
    boolean isRefresh = true;
    private List<OrderInfo> list = new ArrayList();
    private List<OrderInfo> listTangchi = new ArrayList();
    private List<OrderInfo> listTmp = new ArrayList();
    private ListView listView;
    private List<OrderInfo> listWaimai = new ArrayList();
    private LinearLayout llFinish;
    private LinearLayout llScreen;
    private OnDataTransmissionListener mListener;
    private SwipeRefreshView mSwipeLayout;
    int page = 1;
    private HistoryLeftPrestener prestener;
    private int seTndex = 0;
    private int select = 0;
    private int selectIndex = 0;
    private OrderInfo selectOrderinfo;
    private TextView tvTangchi;
    private TextView tvWaiMai;

    public interface OnDataTransmissionListener {
        void dataTransmission(String str, int i);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.orderleftfragment, null);
    }

    protected void initView(View view) {
        this.llScreen = (LinearLayout) view.findViewById(R.id.ll_shaixuan);
        this.llFinish = (LinearLayout) view.findViewById(R.id.ll_finish);
        this.tvWaiMai = (TextView) view.findViewById(R.id.tv_waimai);
        this.tvTangchi = (TextView) view.findViewById(R.id.tv_tangchi);
        this.listView = (ListView) view.findViewById(R.id.listView);
        this.mSwipeLayout = (SwipeRefreshView) view.findViewById(R.id.swipe_container);
        ((TextView) view.findViewById(R.id.tv_refresh)).setVisibility(8);
    }

    protected void initEvent() {
        this.prestener = new HistoryLeftPrestener(this, this.context, this.progressDialog);
        this.mSwipeLayout.setOnRefreshListener(this);
        this.llScreen.setOnClickListener(this);
        this.llFinish.setOnClickListener(this);
        this.tvWaiMai.setOnClickListener(this);
        this.tvTangchi.setOnClickListener(this);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                HistoryOrderLeftFragment.this.adapter.setSelect(i);
                HistoryOrderLeftFragment.this.selectOrderinfo = (OrderInfo) HistoryOrderLeftFragment.this.list.get(i);
                HistoryOrderLeftFragment.this.selectIndex = i;
                if (HistoryOrderLeftFragment.this.mListener != null) {
                    HistoryOrderLeftFragment.this.mListener.dataTransmission(JSON.toJSONString(HistoryOrderLeftFragment.this.list.get(i)), HistoryOrderLeftFragment.this.select);
                }
            }
        });
        this.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                HistoryOrderLeftFragment.this.seTndex = i;
            }
        });
        this.dialogUtils.screenListener(new ScreenListener() {
            public void type(String str, String str2, String str3, String str4, String str5) {
                if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str5)) {
                    Collection notifyList = new orderScreenUtils().notifyList(HistoryOrderLeftFragment.this.list, str);
                    HistoryOrderLeftFragment.this.list.clear();
                    HistoryOrderLeftFragment.this.list.addAll(notifyList);
                    HistoryOrderLeftFragment.this.adapter.notifyDataSetChanged();
                } else {
                    HistoryOrderLeftFragment.this.mSwipeLayout.setRefreshing(true);
                    HistoryOrderLeftFragment.this.prestener.getFromNet(str2 + " " + str3, str4 + " " + str5, 1);
                }
                HistoryOrderLeftFragment.this.setSelectIndex();
            }
        });
    }

    public void setSelectIndex() {
        int index = getIndex(this.selectOrderinfo);
        if (index != -1) {
            this.adapter.setSelect(index);
            this.listView.setSelection(this.seTndex);
        } else if (this.selectIndex >= this.list.size()) {
            this.adapter.setSelect(this.list.size() - 1);
            this.listView.setSelection(this.seTndex);
        } else {
            this.adapter.setSelect(this.selectIndex);
            this.listView.setSelection(this.seTndex);
        }
    }

    public int getIndex(OrderInfo orderInfo) {
        if (this.list == null || this.list.size() <= 0 || this.selectOrderinfo == null) {
            return -1;
        }
        int i = -1;
        for (int i2 = 0; i2 < this.list.size(); i2++) {
            if (((OrderInfo) this.list.get(i2)).getOrderid().equals(orderInfo.getOrderid())) {
                this.selectIndex = i2;
                i = i2;
            }
        }
        return i;
    }

    public void setOnDataTransmissionListener(OnDataTransmissionListener onDataTransmissionListener) {
        this.mListener = onDataTransmissionListener;
    }

    protected void initData() {
        initViewList();
        this.mSwipeLayout.setRefreshing(true);
        onRefresh();
        this.mSwipeLayout.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        this.mSwipeLayout.setDistanceToTriggerSync(HttpStatus.SC_OK);
        this.mSwipeLayout.setProgressBackgroundColor(R.color.White);
        this.mSwipeLayout.setSize(0);
        this.isInit = true;
    }

    public void onRefresh() {
        this.isRefresh = true;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HistoryOrderLeftFragment.this.prestener.getFromNet(null, null, 1);
            }
        }, 2000);
    }

    public void onLoad() {
        this.isRefresh = false;
        this.prestener.getFromNet(null, null, this.page);
    }

    private void initViewList() {
        changeList();
        if (this.adapter == null) {
            this.adapter = new HistoryLeftOrderAdapter(this.context, this.list);
            this.listView.setAdapter(this.adapter);
        } else {
            this.adapter.notifyDataSetChanged();
        }
        if (this.list.size() > 0 && this.list.size() > this.selectIndex) {
            if (this.mListener != null) {
                this.mListener.dataTransmission(JSON.toJSONString(this.list.get(this.selectIndex)), this.select);
            }
            this.adapter.setSelect(this.selectIndex);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_finish:
                getActivity().finish();
                return;
            case R.id.ll_shaixuan:
                this.dialogUtils.showShaiXuan(view, "history" + this.select);
                return;
            case R.id.tv_tangchi:
                this.select = 0;
                this.selectIndex = 0;
                initViewList();
                reset();
                this.tvTangchi.setBackgroundColor(this.context.getResources().getColor(R.color.colorPrimary));
                this.tvTangchi.setTextColor(this.context.getResources().getColor(R.color.white));
                ((HistoryOrderActivity) getActivity()).showRight1();
                return;
            case R.id.tv_waimai:
                this.select = 1;
                this.selectIndex = 0;
                initViewList();
                reset();
                this.tvWaiMai.setBackgroundColor(this.context.getResources().getColor(R.color.colorPrimary));
                this.tvWaiMai.setTextColor(this.context.getResources().getColor(R.color.white));
                ((HistoryOrderActivity) getActivity()).showRight2();
                return;
            default:
                return;
        }
    }

    private void reset() {
        this.tvTangchi.setTextColor(this.context.getResources().getColor(R.color.black));
        this.tvWaiMai.setTextColor(this.context.getResources().getColor(R.color.black));
        this.tvTangchi.setBackgroundColor(this.context.getResources().getColor(R.color.gray));
        this.tvWaiMai.setBackgroundColor(this.context.getResources().getColor(R.color.gray));
    }

    public void onChange() {
        this.mSwipeLayout.setRefreshing(true);
        onRefresh();
        this.adapter.notifyDataSetChanged();
        setSelectIndex();
    }

    public void onFial(String str) {
        if (this.mSwipeLayout != null) {
            this.mSwipeLayout.setRefreshing(false);
        }
    }

    public void change() {
        if (this.isInit) {
            this.mSwipeLayout.setRefreshing(true);
            onRefresh();
            this.adapter.notifyDataSetChanged();
            setSelectIndex();
        }
    }

    public void onSuccess(List<OrderInfo> list) {
        if (this.mSwipeLayout != null) {
            this.mSwipeLayout.setRefreshing(false);
        }
        this.listTmp.clear();
        showOrderList(list);
        setSelectIndex();
    }

    public void showOrderList(List<OrderInfo> list) {
        this.listTmp.addAll(list);
        spliteList(this.listTmp);
        initViewList();
    }

    public void inform(String str) {
    }

    private void spliteList(List<OrderInfo> list) {
        this.listTangchi.clear();
        this.listWaimai.clear();
        for (int i = 0; i < list.size(); i++) {
            OrderInfo orderInfo = (OrderInfo) list.get(i);
            if ("tangchi".equals(orderInfo.getOrderfrom()) || "wxtangchi".equals(orderInfo.getOrderfrom())) {
                this.listTangchi.add(orderInfo);
            } else if ("wxwaimai".equals(orderInfo.getOrderfrom()) || "meituan".equals(orderInfo.getOrderfrom()) || "eleme".equals(orderInfo.getOrderfrom()) || "baidu".equals(orderInfo.getOrderfrom())) {
                this.listWaimai.add(orderInfo);
            }
        }
    }

    private void changeList() {
        this.list.clear();
        if (this.select == 0) {
            this.list.addAll(this.listTangchi);
        } else {
            this.list.addAll(this.listWaimai);
        }
    }
}
