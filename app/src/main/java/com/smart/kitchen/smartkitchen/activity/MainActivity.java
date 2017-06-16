package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.MenuAdapter;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.LeftMenu;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TestEneitys;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.fragments.GoodsFragment;
import com.smart.kitchen.smartkitchen.fragments.RepeatFragment;
import com.smart.kitchen.smartkitchen.fragments.SalesReturnFragment;
import com.smart.kitchen.smartkitchen.fragments.ShopFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.MainPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.MainView;
import com.smart.kitchen.smartkitchen.popwindow.LeftMenuPop;
import com.smart.kitchen.smartkitchen.popwindow.LeftMenuPop.OnItemClickListener;
import com.smart.kitchen.smartkitchen.service.ClientSocketService;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.ObjectEquals;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import com.smart.kitchen.smartkitchen.view.MyGridView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements OnClickListener, MainView, OnItemClickListener {
    public static final int MAX_ONE_PAGE = 10;
    private static final String TAG = "MainActivity";
    public static List<FoodType> listTmp;
    public static MainActivity mainActivity;
    public static int selectTAG = 0;
    public static List<OrderGoods> shoppingCarMap = new ArrayList();
    public static List<String> shoppingCarMap_Key = new ArrayList();
    private final int CHANGE_GOODS = 1;
    private final int CHANGE_SALES_RETURN = 2;
    private final int CHANGE_SHOP_CAR = 0;
    private final int SEARCH_CODE = 122;
    private MenuAdapter adapter;
    private int currentIndex = 1;
    private int flag = 0;
    private List<GoodsFragment> fragmentList;
    private MyGridView gridView;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    MainActivity.this.shopFragment.change();
                    return;
                case 1:
                    ((GoodsFragment) message.obj).change();
                    return;
                case 2:
                    MainActivity.this.salesReturnFragment.change();
                    return;
                default:
                    return;
            }
        }
    };
    private LinearLayout ivMsg;
    private List<LeftMenu> leftMenus = TestEneitys.getLeftMenuData();
    private List<Fragment> leftMenusFragmentList;
    private List<FoodType> list;
    private List<TableArea> listTableArea;
    private LinearLayout llMenuRight;
    private RelativeLayout llSearch;
    private LinearLayout llTopRight;
    private boolean menuBoolean = false;
    private LinearLayout menuController;
    private TextView msgCount;
    private LeftMenuPop popWindowUtils;
    private MainPresenter presenter;
    private SalesReturnFragment salesReturnFragment;
    private FragmentManager selectFM = getSupportFragmentManager();
    private ShopFragment shopFragment;
    private int totalPage = 0;
    private TextView tvBefore;
    private TextView tvNext;
    private Boolean vipBoolean = Boolean.valueOf(false);

    public static void addTo(OrderGoods orderGoods) {
        ObjectEquals.addOrder(shoppingCarMap, orderGoods);
        mainActivity.change();
    }

    public static int getGoodsCount(String str) {
        return ObjectEquals.getGoodsCount(shoppingCarMap, str);
    }

    public static void removeGoodsCount(OrderGoods orderGoods) {
        ObjectEquals.removeGoodsCount(orderGoods);
        mainActivity.change();
    }

    public static boolean getGoods(String str) {
        if (shoppingCarMap_Key.size() == 0 || shoppingCarMap_Key.indexOf(str) == -1) {
            return false;
        }
        return true;
    }

    public static void removeGoods(OrderGoods orderGoods) {
        ObjectEquals.removeGoods(orderGoods);
        mainActivity.change();
    }

    public static void clear() {
        shoppingCarMap_Key.clear();
        shoppingCarMap.clear();
        mainActivity.change();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        FinishActivity.add(this);
        LogUtils.e(TAG, "onCreate: " + new UserInfoDaoManager().getNowUserInfo().toString());
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
        this.ivMsg.setOnClickListener(this);
        this.tvBefore.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.menuController.setOnClickListener(this);
        this.presenter = new MainPresenter(this, this, this.progressDialog);
        if (TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA))) {
            this.presenter.getAreaListFromNET();
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z && this.menuBoolean && this.flag == 0) {
            this.dialogUtils.showArea(this.menuController, this.listTableArea);
            this.flag = 1;
        }
    }

    protected void initEvent() {
        this.llSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivityForResult(new Intent(MainActivity.this.context, SearchActivity.class), 122);
            }
        });
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                MainActivity.this.adapter.setSelectFoodType(i);
                MainActivity.this.setSelect(((MainActivity.this.currentIndex - 1) * 10) + i);
            }
        });
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 122 && i2 == -1) {
            if (selectTAG != 0) {
                setSelect(0);
                clear();
                select1();
                this.adapter.setSelectFoodType(0);
                selectTAG = 0;
            }
            this.presenter.search(((VipInfo) intent.getSerializableExtra("vipinfo")).getPhone());
        }
    }

    protected void initData() {
        this.popWindowUtils = new LeftMenuPop(this.context);
        this.leftMenusFragmentList = new ArrayList();
        for (int i = 0; i < this.leftMenus.size(); i++) {
            this.leftMenusFragmentList.add(null);
        }
        this.presenter.getGoodsListFromDB();
        this.presenter.getGoodsListFromNET();
        receiverMessages(null);
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
            this.llMenuRight.setVisibility(0);
        }
        addData();
        if (listTmp.size() > 0) {
            setSelect(0);
        }
    }

    private void addData() {
        if (this.currentIndex <= 1) {
            this.currentIndex = 1;
            this.tvBefore.setVisibility(4);
            this.tvNext.setVisibility(0);
        }
        if (this.currentIndex >= this.totalPage) {
            this.currentIndex = this.totalPage;
        }
        if (this.currentIndex > 1 && this.currentIndex < this.totalPage) {
            this.tvBefore.setVisibility(0);
            this.tvNext.setVisibility(0);
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
                this.tvBefore.setVisibility(8);
                this.tvNext.setVisibility(8);
            } else {
                this.tvBefore.setVisibility(0);
                this.tvNext.setVisibility(8);
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
                this.popWindowUtils.leftMenuPopWindow(this.leftMenus, view, this.llTopRight, this);
                return;
            case R.id.tv_before:
                this.currentIndex--;
                addData();
                return;
            case R.id.tv_next:
                this.currentIndex++;
                addData();
                return;
            case R.id.iv_msg:
                View view2 = view;
                this.dialogUtils.showMSG(view2, this.llTopRight, this.presenter.getMessaggeListNOReadFromDB(), this.presenter.getMessaggeListReadedFromDB(), new OnClickListener() {
                    public void onClick(View view) {
                        MainActivity.this.receiverMessages(null);
                    }
                });
                return;
            default:
                return;
        }
    }

    public void onItemClick(int i) {
        String tag = ((LeftMenu) this.leftMenus.get(i)).getTag();
        boolean z = true;
        switch (tag.hashCode()) {
            case -1846485744:
                if (tag.equals("writeOff")) {
                    z = true;
                    break;
                }
                break;
            case -1572315523:
                if (tag.equals("back_foods")) {
                    z = true;
                    break;
                }
                break;
            case -1097452776:
                if (tag.equals("locker")) {
                    z = true;
                    break;
                }
                break;
            case -934531685:
                if (tag.equals("repeat")) {
                    z = true;
                    break;
                }
                break;
            case -699592733:
                if (tag.equals("history_order")) {
                    z = true;
                    break;
                }
                break;
            case 3208383:
                if (tag.equals("hold")) {
                    z = true;
                    break;
                }
                break;
            case 111578632:
                if (tag.equals("users")) {
                    z = true;
                    break;
                }
                break;
            case 465628774:
                if (tag.equals("distory_foods")) {
                    z = true;
                    break;
                }
                break;
            case 1141029527:
                if (tag.equals("get_money")) {
                    z = false;
                    break;
                }
                break;
            case 1142956325:
                if (tag.equals("get_order")) {
                    z = true;
                    break;
                }
                break;
            case 1985941072:
                if (tag.equals("setting")) {
                    z = true;
                    break;
                }
                break;
        }
        int x = z?1:0;
        switch (x) {
            case 0:
                setSelect(0);
                if (selectTAG != 0) {
                    clear();
                    select1();
                    this.adapter.setSelectFoodType(0);
                    selectTAG = 0;
                    return;
                }
                return;
            case 1:
                if (getArea()) {
                    Toasts.show(this.context, "请先至设置界面选择区域");
                    return;
                } else {
                    startActivity(new Intent(this, OrderActivity.class));
                    return;
                }
//            case true:
//                startActivity(new Intent(this, HistoryOrderActivity.class));
//                return;
//            case true:
//                if (AuthorityUtil.getInstance().getRoleFlag() == 2) {
//                    setSelect(0);
//                    if (selectTAG != 1) {
//                        clear();
//                        select();
//                        this.adapter.setSelectFoodType(0);
//                        selectTAG = 1;
//                        return;
//                    }
//                    return;
//                }
//                Toasts.show(this.context, getResources().getString(R.string.no_authority));
//                return;
//            case true:
//                if (AuthorityUtil.getInstance().permitBreakage() || AuthorityUtil.getInstance().getRoleFlag() == 2) {
//                    startActivity(new Intent(this, BreakageActivity.class));
//                    return;
//                } else {
//                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
//                    return;
//                }
//            case true:
//                if (!AuthorityUtil.getInstance().permitCheck()) {
//                    Toasts.show(this.context, getResources().getString(R.string.no_authority));
//                    return;
//                } else if (SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONCOUNT)) {
//                    startActivity(new Intent(this, CheckActivity.class));
//                    return;
//                } else {
//                    Toasts.show(this.context, "未开启盘点功能，前往本地设置开启");
//                    return;
//                }
//            case true:
//                startActivityForResult(new Intent(this.context, SearchActivity.class), 122);
//                return;
//            case true:
//                startActivity(new Intent(this, LocalSettingActivity.class));
//                return;
//            case true:
//                startActivity(new Intent(this.context, RepeatActivity.class));
//                return;
//            case true:
//                Intent intent = new Intent(this, LoginActivity.class);
//                intent.putExtra("is_main", true);
//                startActivity(intent);
//                return;
//            case true:
//                SPUtils.setUserinfo(this.context, SPUtils.IS_LOGIN, Boolean.valueOf(false));
//                SPUtils.remove(this.context, SPUtils.SETTING_AREA);
//                this.context.stopService(new Intent(this.context, ClientSocketService.class));
//                startActivity(new Intent(this.context, LoginActivity.class));
//                TestEneitys.leftMenus = null;
//                AuthorityUtil.setInstance();
//                finish();
//                return;
//            default:
//                return;
        }
    }

    private void initShopData() {
        this.shopFragment = new ShopFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.main_frag_right, this.shopFragment);
        this.salesReturnFragment = new SalesReturnFragment();
        FragmentTransaction beginTransaction = this.selectFM.beginTransaction();
        beginTransaction.add(R.id.main_frag_right, this.shopFragment, "ShopFragment");
        beginTransaction.commit();
    }

    private void select() {
        FragmentTransaction beginTransaction = this.selectFM.beginTransaction();
        if (this.salesReturnFragment.isAdded()) {
            beginTransaction.hide(this.shopFragment).show(this.salesReturnFragment);
        } else {
            beginTransaction.hide(this.shopFragment).add((int) R.id.main_frag_right, this.salesReturnFragment);
        }
        beginTransaction.commit();
    }

    private void select1() {
        FragmentTransaction beginTransaction = this.selectFM.beginTransaction();
        beginTransaction.hide(this.salesReturnFragment).show(this.shopFragment);
        beginTransaction.commit();
    }

    private void setSelect(int i) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragmentList.get(i) == null) {
            LogUtils.e(TAG, "setSelect:is null " + i);
            this.fragmentList.set(i, new GoodsFragment());
            ((GoodsFragment) this.fragmentList.get(i)).setIndexPage(i);
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

    private void setSelectLeftMenu(int i) {

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
            GoodsFragment goodsFragment = (GoodsFragment) this.fragmentList.get(i);
            if (goodsFragment != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = goodsFragment;
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
        if (this.shopFragment != null) {
            this.handler.sendEmptyMessage(0);
            if (selectTAG == 0) {
                if (this.shopFragment != null) {
                    this.handler.sendEmptyMessage(0);
                }
            } else if (selectTAG == 1 && this.salesReturnFragment != null) {
                this.handler.sendEmptyMessage(2);
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

    public void isSubmitIndent(String str, String str2) {
        if ("AreaOnSuccess".equals(str)) {
            this.menuBoolean = true;
            this.listTableArea = (List) JSON.parseObject(str2, new TypeReference<List<TableArea>>() {
            }, new Feature[0]);
        } else if ("AreaOnAlert".equals(str) || "AreaOnFailure".equals(str)) {
            Toasts.showLong(this.context, (CharSequence) "没有选择区域号,无法获取数据");
        }
        if ("SearchOnSuccess".equals(str)) {
            VipInfo vipInfo = (VipInfo) JSON.parseObject(str2, new TypeReference<VipInfo>() {
            }, new Feature[0]);
            this.vipBoolean = Boolean.valueOf(true);
            this.shopFragment.setVipInfo(vipInfo, this.vipBoolean.booleanValue());
        } else if ("SearchOnAlert".equals(str) || "SearchOnFailure".equals(str)) {
            this.vipBoolean = Boolean.valueOf(false);
            this.shopFragment.setVipInfo(null, this.vipBoolean.booleanValue());
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    protected void onResume() {
        super.onResume();
        receiverMessages(null);
        this.presenter.getGoodsListFromNET();
        if (this.vipBoolean.booleanValue()) {
            this.shopFragment.setVipInfo(null, false);
        }
    }

    public void receiverMessages(String str) {
        List messaggeListNOReadFromDB = this.presenter.getMessaggeListNOReadFromDB();
        LogUtils.e(TAG, "receiverMessages: " + messaggeListNOReadFromDB.size());
        this.msgCount.setText("0");
        if (messaggeListNOReadFromDB.size() > 0) {
            this.msgCount.setText("" + messaggeListNOReadFromDB.size());
        }
    }

    public boolean getArea() {
        return TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA));
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.popWindowUtils.popWindow != null) {
            this.popWindowUtils.popWindow.dismiss();
        }
    }
}
