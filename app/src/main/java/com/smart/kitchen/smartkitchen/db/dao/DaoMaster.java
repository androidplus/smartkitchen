package com.smart.kitchen.smartkitchen.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.a.a.a.a.a;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    public static abstract class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context, String str, CursorFactory cursorFactory) {
            super(context, str, cursorFactory, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            LogUtils.i("greenDAO", "Creating tables for schema version 1");
            DaoMaster.createAllTables(sQLiteDatabase, false);
        }
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String str, CursorFactory cursorFactory) {
            super(context, str, cursorFactory);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Class[] clsArr = new Class[0];
            clsArr[0] = UserInfoDao.class;
            clsArr[1] = FoodTypeDao.class;
            clsArr[2] = GoodsDao.class;
            clsArr[3] = MessageCenterDao.class;
            clsArr[5] = TableAreaDao.class;
            clsArr[6] = TableNumberDao.class;
            a.a().a(sQLiteDatabase, clsArr);
        }
    }

    public static void createAllTables(SQLiteDatabase sQLiteDatabase, boolean z) {
        StoreInfoDao.createTable(sQLiteDatabase, z);
        GoodSizeDao.createTable(sQLiteDatabase, z);
        GoodTasteDao.createTable(sQLiteDatabase, z);
        UserInfoDao.createTable(sQLiteDatabase, z);
        FoodTypeDao.createTable(sQLiteDatabase, z);
        GoodsDao.createTable(sQLiteDatabase, z);
        TableAreaDao.createTable(sQLiteDatabase, z);
        TableNumberDao.createTable(sQLiteDatabase, z);
        MessageCenterDao.createTable(sQLiteDatabase, z);
    }

    public static void dropAllTables(SQLiteDatabase sQLiteDatabase, boolean z) {
        StoreInfoDao.dropTable(sQLiteDatabase, z);
        GoodSizeDao.dropTable(sQLiteDatabase, z);
        GoodTasteDao.dropTable(sQLiteDatabase, z);
        UserInfoDao.dropTable(sQLiteDatabase, z);
        FoodTypeDao.dropTable(sQLiteDatabase, z);
        GoodsDao.dropTable(sQLiteDatabase, z);
        TableAreaDao.dropTable(sQLiteDatabase, z);
        TableNumberDao.dropTable(sQLiteDatabase, z);
        MessageCenterDao.dropTable(sQLiteDatabase, z);
    }

    public DaoMaster(SQLiteDatabase sQLiteDatabase) {
        super(sQLiteDatabase, 1);
        registerDaoClass(StoreInfoDao.class);
        registerDaoClass(GoodSizeDao.class);
        registerDaoClass(GoodTasteDao.class);
        registerDaoClass(UserInfoDao.class);
        registerDaoClass(FoodTypeDao.class);
        registerDaoClass(GoodsDao.class);
        registerDaoClass(TableAreaDao.class);
        registerDaoClass(TableNumberDao.class);
        registerDaoClass(MessageCenterDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType identityScopeType) {
        return new DaoSession(this.db, identityScopeType, this.daoConfigMap);
    }
}
