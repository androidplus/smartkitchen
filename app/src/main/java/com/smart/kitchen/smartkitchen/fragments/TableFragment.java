package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.TableActivity;
import com.smart.kitchen.smartkitchen.adapters.TableAdapter;
import com.smart.kitchen.smartkitchen.adapters.TableAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.presenter.TableFragmentPrestener;
import com.smart.kitchen.smartkitchen.mvp.view.TableFragmentView;
import java.util.ArrayList;
import java.util.List;

public class TableFragment extends BaseFragment implements OnItemClickListener, TableFragmentView {
    private static final String TAG = "GoodsFragment";
    private TableAdapter adapter;
    private GridView gvGoods;
    private long id;
    private int indexPage;
    private List<TableNumber> list;
    private TableFragmentPrestener prestener;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_table, null);
    }

    public void setIndexPage(int i) {
        this.indexPage = i;
    }

    protected void initView(View view) {
        this.gvGoods = (GridView) view.findViewById(R.id.gv_goods);
    }

    protected void initEvent() {
        this.prestener = new TableFragmentPrestener(this.context, this);
        this.list = new ArrayList();
    }

    protected void initData() {
        this.id = ((TableArea) TableActivity.listTmp.get(this.indexPage)).getId().longValue();
        this.adapter = new TableAdapter(this.context, this.list, this.indexPage);
        this.gvGoods.setAdapter(this.adapter);
        this.prestener.getTableFragmentListFromDB();
        this.prestener.getTableFragmentListFromNET(this.id);
    }

    public void onItemClick(int i) {
    }

    public void onSuccess(List<TableNumber> list) {
        ShowTableNumber(list);
    }

    public void onFail() {
    }

    public long getTableAreaId() {
        return this.id;
    }

    public void ShowTableNumber(List<TableNumber> list) {
        this.list.clear();
        this.list.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    public void OnChange() {
        if (this.adapter != null) {
            this.adapter.SetClear();
            this.adapter.notifyDataSetChanged();
        }
    }

    public void change() {
        this.adapter.notifyDataSetChanged();
    }
}
