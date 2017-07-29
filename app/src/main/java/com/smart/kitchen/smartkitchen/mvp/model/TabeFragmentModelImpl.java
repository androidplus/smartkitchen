package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.TableNumberDao.Properties;
import com.smart.kitchen.smartkitchen.db.daoutils.TableNumberDaoManager;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import java.util.ArrayList;
import java.util.List;

public class TabeFragmentModelImpl implements TabeFragmentModel {
    TableNumberDaoManager daoManager = new TableNumberDaoManager();

    public List<TableNumber> getTableNumberListFromDB(long j) {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Pid);
        List arrayList2 = new ArrayList();
        arrayList2.add(Long.valueOf(j));
        TableNumberDaoManager tableNumberDaoManager = this.daoManager;
        return DbManager.getInstance().queryMultObject(TableNumber.class, arrayList, arrayList2);
    }

    public void getTableNumberListFromNET(int i, Context context, long j, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("area_id", j);
        HttpUtils.post(context, Contants.GET_TABLE_LSIT, requestParams, i, onResultListener, progressDialog);
    }

    public void saveTableNumber(List<TableNumber> list) {
        this.daoManager.compare(list);
        for (int i = 0; i < list.size(); i++) {
            List arrayList = new ArrayList();
            arrayList.add(Properties.Id);
            arrayList.add(Properties.Pid);
            List arrayList2 = new ArrayList();
            arrayList2.add(((TableNumber) list.get(i)).getId());
            arrayList2.add(((TableNumber) list.get(i)).getPid());
            TableNumberDaoManager tableNumberDaoManager = this.daoManager;
            TableNumber tableNumber = (TableNumber) DbManager.getInstance().query(TableNumber.class, arrayList, arrayList2);
            if (tableNumber != null) {
                tableNumber.setEating_count(((TableNumber) list.get(i)).getEating_count());
                tableNumber.setTable_name(((TableNumber) list.get(i)).getTable_name());
                TableNumberDaoManager tableNumberDaoManager2 = this.daoManager;
                DbManager.getInstance().update(TableNumber.class, tableNumber);
            } else {
                ((TableNumber) list.get(i)).setTable_person(Integer.valueOf(0));
                ((TableNumber) list.get(i)).setTable_type_count(Integer.valueOf(0));
                tableNumberDaoManager = this.daoManager;
                DbManager.getInstance().insert(TableNumber.class, list.get(i));
            }
        }
    }

    public void setTableNumber(List<TableNumber> list) {
        for (int i = 0; i < list.size(); i++) {
            List arrayList = new ArrayList();
            arrayList.add(Properties.Id);
            arrayList.add(Properties.Pid);
            List arrayList2 = new ArrayList();
            arrayList2.add(((TableNumber) list.get(i)).getId());
            arrayList2.add(((TableNumber) list.get(i)).getPid());
            TableNumberDaoManager tableNumberDaoManager = this.daoManager;
            TableNumber tableNumber = (TableNumber) DbManager.getInstance().query(TableNumber.class, arrayList, arrayList2);
            if (tableNumber != null) {
                tableNumber.setTable_type_count(((TableNumber) list.get(i)).getTable_type_count());
                tableNumber.setTable_person(((TableNumber) list.get(i)).getTable_person());
                TableNumberDaoManager tableNumberDaoManager2 = this.daoManager;
                DbManager.getInstance().update(TableNumber.class, tableNumber);
            }
        }
    }

    public List<TableNumber> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<TableNumber>>() {
        }, new Feature[0]);
    }
}
