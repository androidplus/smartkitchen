package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.CheckInfo;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class CheckInfoDao extends AbstractDao<CheckInfo, Long> {
    public static final String TABLENAME = "CHECK_INFO";

    public static class Properties {
        public static final Property Changetime = new Property(4, String.class, "changetime", false, "CHANGETIME");
        public static final Property Changevalue = new Property(5, Integer.class, "changevalue", false, "CHANGEVALUE");
        public static final Property Enable = new Property(7, Integer.class, "enable", false, "ENABLE");
        public static final Property Goodsid = new Property(1, Long.class, "goodsid", false, "GOODSID");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Mark = new Property(6, String.class, "mark", false, "MARK");
        public static final Property Operator = new Property(3, String.class, "operator", false, "OPERATOR");
        public static final Property Stocks = new Property(2, Integer.class, "stocks", false, "STOCKS");
    }

    public CheckInfoDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public CheckInfoDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'CHECK_INFO' (" + "'_id' INTEGER PRIMARY KEY ," + "'GOODSID' INTEGER," + "'STOCKS' INTEGER," + "'OPERATOR' TEXT," + "'CHANGETIME' TEXT," + "'CHANGEVALUE' INTEGER," + "'MARK' TEXT," + "'ENABLE' INTEGER);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'CHECK_INFO'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, CheckInfo checkInfo) {
        sQLiteStatement.clearBindings();
        Long id = checkInfo.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        id = checkInfo.getGoodsid();
        if (id != null) {
            sQLiteStatement.bindLong(2, id.longValue());
        }
        Integer stocks = checkInfo.getStocks();
        if (stocks != null) {
            sQLiteStatement.bindLong(3, (long) stocks.intValue());
        }
        String operator = checkInfo.getOperator();
        if (operator != null) {
            sQLiteStatement.bindString(4, operator);
        }
        operator = checkInfo.getChangetime();
        if (operator != null) {
            sQLiteStatement.bindString(5, operator);
        }
        stocks = checkInfo.getChangevalue();
        if (stocks != null) {
            sQLiteStatement.bindLong(6, (long) stocks.intValue());
        }
        operator = checkInfo.getMark();
        if (operator != null) {
            sQLiteStatement.bindString(7, operator);
        }
        stocks = checkInfo.getEnable();
        if (stocks != null) {
            sQLiteStatement.bindLong(8, (long) stocks.intValue());
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public CheckInfo readEntity(Cursor cursor, int i) {
        Integer num = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        Long valueOf2 = cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1));
        Integer valueOf3 = cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2));
        String string = cursor.isNull(i + 3) ? null : cursor.getString(i + 3);
        String string2 = cursor.isNull(i + 4) ? null : cursor.getString(i + 4);
        Integer valueOf4 = cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5));
        String string3 = cursor.isNull(i + 6) ? null : cursor.getString(i + 6);
        if (!cursor.isNull(i + 7)) {
            num = Integer.valueOf(cursor.getInt(i + 7));
        }
        return new CheckInfo(valueOf, valueOf2, valueOf3, string, string2, valueOf4, string3, num);
    }

    public void readEntity(Cursor cursor, CheckInfo checkInfo, int i) {
        Integer num = null;
        checkInfo.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        checkInfo.setGoodsid(cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1)));
        checkInfo.setStocks(cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2)));
        checkInfo.setOperator(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        checkInfo.setChangetime(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        checkInfo.setChangevalue(cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5)));
        checkInfo.setMark(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        if (!cursor.isNull(i + 7)) {
            num = Integer.valueOf(cursor.getInt(i + 7));
        }
        checkInfo.setEnable(num);
    }

    protected Long updateKeyAfterInsert(CheckInfo checkInfo, long j) {
        checkInfo.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(CheckInfo checkInfo) {
        if (checkInfo != null) {
            return checkInfo.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
