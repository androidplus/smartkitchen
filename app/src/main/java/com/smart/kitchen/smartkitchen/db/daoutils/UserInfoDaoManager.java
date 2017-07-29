package com.smart.kitchen.smartkitchen.db.daoutils;

import android.database.Cursor;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.UserInfoDao.Properties;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import de.greenrobot.dao.AbstractDao;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDaoManager extends DbManager {
    private static final String TAG = "UserInfoDaoManager";

    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getUserInfoDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getUserInfoDao();
    }

    public void exeque(Class<UserInfo> cls) {
        List<UserInfo> list = (List<UserInfo>) queryAll(cls);
        for (UserInfo userInfo : list) {
            LogUtils.e(TAG, "exeque: " + userInfo.toString());
        }
    }

    private long getMaxId() {
        daoSession = daoMaster_read.newSession();
        Cursor rawQuery = openHelper.getReadableDatabase().rawQuery("select max(_id) AS maxId from user_info", null);
        int i = -1;
        if (rawQuery.moveToNext()) {
            i = rawQuery.getInt(rawQuery.getColumnIndex("maxId"));
        }
        rawQuery.close();
        return (long) i;
    }

    public UserInfo getNowUserInfo() {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Id);
        List arrayList2 = new ArrayList();
        arrayList2.add(Long.valueOf(getMaxId()));
        return (UserInfo) query(UserInfo.class, arrayList, arrayList2);
    }

    public UserInfo getBeforeUserInfo() {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Id);
        List arrayList2 = new ArrayList();
        long maxId = getMaxId();
        if (maxId <= 1) {
            return null;
        }
        arrayList2.add(Long.valueOf(maxId - 1));
        return (UserInfo) query(UserInfo.class, arrayList, arrayList2);
    }

    public void offWorkUserInfo() {
        UserInfo nowUserInfo = getNowUserInfo();
        nowUserInfo.setWorkofftime(CalendarUtils.getNowFullTime());
        update(UserInfo.class, nowUserInfo);
    }
}
