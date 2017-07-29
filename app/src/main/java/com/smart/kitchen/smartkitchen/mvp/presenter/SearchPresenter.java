package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.SearchModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.SearchView;

public class SearchPresenter implements OnResultListener {
    private Context context;
    private SearchModelImpl searchModel = new SearchModelImpl();
    private SearchView searchView;

    public SearchPresenter(SearchView searchView, Context context) {
        this.searchView = searchView;
        this.context = context;
    }

    public void search(String str) {
        this.searchModel.search(0, this.context, str, this);
    }

    public void onSuccess(int i, String str) {
        this.searchView.onSuccess(this.searchModel.asyncJson(str));
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
        this.searchView.onFial(str);
    }
}
