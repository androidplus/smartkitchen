package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.TabeFragmentModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.TableFragmentView;
import java.util.List;

public class TableFragmentPrestener implements OnResultListener {
    private Context context;
    private TabeFragmentModelImpl tableFragmentModel = new TabeFragmentModelImpl();
    private TableFragmentView tableFragmentView;

    public TableFragmentPrestener(Context context, TableFragmentView tableFragmentView) {
        this.context = context;
        this.tableFragmentView = tableFragmentView;
    }

    public void getTableFragmentListFromDB() {
        this.tableFragmentView.ShowTableNumber(this.tableFragmentModel.getTableNumberListFromDB(this.tableFragmentView.getTableAreaId()));
    }

    public void getTableFragmentListFromNET(long j) {
        this.tableFragmentModel.getTableNumberListFromNET(0, this.context, this.tableFragmentView.getTableAreaId(), this, null);
    }

    public void setTableNumber(List<TableNumber> list) {
        this.tableFragmentModel.setTableNumber(list);
    }

    public void onSuccess(int i, String str) {
        this.tableFragmentModel.saveTableNumber(this.tableFragmentModel.asyncJson(str));
        getTableFragmentListFromDB();
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
    }
}
