package com.smart.kitchen.smartkitchen.db.dao;

import android.content.Context;
import com.smart.kitchen.smartkitchen.MyApplication;
import com.smart.kitchen.smartkitchen.db.dao.DaoMaster.DevOpenHelper;
import com.smart.kitchen.smartkitchen.entitys.FoodType;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import java.util.List;

public class DbManager {
    protected static final String DB_NAME = "smartkitchen";
    protected static DaoMaster daoMaster_read;
    protected static DaoMaster daoMaster_write;
    protected static DaoSession daoSession;
    protected static volatile DbManager instance;
    protected static DevOpenHelper openHelper;
    protected Context context = MyApplication.getApp();

    protected DbManager() {
        openHelper = new DevOpenHelper(this.context, DB_NAME, null);
        daoMaster_write = new DaoMaster(openHelper.getWritableDatabase());
        daoMaster_read = new DaoMaster(openHelper.getReadableDatabase());
    }

    public static DbManager getInstance() {
        DbManager dbManager = instance;
        if (dbManager == null) {
            synchronized (DbManager.class) {
                if (dbManager == null) {
                    instance = new DbManager();
                    dbManager = instance;
                }
            }
        }
        return dbManager;
    }

    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getDao(cls);
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getDao(cls);
    }

    public long insert(Class<? extends Object> cls, Object obj) {
        if (obj == null) {
            return -1;
        }
        return getWriteDao(cls).insert(obj);
    }

    public void insertMultObject(Class<? extends Object> cls, List<? extends Object> list) {
        if (list != null || list.size() > 0) {
            getWriteDao(cls).insertInTx((Iterable) list);
        }
    }

    public Object query(Class<? extends Object> cls, List<Property> list, List<Object> list2) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        QueryBuilder queryBuilder = getWriteDao(cls).queryBuilder();
        for (int i = 0; i < size; i++) {
            queryBuilder.where(((Property) list.get(i)).eq(list2.get(i)), new WhereCondition[0]);
        }
        return queryBuilder.build().unique();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Object query(Class<? extends Object> clazz, List<de.greenrobot.dao.Property> list, List<String> list2, List<String> list3) {
        Object unique;
        if (list == null || list.isEmpty()) {
            unique = null;
        }
        else {
            final int size = list.size();
            final QueryBuilder queryBuilder = this.getWriteDao(clazz).queryBuilder();
            int n3 = 0;
            for (int i = 0; i < size; i = n3 + 1) {
                final String s = list2.get(i);
                final int n = -1;
                int n2 = -1;
                switch (s) {
                    case "eq":
                        n2 = 0;
                        break;
                    case "notEq":
                        n2 = 1;
                        break;
                    case "like":
                        n2 = 2;
                        break;
                    case "between":
                        n2 = 3;
                        break;
                }
                switch (n2) {
                    default: {
                        n3 = i;
                        break;
                    }
                    case 0: {
                        queryBuilder.where(list.get(i).eq(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 1: {
                        queryBuilder.where(list.get(i).notEq(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 2: {
                        queryBuilder.where(list.get(i).like(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 3: {
                        queryBuilder.where(list.get(i).between(list3.get(i), list3.get(i + 1)), new WhereCondition[0]);
                        n3 = i + 1;
                        break;
                    }
                }
            }
            unique = queryBuilder.build().unique();
        }
        return unique;
    }

    public List<? extends Object> queryMultObject(Class<? extends Object> cls, List<Property> list, List<Object> list2) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        QueryBuilder queryBuilder = getWriteDao(cls).queryBuilder();
        for (int i = 0; i < size; i++) {
            queryBuilder.where(((Property) list.get(i)).eq(list2.get(i)), new WhereCondition[0]);
        }
        return queryBuilder.build().list();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public List<? extends Object> queryMultObject(Class<? extends Object> clazz, List<de.greenrobot.dao.Property> list, List<String> list2, List<String> list3) {
        List list4;
        if (list == null || list.isEmpty()) {
            list4 = null;
        }
        else {
            final int size = list.size();
            final QueryBuilder queryBuilder = this.getWriteDao(clazz).queryBuilder();
            int n3 = 0;
            for (int i = 0; i < size; i = n3 + 1) {
                final String s = list2.get(i);
                final int n = -1;
                int n2 = -1;
                switch (s) {
                    case "eq":
                        n2 = 0;
                        break;
                    case "notEq":
                        n2 = 1;
                        break;
                    case "like":
                        n2 = 2;
                        break;
                    case "between":
                        n2 = 3;
                        break;
                }

                switch (n2) {
                    default: {
                        n3 = i;
                        break;
                    }
                    case 0: {
                        queryBuilder.where(list.get(i).eq(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 1: {
                        queryBuilder.where(list.get(i).notEq(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 2: {
                        queryBuilder.where(list.get(i).like(list3.get(i)), new WhereCondition[0]);
                        n3 = i;
                        break;
                    }
                    case 3: {
                        queryBuilder.where(list.get(i).between(list3.get(i), list3.get(i + 1)), new WhereCondition[0]);
                        n3 = i + 1;
                        break;
                    }
                }
            }
            list4 = queryBuilder.build().list();
        }
        return list4;
    }

    public List<? extends Object> queryAll(Class< ? extends Object> cls) {
        return getWriteDao(cls).queryBuilder().list();
    }

    public void delete(Class<? extends Object> cls, Object obj) {
        if (obj != null) {
            getWriteDao(cls).delete(obj);
        }
    }

    public void deleteMultObject(Class<? extends Object> cls, List<? extends Object> list) {
        if (list != null && !list.isEmpty()) {
            getWriteDao(cls).updateInTx((Iterable) list);
        }
    }

    public void deleteAll(Class<? extends Object> cls) {
        getWriteDao(cls).deleteAll();
    }

    public void update(Class<? extends Object> cls, Object obj) {
        if (obj != null) {
            getWriteDao(cls).update(obj);
        }
    }

    public void updateMultObject(Class<? extends Object> cls, List<? extends Object> list) {
        if (list != null && !list.isEmpty()) {
            getWriteDao(cls).updateInTx((Iterable) list);
        }
    }
}
