package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import java.util.List;

public interface TabeFragmentModel {
    List<TableNumber> asyncJson(String str);

    List<TableNumber> getTableNumberListFromDB(long j);

    void getTableNumberListFromNET(int i, Context context, long j, OnResultListener onResultListener, ProgressDialog progressDialog);

    void saveTableNumber(List<TableNumber> list);

    void setTableNumber(List<TableNumber> list);
}
