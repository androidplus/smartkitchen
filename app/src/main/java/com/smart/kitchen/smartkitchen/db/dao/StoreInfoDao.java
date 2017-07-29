package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.StoreInfo;
import com.smart.kitchen.smartkitchen.print.dialog.WifiDeviceChooseDialog;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class StoreInfoDao extends AbstractDao<StoreInfo, Void> {
    public static final String TABLENAME = "STORE_INFO";

    public static class Properties {
        public static final Property Address = new Property(6, String.class, WifiDeviceChooseDialog.BUNDLE_KEY_ADDRESS, false, "ADDRESS");
        public static final Property Company = new Property(4, String.class, "company", false, "COMPANY");
        public static final Property Contact = new Property(5, String.class, "contact", false, "CONTACT");
        public static final Property Dining_name = new Property(2, String.class, "dining_name", false, "DINING_NAME");
        public static final Property Id = new Property(0, Long.TYPE, "id", false, "ID");
        public static final Property Logo = new Property(8, String.class, "logo", false, "LOGO");
        public static final Property Parent_id = new Property(1, Long.class, "parent_id", false, "PARENT_ID");
        public static final Property Petty_cash = new Property(9, Double.class, "petty_cash", false, "PETTY_CASH");
        public static final Property Phone = new Property(3, String.class, "phone", false, "PHONE");
        public static final Property Status = new Property(7, String.class, "status", false, "STATUS");
    }

    public StoreInfoDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public StoreInfoDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'STORE_INFO' (" + "'ID' INTEGER NOT NULL ," + "'PARENT_ID' INTEGER," + "'DINING_NAME' TEXT," + "'PHONE' TEXT," + "'COMPANY' TEXT," + "'CONTACT' TEXT," + "'ADDRESS' TEXT," + "'STATUS' TEXT," + "'LOGO' TEXT NOT NULL ," + "'PETTY_CASH' REAL);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'STORE_INFO'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, StoreInfo storeInfo) {
        sQLiteStatement.clearBindings();
        sQLiteStatement.bindLong(1, storeInfo.getId());
        Long parent_id = storeInfo.getParent_id();
        if (parent_id != null) {
            sQLiteStatement.bindLong(2, parent_id.longValue());
        }
        String dining_name = storeInfo.getDining_name();
        if (dining_name != null) {
            sQLiteStatement.bindString(3, dining_name);
        }
        dining_name = storeInfo.getPhone();
        if (dining_name != null) {
            sQLiteStatement.bindString(4, dining_name);
        }
        dining_name = storeInfo.getCompany();
        if (dining_name != null) {
            sQLiteStatement.bindString(5, dining_name);
        }
        dining_name = storeInfo.getContact();
        if (dining_name != null) {
            sQLiteStatement.bindString(6, dining_name);
        }
        dining_name = storeInfo.getAddress();
        if (dining_name != null) {
            sQLiteStatement.bindString(7, dining_name);
        }
        dining_name = storeInfo.getStatus();
        if (dining_name != null) {
            sQLiteStatement.bindString(8, dining_name);
        }
        sQLiteStatement.bindString(9, storeInfo.getLogo());
        Double petty_cash = storeInfo.getPetty_cash();
        if (petty_cash != null) {
            sQLiteStatement.bindDouble(10, petty_cash.doubleValue());
        }
    }

    public Void readKey(Cursor cursor, int i) {
        return null;
    }

    public StoreInfo readEntity(Cursor cursor, int i) {
        return new StoreInfo(cursor.getLong(i + 0), cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1)), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6), cursor.isNull(i + 7) ? null : cursor.getString(i + 7), cursor.getString(i + 8), cursor.isNull(i + 9) ? null : Double.valueOf(cursor.getDouble(i + 9)));
    }

    public void readEntity(Cursor cursor, StoreInfo storeInfo, int i) {
        Double d = null;
        storeInfo.setId(cursor.getLong(i + 0));
        storeInfo.setParent_id(cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1)));
        storeInfo.setDining_name(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        storeInfo.setPhone(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        storeInfo.setCompany(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        storeInfo.setContact(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        storeInfo.setAddress(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        storeInfo.setStatus(cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        storeInfo.setLogo(cursor.getString(i + 8));
        if (!cursor.isNull(i + 9)) {
            d = Double.valueOf(cursor.getDouble(i + 9));
        }
        storeInfo.setPetty_cash(d);
    }

    protected Void updateKeyAfterInsert(StoreInfo storeInfo, long j) {
        return null;
    }

    public Void getKey(StoreInfo storeInfo) {
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
