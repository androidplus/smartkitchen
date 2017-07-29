package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.AddToActivity;
import com.smart.kitchen.smartkitchen.adapters.AddToGoodsAdapter;
import com.smart.kitchen.smartkitchen.adapters.AddToGoodsAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.mvp.presenter.GoodsPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.GoodsView;
import java.util.ArrayList;
import java.util.List;

public class AddToGoodsFragment extends BaseFragment implements OnItemClickListener, GoodsView {
    private static final String TAG = "AddToGoodsFragment";
    private AddToGoodsAdapter adapter;
    private GridView gv_goods;
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
        this.gv_goods = (GridView) view.findViewById(R.id.gv_goods);
    }

    protected void initEvent() {
        this.prestener = new GoodsPrestener(this.context, this);
        this.list = new ArrayList();
    }

    protected void initData() {
        this.menuId = ((FoodType) AddToActivity.listTmp.get(this.indexPage)).getId().longValue();
        this.adapter = new AddToGoodsAdapter(this.context, this.list, this.indexPage);
        this.adapter.setListener(this);
        this.gv_goods.setAdapter(this.adapter);
        this.prestener.getGoodsListFromDB(this.menuId);
        this.prestener.getGoodsListFromNET(this.menuId);
    }

    public void change() {
        this.adapter.notifyDataSetChanged();
    }

    public void onItemClick(int i) {
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
        this.adapter.notifyDataSetChanged();
    }
}
