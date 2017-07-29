package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.TableAreaDao.Properties;
import com.smart.kitchen.smartkitchen.db.dao.TableNumberDao;
import com.smart.kitchen.smartkitchen.db.daoutils.MessageCenterDaoManager;
import com.smart.kitchen.smartkitchen.db.daoutils.TableAreaDaoManager;
import com.smart.kitchen.smartkitchen.db.daoutils.TableNumberDaoManager;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;

public class TableModelImpl implements TableModel {
    TableAreaDaoManager daoManager = new TableAreaDaoManager();
    MessageCenterDaoManager messageCenterDaoManager = new MessageCenterDaoManager();
    TableNumberDaoManager tableNumberDaoManager = new TableNumberDaoManager();

    public List<MessageCenter> getMessageCenterListNORead() {
        return this.messageCenterDaoManager.getNoReadedList();
    }

    public List<MessageCenter> getMessageCenterListReaded() {
        return this.messageCenterDaoManager.getReadedList();
    }

    public List<TableArea> getTableAreaTypeFromDB() {
        TableAreaDaoManager tableAreaDaoManager = this.daoManager;
        return (List<TableArea>) DbManager.getInstance().queryAll(TableArea.class);
    }

    public void getTableAreaFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        HttpUtils.post(context, Contants.GET_TABLE_AREA, requestParams, i, onResultListener, progressDialog);
    }

    public void saveTableArea(List<TableArea> list) {
        this.daoManager.compare(list);
        for (int i = 0; i < list.size(); i++) {
            List arrayList = new ArrayList();
            arrayList.add(Properties.Id);
            List arrayList2 = new ArrayList();
            arrayList2.add(((TableArea) list.get(i)).getId());
            TableAreaDaoManager tableAreaDaoManager = this.daoManager;
            TableArea tableArea = (TableArea) DbManager.getInstance().query(TableArea.class, arrayList, arrayList2);
            if (tableArea != null) {
                tableArea.setArea_name(((TableArea) list.get(i)).getArea_name());
                TableAreaDaoManager tableAreaDaoManager2 = this.daoManager;
                DbManager.getInstance().update(TableArea.class, tableArea);
            } else {
                tableAreaDaoManager = this.daoManager;
                DbManager.getInstance().insert(TableArea.class, list.get(i));
            }
        }
    }

    public TableNumber getTableNumber(Long l) {
        List arrayList = new ArrayList();
        arrayList.add(TableNumberDao.Properties.Id);
        List arrayList2 = new ArrayList();
        arrayList2.add(l);
        TableNumberDaoManager tableNumberDaoManager = this.tableNumberDaoManager;
        return (TableNumber) DbManager.getInstance().query(TableNumber.class, arrayList, arrayList2);
    }

    public void setTableNumber(List<TableNumber> list) {
        for (int i = 0; i < list.size(); i++) {
            List arrayList = new ArrayList();
            arrayList.add(TableNumberDao.Properties.Id);
            arrayList.add(TableNumberDao.Properties.Pid);
            List arrayList2 = new ArrayList();
            arrayList2.add(((TableNumber) list.get(i)).getId());
            arrayList2.add(((TableNumber) list.get(i)).getPid());
            TableNumberDaoManager tableNumberDaoManager = this.tableNumberDaoManager;
            TableNumber tableNumber = (TableNumber) DbManager.getInstance().query(TableNumber.class, arrayList, arrayList2);
            if (tableNumber != null) {
                tableNumber.setTable_type_count(((TableNumber) list.get(i)).getTable_type_count());
                tableNumber.setTable_person(((TableNumber) list.get(i)).getTable_person());
                TableNumberDaoManager tableNumberDaoManager2 = this.tableNumberDaoManager;
                DbManager.getInstance().update(TableNumber.class, tableNumber);
            }
        }
    }

    public void submitIndent(int i, Context context, OnResultListener onResultListener, OrderInfo orderInfo, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("onwerid", SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", orderInfo.getOrderid());
        requestParams.put("totalprice", orderInfo.getTotalprice());
        requestParams.put("usersnum", orderInfo.getUsersnum());
        requestParams.put("tablenumber", orderInfo.getTablenumber());
        requestParams.put("mark", orderInfo.getMark());
        requestParams.put("waiterid", orderInfo.getWaiterid());
        requestParams.put("goodslist", orderInfo.getGoodslist());
        requestParams.put("areaid", SPUtils.getUserinfo(context, SPUtils.SETTING_AREA));
        HttpUtils.post(context, Contants.ADDORDERS, requestParams, i, onResultListener, progressDialog);
    }

    public List<TableArea> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<TableArea>>() {
        }, new Feature[0]);
    }
}
