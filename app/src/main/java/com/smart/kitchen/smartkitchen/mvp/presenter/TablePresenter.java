package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.TableModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.TableView;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import java.util.List;

public class TablePresenter implements OnResultListener {
    private Context context;
    private long mms = 0;
    private ProgressDialog progressDialog;
    private TableModelImpl tableModel;
    private TableView tableView;

    public TablePresenter(Context context, TableView tableView, ProgressDialog progressDialog) {
        this.context = context;
        this.tableView = tableView;
        this.tableModel = new TableModelImpl();
        this.progressDialog = progressDialog;
    }

    public List<MessageCenter> getMessaggeListNOReadFromDB() {
        return this.tableModel.getMessageCenterListNORead();
    }

    public List<MessageCenter> getMessaggeListReadedFromDB() {
        return this.tableModel.getMessageCenterListReaded();
    }

    public void getAreaListFromDB() {
        this.tableView.ShowTableArea(this.tableModel.getTableAreaTypeFromDB());
    }

    public void getAreaListFromNET() {
        this.tableModel.getTableAreaFromNET(0, this.context, this, null);
    }

    public void setTableNumber(List<TableNumber> list) {
        this.tableModel.setTableNumber(list);
    }

    public void submitIndent(OrderInfo orderInfo) {
        long nowMms = CalendarUtils.getNowMms();
        if ((nowMms - this.mms) / 1000 >= 3) {
            this.mms = nowMms;
            this.tableModel.submitIndent(1, this.context, this, orderInfo, this.progressDialog);
        }
    }

    public void submitIndents(OrderInfo orderInfo) {
        long nowMms = CalendarUtils.getNowMms();
        if ((nowMms - this.mms) / 1000 >= 3) {
            this.mms = nowMms;
            this.tableModel.submitIndent(2, this.context, this, orderInfo, this.progressDialog);
        }
    }

    public TableNumber getTableNumber(long j) {
        return this.tableModel.getTableNumber(Long.valueOf(j));
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                this.tableModel.saveTableArea(this.tableModel.asyncJson(str));
                getAreaListFromDB();
                return;
            case 1:
                this.tableView.isSubmitIndent("onSuccess");
                return;
            case 2:
                this.tableView.isSubmitIndent("settleAccountsOnSuccess");
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 1:
                this.tableView.isSubmitIndent("onAlert");
                return;
            case 2:
                this.tableView.isSubmitIndent("settleAccountsOnAlert");
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 1:
                this.tableView.isSubmitIndent("onFailure");
                return;
            case 2:
                this.tableView.isSubmitIndent("settleAccountsOnFailure");
                return;
            default:
                return;
        }
    }
}
