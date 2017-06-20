package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.MenuAdapter;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.fragments.BreakageFragment;
import com.smart.kitchen.smartkitchen.fragments.BreakageGoodsFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.BreakagePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.BreakageView;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.view.MyGridView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BreakageActivity extends BaseFragmentActivity implements OnClickListener, BreakageView {
    public static final int MAX_ONE_PAGE = 10;
    private static final String TAG = "BreakageActivity";
    public static BreakageActivity breakageActivity;
    public static List<FoodType> listTmp;
    public static List<OrderGoods> shoppingCarMap = new ArrayList<>();
    public static List<String> shoppingCarMap_Key = new ArrayList<>();
    private final int CHANGE_GOODS = 1;
    private final int CHANGE_SHOP_CAR = 0;
    private MenuAdapter adapter;
    private BreakageFragment breakageFragment;
    private int currentIndex = 1;
    private List<BreakageGoodsFragment> fragmentList;
    private MyGridView gridView;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    BreakageActivity.this.breakageFragment.change();
                    return;
                case 1:
                    ((BreakageGoodsFragment) message.obj).change();
                    return;
                default:
                    return;
            }
        }
    };
    private LinearLayout ivMsg;
    private List<FoodType> list;
    private LinearLayout llMenuRight;
    private LinearLayout llTopRight;
    private LinearLayout menuController;
    private TextView msgCount;
    private BreakagePresenter presenter;
    private int totalPage = 0;
    private TextView tvBefore;
    private TextView tvNext;

    public static void addTo(OrderGoods orderGoods) {
        int i;
        OrderGoods orderGoods2;
        Object obj = null;
        if (shoppingCarMap.size() > 0) {
            i = 0;
            while (i < shoppingCarMap.size()) {
                if (orderGoods.getGoods().getId().equals((shoppingCarMap.get(i)).getGoods().getId())
                        && orderGoods.getGoodsize().getId().equals((shoppingCarMap.get(i)).getGoodsize().getId())) {
                    if (orderGoods.getGoodtaste() != null && (shoppingCarMap.get(i)).getGoodtaste() == null) {
                        return;
                    }
                    if (orderGoods.getGoodtaste() != null || (shoppingCarMap.get(i)).getGoodtaste() == null) {
                        if (orderGoods.getGoodtaste() == null && (shoppingCarMap.get(i)).getGoodtaste() == null) {
                            orderGoods2 = shoppingCarMap.get(i);
                            obj = 1;
                            break;
                        } else if (orderGoods.getGoodtaste().getId().equals((shoppingCarMap.get(i)).getGoodtaste().getId())) {
                            orderGoods2 = shoppingCarMap.get(i);
                            int i2 = 1;
                            break;
                        }
                    } else {
                        return;
                    }
                }
                i++;
            }
        }
        i = -1;
        orderGoods2 = null;
        if (obj != null) {
            orderGoods2.setCount(orderGoods2.getCount() + orderGoods.getCount());
            shoppingCarMap.set(i, orderGoods2);
        } else {
            shoppingCarMap_Key.add(orderGoods.getGoods().getId() + "");
            shoppingCarMap.add(orderGoods);
        }
        breakageActivity.change();
    }

    public static int getGoodsCount(String str) {
        if (shoppingCarMap.size() <= 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < shoppingCarMap.size(); i2++) {
            if (String.valueOf(((OrderGoods) shoppingCarMap.get(i2)).getGoods().getId()).equals(str)) {
                i += ((OrderGoods) shoppingCarMap.get(i2)).getCount();
            }
        }
        return i;
    }

    public static void removeGoods(OrderGoods orderGoods) {
        Iterator it = shoppingCarMap.iterator();
        int i = 0;
        while (it.hasNext()) {
            OrderGoods orderGoods2 = (OrderGoods) it.next();
            i++;
            if (orderGoods.getGoods().getId().equals(orderGoods2.getGoods().getId()) && orderGoods.getGoodsize().getId().equals(orderGoods2.getGoodsize().getId())) {
                int i2;
                if (orderGoods.getGoodtaste() != null) {
                    if (orderGoods2.getGoodtaste() == null) {
                        return;
                    }
                    if (orderGoods.getGoodtaste().getId().equals(orderGoods2.getGoodtaste().getId())) {
                        it.remove();
                        shoppingCarMap_Key.remove(i - 1);
                        i2 = i - 1;
                        break;
                    }
                } else if (orderGoods2.getGoodtaste() == null) {
                    it.remove();
                    shoppingCarMap_Key.remove(i - 1);
                    i2 = i - 1;
                } else {
                    return;
                }
            }
        }
        breakageActivity.change();
    }

    public static void removeGoodsCount(OrderGoods orderGoods) {
        if (shoppingCarMap.size() > 0) {
            int i = 0;
            while (i < shoppingCarMap.size()) {
                if (orderGoods.getGoods().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoods().getId()) && orderGoods.getGoodsize().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoodsize().getId())) {
                    if (orderGoods.getGoodtaste() != null) {
                        if (((OrderGoods) shoppingCarMap.get(i)).getGoodtaste() == null) {
                            return;
                        }
                        if (orderGoods.getGoodtaste().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoodtaste().getId())) {
                            ((OrderGoods) shoppingCarMap.get(i)).setCount(orderGoods.getCount());
                        }
                    } else if (((OrderGoods) shoppingCarMap.get(i)).getGoodtaste() == null) {
                        ((OrderGoods) shoppingCarMap.get(i)).setCount(orderGoods.getCount());
                    } else {
                        return;
                    }
                }
                i++;
            }
        }
        breakageActivity.change();
    }

    public static void clear() {
        shoppingCarMap_Key.clear();
        shoppingCarMap.clear();
        breakageActivity.change();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_breakage);
        breakageActivity = this;
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.gridView = (MyGridView) findViewById(R.id.gridView);
        this.tvBefore = (TextView) findViewById(R.id.tv_before);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.llMenuRight = (LinearLayout) findViewById(R.id.ll_menu_right);
        this.menuController = (LinearLayout) findViewById(R.id.menu_controller);
        this.llTopRight = (LinearLayout) findViewById(R.id.ll_top_right);
        this.msgCount = (TextView) findViewById(R.id.msg_count);
        this.ivMsg = (LinearLayout) findViewById(R.id.iv_msg);
        this.ivMsg.setOnClickListener(this);
        this.tvBefore.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.menuController.setOnClickListener(this);
        this.llTopRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initEvent() {
        this.presenter = new BreakagePresenter(this, this);
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BreakageActivity.this.adapter.setSelectFoodType(i);
                BreakageActivity.this.setSelect(((BreakageActivity.this.currentIndex - 1) * 10) + i);
            }
        });
    }

    @Override
    protected void initData() {
        this.presenter.getGoodsListFromDB();
        this.presenter.getGoodsListFromNET();
        initShopData();
    }

    private void initFoodType() {
        int i;
        this.fragmentList = new ArrayList();
        for (i = 0; i < listTmp.size(); i++) {
            this.fragmentList.add(null);
        }
        if (listTmp.size() / 10 == 0) {
            i = 1;
        } else if (listTmp.size() % 10 == 0) {
            i = listTmp.size() / 10;
        } else {
            i = (listTmp.size() / 10) + 1;
        }
        this.totalPage = i;
        if (listTmp.size() > 10) {
            this.llMenuRight.setVisibility(View.VISIBLE);
        }
        addData();
        if (listTmp.size() > 0) {
            setSelect(0);
        }
    }

    private void addData() {
        if (this.currentIndex <= 1) {
            this.currentIndex = 1;
            this.tvBefore.setVisibility(View.INVISIBLE);
            this.tvNext.setVisibility(View.VISIBLE);
        }
        if (this.currentIndex >= this.totalPage) {
            this.currentIndex = this.totalPage;
        }
        if (this.currentIndex > 1 && this.currentIndex < this.totalPage) {
            this.tvBefore.setVisibility(View.VISIBLE);
            this.tvNext.setVisibility(View.VISIBLE);
        }
        if (this.list == null) {
            this.list = new ArrayList();
        }
        this.list.clear();
        int i = (this.currentIndex - 1) * 10;
        int i2 = this.currentIndex * 10;
        if (i2 > listTmp.size()) {
            i2 = listTmp.size();
        }
        while (i < i2) {
            this.list.add(listTmp.get(i));
            i++;
        }
        if (this.list.size() <= 5) {
            if (listTmp.size() <= 10) {
                this.tvBefore.setVisibility(View.GONE);
                this.tvNext.setVisibility(View.GONE);
            } else {
                this.tvBefore.setVisibility(View.VISIBLE);
                this.tvNext.setVisibility(View.GONE);
            }
            this.gridView.setNumColumns(this.list.size());
        } else {
            this.gridView.setNumColumns(5);
        }
        if (this.adapter == null) {
            this.adapter = new MenuAdapter(this.context, this.list);
            this.gridView.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_controller:
                finish();
                return;
            case R.id.tv_before:
                this.currentIndex--;
                addData();
                return;
            case R.id.tv_next:
                this.currentIndex++;
                addData();
                return;
            default:
                return;
        }
    }

    private void initShopData() {
        this.breakageFragment = new BreakageFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.main_frag_right, this.breakageFragment, "salesReturnFragment");
        beginTransaction.commit();
    }

    private void setSelect(int selectedIndex) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (fragmentList.get(selectedIndex) == null) {
            LogUtils.e(TAG, "setSelect:is null " + selectedIndex);
            fragmentList.set(selectedIndex, new BreakageGoodsFragment());
             fragmentList.get(selectedIndex).setIndexPage(selectedIndex);
            beginTransaction.add( R.id.main_frag, this.fragmentList.get(selectedIndex));
        } else {
            LogUtils.e(TAG, "setSelect:not null " + selectedIndex);
            beginTransaction.show(fragmentList.get(selectedIndex));
        }
        try {
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fm() {
        setSelect(0);
    }

    private FragmentTransaction getFragmentTransaction() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        return beginTransaction;
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i) != null) {
                fragmentTransaction.hide( fragmentList.get(i));
            }
        }
    }

    public void notifyGoodsChange() {
        for (int i = 0; i < this.fragmentList.size(); i++) {
            BreakageGoodsFragment breakageGoodsFragment = fragmentList.get(i);
            if (breakageGoodsFragment != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = breakageGoodsFragment;
                this.handler.sendMessage(message);
                LogUtils.e(TAG, "notifyGoodsChange: ");
            }
        }
    }

    public void change() {
        notifyGoodsChange();
        notifyShopCarChange();
    }

    public void notifyShopCarChange() {
        LogUtils.e(TAG, "notifyShopCarChange: ");
        if (this.breakageFragment != null) {
            this.handler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onSuccess(List<FoodType> list) {
        ShowFoodType(list);
    }

    @Override
    public void onFail() {
    }

    @Override
    public void ShowFoodType(List<FoodType> list) {
        if (listTmp == null) {
            listTmp = new ArrayList();
        }
        listTmp.clear();
        listTmp.addAll(list);
        this.adapter = null;
        initFoodType();
    }

    @Override
    public void inFrom(String str) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
