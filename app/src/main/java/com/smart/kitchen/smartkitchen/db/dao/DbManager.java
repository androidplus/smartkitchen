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
    public Object query(Class<? extends Object> r8, List<de.greenrobot.dao.Property> r9, List<String> r10, List<Object> r11) {
        /*
        r7 = this;
        r3 = 0;
        if (r9 == 0) goto L_0x0009;
    L_0x0003:
        r0 = r9.isEmpty();
        if (r0 == 0) goto L_0x000b;
    L_0x0009:
        r0 = 0;
    L_0x000a:
        return r0;
    L_0x000b:
        r4 = r9.size();
        r0 = r7.getWriteDao(r8);
        r5 = r0.queryBuilder();
        r2 = r3;
    L_0x0018:
        if (r2 >= r4) goto L_0x00b6;
    L_0x001a:
        r0 = r10.get(r2);
        r0 = (java.lang.String) r0;
        r1 = -1;
        r6 = r0.hashCode();
        switch(r6) {
            case -216634360: goto L_0x004e;
            case 3244: goto L_0x0030;
            case 3321751: goto L_0x0044;
            case 105007839: goto L_0x003a;
            default: goto L_0x0028;
        };
    L_0x0028:
        r0 = r1;
    L_0x0029:
        switch(r0) {
            case 0: goto L_0x0058;
            case 1: goto L_0x006d;
            case 2: goto L_0x0082;
            case 3: goto L_0x0099;
            default: goto L_0x002c;
        };
    L_0x002c:
        r0 = r2;
    L_0x002d:
        r2 = r0 + 1;
        goto L_0x0018;
    L_0x0030:
        r6 = "eq";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0038:
        r0 = r3;
        goto L_0x0029;
    L_0x003a:
        r6 = "notEq";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0042:
        r0 = 1;
        goto L_0x0029;
    L_0x0044:
        r6 = "like";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x004c:
        r0 = 2;
        goto L_0x0029;
    L_0x004e:
        r6 = "between";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0056:
        r0 = 3;
        goto L_0x0029;
    L_0x0058:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r0 = r0.eq(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x006d:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r0 = r0.notEq(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x0082:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r1 = (java.lang.String) r1;
        r0 = r0.like(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x0099:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r6 = r2 + 1;
        r6 = r11.get(r6);
        r0 = r0.between(r1, r6);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2 + 1;
        goto L_0x002d;
    L_0x00b6:
        r0 = r5.build();
        r0 = r0.unique();
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.smart.kitchen.smartkitchen.db.dao.DbManager.query(java.lang.Class, java.util.List, java.util.List, java.util.List):java.lang.Object");
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
    public List<? extends Object> queryMultObject(Class<? extends Object> r8, List<de.greenrobot.dao.Property> r9, List<String> r10, List<Object> r11) {
        /*
        r7 = this;
        r3 = 0;
        if (r9 == 0) goto L_0x0009;
    L_0x0003:
        r0 = r9.isEmpty();
        if (r0 == 0) goto L_0x000b;
    L_0x0009:
        r0 = 0;
    L_0x000a:
        return r0;
    L_0x000b:
        r4 = r9.size();
        r0 = r7.getWriteDao(r8);
        r5 = r0.queryBuilder();
        r2 = r3;
    L_0x0018:
        if (r2 >= r4) goto L_0x00b6;
    L_0x001a:
        r0 = r10.get(r2);
        r0 = (java.lang.String) r0;
        r1 = -1;
        r6 = r0.hashCode();
        switch(r6) {
            case -216634360: goto L_0x004e;
            case 3244: goto L_0x0030;
            case 3321751: goto L_0x0044;
            case 105007839: goto L_0x003a;
            default: goto L_0x0028;
        };
    L_0x0028:
        r0 = r1;
    L_0x0029:
        switch(r0) {
            case 0: goto L_0x0058;
            case 1: goto L_0x006d;
            case 2: goto L_0x0082;
            case 3: goto L_0x0099;
            default: goto L_0x002c;
        };
    L_0x002c:
        r0 = r2;
    L_0x002d:
        r2 = r0 + 1;
        goto L_0x0018;
    L_0x0030:
        r6 = "eq";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0038:
        r0 = r3;
        goto L_0x0029;
    L_0x003a:
        r6 = "notEq";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0042:
        r0 = 1;
        goto L_0x0029;
    L_0x0044:
        r6 = "like";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x004c:
        r0 = 2;
        goto L_0x0029;
    L_0x004e:
        r6 = "between";
        r0 = r0.equals(r6);
        if (r0 == 0) goto L_0x0028;
    L_0x0056:
        r0 = 3;
        goto L_0x0029;
    L_0x0058:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r0 = r0.eq(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x006d:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r0 = r0.notEq(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x0082:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r1 = (java.lang.String) r1;
        r0 = r0.like(r1);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2;
        goto L_0x002d;
    L_0x0099:
        r0 = r9.get(r2);
        r0 = (de.greenrobot.dao.Property) r0;
        r1 = r11.get(r2);
        r6 = r2 + 1;
        r6 = r11.get(r6);
        r0 = r0.between(r1, r6);
        r1 = new de.greenrobot.dao.query.WhereCondition[r3];
        r5.where(r0, r1);
        r0 = r2 + 1;
        goto L_0x002d;
    L_0x00b6:
        r0 = r5.build();
        r0 = r0.list();
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.smart.kitchen.smartkitchen.db.dao.DbManager.queryMultObject(java.lang.Class, java.util.List, java.util.List, java.util.List):java.util.List<? extends java.lang.Object>");
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
