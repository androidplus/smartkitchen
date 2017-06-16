package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.MenuAdapter;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.LeftMenu;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TestEneitys;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.fragments.AddToFragment;
import com.smart.kitchen.smartkitchen.fragments.AddToFragment.isOk;
import com.smart.kitchen.smartkitchen.fragments.AddToGoodsFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.MainPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.MainView;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import com.smart.kitchen.smartkitchen.view.MyGridView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddToActivity extends BaseFragmentActivity implements OnClickListener, MainView {
    public static final int MAX_ONE_PAGE = 10;
    private static final String TAG = "AddToActivity";
    public static AddToActivity addToActivity;
    public static int indexof = 0;
    public static List<FoodType> listTmp;
    public static List<OrderGoods> shoppingAll = new ArrayList();
    public static List<OrderGoods> shoppingAllType = new ArrayList();
    public static List<OrderGoods> shoppingCarMap = new ArrayList();
    public static List<String> shoppingCarMap_Key = new ArrayList();
    private final int CHANGE_GOODS = 1;
    private final int CHANGE_SHOP_CAR = 0;
    private MenuAdapter adapter;
    private AddToFragment addToFragment;
    private int currentIndex = 1;
    private List<AddToGoodsFragment> fragmentList;
    private MyGridView gridView;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    AddToActivity.this.addToFragment.change();
                    return;
                case 1:
                    ((AddToGoodsFragment) message.obj).change();
                    return;
                default:
                    return;
            }
        }
    };
    private ImageView ivMenuController;
    private LinearLayout ivMsg;
    private List<LeftMenu> leftMenus = TestEneitys.getLeftMenuData();
    private List<Fragment> leftMenusFragmentList;
    private List<FoodType> list;
    private LinearLayout llMenuRight;
    private RelativeLayout llSearch;
    private LinearLayout llTopRight;
    private LinearLayout menuController;
    private TextView msgCount;
    private OrderInfo orderInfo;
    private MainPresenter presenter;
    private FragmentManager selectFM = getSupportFragmentManager();
    private int totalPage = 0;
    private TextView tvBefore;
    private TextView tvNext;
    private VipInfo vipinfo;

    public static void addTo(OrderGoods orderGoods) {
        int i;
        OrderGoods orderGoods2;
        Object obj = null;
        if (shoppingCarMap.size() > 0) {
            i = 0;
            while (i < shoppingCarMap.size()) {
                if (orderGoods.getGoods().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoods().getId()) && orderGoods.getGoodsize().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoodsize().getId())) {
                    if (orderGoods.getGoodtaste() != null && ((OrderGoods) shoppingCarMap.get(i)).getGoodtaste() == null) {
                        return;
                    }
                    if (orderGoods.getGoodtaste() != null || ((OrderGoods) shoppingCarMap.get(i)).getGoodtaste() == null) {
                        if (orderGoods.getGoodtaste() == null && ((OrderGoods) shoppingCarMap.get(i)).getGoodtaste() == null) {
                            orderGoods2 = (OrderGoods) shoppingCarMap.get(i);
                            obj = 1;
                            break;
                        } else if (orderGoods.getGoodtaste().getId().equals(((OrderGoods) shoppingCarMap.get(i)).getGoodtaste().getId())) {
                            orderGoods2 = (OrderGoods) shoppingCarMap.get(i);
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
        removeShoppingAll();
        shoppingAll.addAll(shoppingCarMap);
        addToActivity.change();
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
        removeShoppingAll();
        shoppingAll.addAll(shoppingCarMap);
        addToActivity.change();
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
        removeShoppingAll();
        shoppingAll.addAll(shoppingCarMap);
        addToActivity.change();
    }

    public static void clear() {
        shoppingCarMap_Key.clear();
        shoppingCarMap.clear();
        shoppingAll.clear();
        shoppingAllType.clear();
    }

    public static void removeShoppingAll() {
        shoppingAll.clear();
        shoppingAll.addAll(shoppingAllType);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.appendfood);
        addToActivity = this;
        FinishActivity.add(this);
        this.addToFragment.setIsOk(new isOk() {
            public void isOk() {
                if (AddToActivity.shoppingCarMap.size() == 0) {
                    Toasts.show(AddToActivity.this.context, "请先添加菜品");
                } else {
                    AddToActivity.this.presenter.submitIndent(AddToActivity.this.orderInfo.getOrderid(), JSON.toJSONString(AddToActivity.shoppingCarMap));
                }
            }
        });
    }

    protected void initView() {
        this.gridView = (MyGridView) findViewById(R.id.gridView);
        this.tvBefore = (TextView) findViewById(R.id.tv_before);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.llMenuRight = (LinearLayout) findViewById(R.id.ll_menu_right);
        this.menuController = (LinearLayout) findViewById(R.id.menu_controller);
        this.llSearch = (RelativeLayout) findViewById(R.id.ll_search);
        this.llTopRight = (LinearLayout) findViewById(R.id.ll_top_right);
        this.msgCount = (TextView) findViewById(R.id.msg_count);
        this.ivMsg = (LinearLayout) findViewById(R.id.iv_msg);
        this.ivMenuController = (ImageView) findViewById(R.id.iv_menu_controller);
        this.ivMsg.setOnClickListener(this);
        this.tvBefore.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.ivMenuController.setImageResource(R.mipmap.back);
    }

    protected void initEvent() {
        this.presenter = new MainPresenter(this, this, this.progressDialog);
        this.llSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddToActivity.this.startActivity(new Intent(AddToActivity.this.context, SearchActivity.class));
            }
        });
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                AddToActivity.this.adapter.setSelectFoodType(i);
                AddToActivity.this.setSelect(((AddToActivity.this.currentIndex - 1) * 10) + i);
            }
        });
        this.menuController.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddToActivity.clear();
                AddToActivity.this.finish();
            }
        });
    }

    protected void initData() {
        this.leftMenusFragmentList = new ArrayList();
        for (int i = 0; i < this.leftMenus.size(); i++) {
            this.leftMenusFragmentList.add(null);
        }
        this.presenter.getGoodsListFromDB();
        this.presenter.getGoodsListFromNET();
        receiverMessages(null);
        initShopData();
        this.orderInfo = (OrderInfo) getIntent().getSerializableExtra("ORD");
        shoppingAll.clear();
        shoppingAllType.clear();
        List<OrderGoods> list = (List) JSON.parseObject(this.orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (((OrderGoods) list.get(i2)).getStatus() != 4) {
                shoppingAll.add(list.get(i2));
                shoppingAllType.add(list.get(i2));
            }
        }
        indexof = shoppingAll.size();
        change();
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
        this.addToFragment = new AddToFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.main_frag_right, this.addToFragment);
        FragmentTransaction beginTransaction = this.selectFM.beginTransaction();
        beginTransaction.add(R.id.main_frag_right, this.addToFragment, "AddToFragment");
        beginTransaction.commit();
    }

    private void setSelect(int i) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragmentList.get(i) == null) {
            LogUtils.e(TAG, "setSelect:is null " + i);
            this.fragmentList.set(i, new AddToGoodsFragment());
            ((AddToGoodsFragment) this.fragmentList.get(i)).setIndexPage(i);
            beginTransaction.add((int) R.id.main_frag, (Fragment) this.fragmentList.get(i));
        } else {
            LogUtils.e(TAG, "setSelect:not null " + i);
            beginTransaction.show((Fragment) this.fragmentList.get(i));
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
        int i = 0;
        for (int i2 = 0; i2 < this.leftMenus.size(); i2++) {
            if (this.leftMenusFragmentList.get(i2) != null) {
                fragmentTransaction.hide((Fragment) this.leftMenusFragmentList.get(i2));
            }
        }
        while (i < this.fragmentList.size()) {
            if (this.fragmentList.get(i) != null) {
                fragmentTransaction.hide((Fragment) this.fragmentList.get(i));
            }
            i++;
        }
    }

    public void notifyGoodsChange() {
        for (int i = 0; i < this.fragmentList.size(); i++) {
            AddToGoodsFragment addToGoodsFragment = (AddToGoodsFragment) this.fragmentList.get(i);
            if (addToGoodsFragment != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = addToGoodsFragment;
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
        if (this.addToFragment != null) {
            this.handler.sendEmptyMessage(0);
            if (this.addToFragment != null) {
                this.handler.sendEmptyMessage(0);
            }
        }
    }

    public void onSuccess(List<FoodType> list) {
        ShowFoodType(list);
    }

    public void onFail() {
    }

    public void ShowFoodType(List<FoodType> list) {
        if (listTmp == null) {
            listTmp = new ArrayList();
        }
        listTmp.clear();
        listTmp.addAll(list);
        this.adapter = null;
        initFoodType();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    protected void onResume() {
        super.onResume();
        this.vipinfo = (VipInfo) getIntent().getSerializableExtra("vip");
        if (this.vipinfo == null) {
        }
    }

    public void receiverMessages(String str) {
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            clear();
            finish();
        }
        return true;
    }

    public void isSubmitIndent(String str, String str2) {
        if ("onSuccess".equals(str)) {
            finish();
            clear();
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "未追加成功,请重新追加");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "未追加成功,请重新追加");
        }
    }
}
