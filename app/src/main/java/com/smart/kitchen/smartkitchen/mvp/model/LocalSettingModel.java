package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import java.util.List;

public interface LocalSettingModel {
    List<TableArea> asyncJson(String str);

    void getTableAreaFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog);
}
