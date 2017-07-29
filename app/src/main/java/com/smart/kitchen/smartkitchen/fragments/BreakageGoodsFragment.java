package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.BreakageActivity;
import com.smart.kitchen.smartkitchen.adapters.GoodsBreakageAdapter;
import com.smart.kitchen.smartkitchen.adapters.GoodsBreakageAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.mvp.presenter.GoodsPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.GoodsView;
import java.util.ArrayList;
import java.util.List;

public class BreakageGoodsFragment extends BaseFragment implements OnItemClickListener, GoodsView {
    private GoodsBreakageAdapter goodsCheckAdapter;
    private GridView gvGoods;
    private int indexPage;
    private List<Goods> list;
    private long menuId;
    private GoodsPrestener prestener;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_goods, null);
    }

    public void setIndexPage(int i) {
        this.indexPage = i;
    }

    protected void initView(View view) {
        this.gvGoods = (GridView) view.findViewById(R.id.gv_goods);
    }

    protected void initEvent() {
        this.prestener = new GoodsPrestener(this.context, this);
        this.list = new ArrayList();
    }

    protected void initData() {
        this.menuId = ((FoodType) BreakageActivity.listTmp.get(this.indexPage)).getId().longValue();
        this.goodsCheckAdapter = new GoodsBreakageAdapter(this.context, this.list, this.indexPage);
        this.goodsCheckAdapter.setListener(this);
        this.gvGoods.setAdapter(this.goodsCheckAdapter);
        this.prestener.getGoodsListFromNET(this.menuId);
    }

    public void change() {
        this.goodsCheckAdapter.notifyDataSetChanged();
    }

    public void onSuccess(List<Goods> list) {
        ShowGoods(list);
    }

    public void onFail() {
    }

    public long getMenuTypeId() {
        return this.menuId;
    }

    public void ShowGoods(List<Goods> list) {
        this.list.clear();
        this.list.addAll(list);
        this.goodsCheckAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
        BreakageActivity.clear();
    }

    public void onItemClick(int i) {
    }
}
