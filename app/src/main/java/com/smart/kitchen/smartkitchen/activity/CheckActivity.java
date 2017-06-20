package com.smart.kitchen.smartkitchen.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.MenuAdapter;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.fragments.CheckFragment;
import com.smart.kitchen.smartkitchen.fragments.CheckFragment.onNotityGoods;
import com.smart.kitchen.smartkitchen.fragments.CheckGoodsFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.CheckPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.CheckView;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.view.MyGridView;
import java.util.ArrayList;
import java.util.List;

public class CheckActivity extends BaseFragmentActivity implements OnClickListener, CheckView {
    public static final int MAX_ONE_PAGE = 10;
    private static final String TAG = "CheckActivity";
    public static CheckActivity checkActivity;
    public static List<Goods> checkInfoMap = new ArrayList<>();
    public static List<String> checkInfoMap_Key = new ArrayList<>();
    public static List<FoodType> listTmp;
    private final int CHANGE_GOODS = 0;
    private final int CHECK_GOODS = 1;
    private MenuAdapter adapter;
    private CheckFragment checkFragment;
    private int currentIndex = 1;
    private List<CheckGoodsFragment> fragmentList;
    private CheckGoodsFragment goodsFragment;
    private MyGridView gridView;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    ((CheckGoodsFragment) message.obj).change();
                    return;
                case 1:
                    CheckActivity.this.checkFragment.addData();
                    return;
                default:
                    return;
            }
        }
    };
    private LinearLayout ivMsg;
    private List<FoodType> list;
    private LinearLayout llMenuRight;
    private RelativeLayout llSearch;
    private LinearLayout llTopRight;
    private LinearLayout menuCheckBack;
    private TextView msgCount;
    private CheckPresenter presenter;
    private int totalPage = 0;
    private TextView tvBefore;
    private TextView tvNext;

    public static void add(String str, Goods goods) {
        int indexOf = checkInfoMap_Key.indexOf(str);
        if (indexOf != -1) {
            checkInfoMap.set(indexOf, goods);
        } else {
            checkInfoMap_Key.add(str);
            checkInfoMap.add(goods);
        }
        checkActivity.change();
    }

    public static Goods get(String str) {
        if (checkInfoMap_Key.size() == 0) {
            return null;
        }
        int indexOf = checkInfoMap_Key.indexOf(str);
        if (indexOf != -1) {
            return (Goods) checkInfoMap.get(indexOf);
        }
        return null;
    }

    public static void remove(String str) {
        if (checkInfoMap_Key.size() != 0) {
            int indexOf = checkInfoMap_Key.indexOf(str);
            if (indexOf != -1) {
                checkInfoMap_Key.remove(indexOf);
                checkInfoMap.remove(indexOf);
            }
            checkActivity.change();
        }
    }

    public static void clear() {
        checkInfoMap_Key.clear();
        checkInfoMap.clear();
        checkActivity.change();
    }

    public void change() {
        notifyCheckInfoChange();
        notifyGoodsInfoChange();
    }

    public void notifyCheckInfoChange() {
        LogUtils.e(TAG, "notifyShopCarChange: ");
        if (this.checkFragment != null) {
            this.handler.sendEmptyMessage(1);
        }
    }

    public void notifyGoodsInfoChange() {
        for (int i = 0; i < this.fragmentList.size(); i++) {
            CheckGoodsFragment checkGoodsFragment = (CheckGoodsFragment) this.fragmentList.get(i);
            if (checkGoodsFragment != null) {
                Message message = new Message();
                message.what = 0;
                message.obj = checkGoodsFragment;
                this.handler.sendMessage(message);
                LogUtils.e(TAG, "notifyGoodsChange: ");
            }
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_check);
        checkActivity = this;
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.gridView = (MyGridView) findViewById(R.id.gridView);
        this.tvBefore = (TextView) findViewById(R.id.tv_before);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.llMenuRight = (LinearLayout) findViewById(R.id.ll_menu_right);
        this.menuCheckBack = (LinearLayout) findViewById(R.id.menu_check_back);
        this.llSearch = (RelativeLayout) findViewById(R.id.ll_search);
        this.llTopRight = (LinearLayout) findViewById(R.id.ll_top_right);
        this.msgCount = (TextView) findViewById(R.id.msg_count);
        this.ivMsg = (LinearLayout) findViewById(R.id.iv_msg);
        this.llTopRight.setVisibility(4);
        this.tvBefore.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.menuCheckBack.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        this.presenter = new CheckPresenter(this, this);
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                CheckActivity.this.adapter.setSelectFoodType(i);
                CheckActivity.this.setSelect(((CheckActivity.this.currentIndex - 1) * 10) + i);
            }
        });
    }

    @Override
    protected void initData() {
        this.presenter.getGoodsListFromDB();
        this.presenter.getGoodsListFromNET();
        initCheckInfoData();
        this.checkFragment.setOnNotityGoods(new onNotityGoods() {
            public void notity() {
                CheckActivity.this.goodsFragment.notifyGoods();
            }
        });
    }

    private void initCheckInfoData() {
        this.checkFragment = new CheckFragment();
        getSupportFragmentManager().beginTransaction().add((int) R.id.main_frag_right, this.checkFragment);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.main_frag_right, this.checkFragment, "CheckFragment");
        beginTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_check_back:
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

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < this.fragmentList.size(); i++) {
            if (this.fragmentList.get(i) != null) {
                fragmentTransaction.hide((Fragment) this.fragmentList.get(i));
            }
        }
    }

    private void setSelect(int i) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragmentList.get(i) == null) {
            LogUtils.e(TAG, "setSelect:is null " + i);
            this.fragmentList.set(i, new CheckGoodsFragment());
            ((CheckGoodsFragment) this.fragmentList.get(i)).setIndexPage(i);
            beginTransaction.add((int) R.id.main_frag, (Fragment) this.fragmentList.get(i));
            this.goodsFragment = (CheckGoodsFragment) this.fragmentList.get(i);
        } else {
            LogUtils.e(TAG, "setSelect:not null " + i);
            beginTransaction.show((Fragment) this.fragmentList.get(i));
            this.goodsFragment = (CheckGoodsFragment) this.fragmentList.get(i);
        }
        try {
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
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
}
