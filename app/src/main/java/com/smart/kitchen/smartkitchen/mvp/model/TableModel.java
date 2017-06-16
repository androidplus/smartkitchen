package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import java.util.List;

public interface TableModel {
    List<TableArea> asyncJson(String str);

    List<MessageCenter> getMessageCenterListNORead();

    List<MessageCenter> getMessageCenterListReaded();

    void getTableAreaFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog);

    List<TableArea> getTableAreaTypeFromDB();

    void saveTableArea(List<TableArea> list);

    void setTableNumber(List<TableNumber> list);

    void submitIndent(int i, Context context, OnResultListener onResultListener, OrderInfo orderInfo, ProgressDialog progressDialog);
}
