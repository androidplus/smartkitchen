package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class GoodTasteDao extends AbstractDao<GoodTaste, Long> {
    public static final String TABLENAME = "GOOD_TASTE";

    public static class Properties {
        public static final Property Goodsid = new Property(2, String.class, "goodsid", false, "GOODSID");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Tastename = new Property(1, String.class, "tastename", false, "TASTENAME");
    }

    public GoodTasteDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public GoodTasteDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'GOOD_TASTE' (" + "'_id' INTEGER PRIMARY KEY ," + "'TASTENAME' TEXT," + "'GOODSID' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'GOOD_TASTE'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, GoodTaste goodTaste) {
        sQLiteStatement.clearBindings();
        Long id = goodTaste.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String tastename = goodTaste.getTastename();
        if (tastename != null) {
            sQLiteStatement.bindString(2, tastename);
        }
        tastename = goodTaste.getGoodsid();
        if (tastename != null) {
            sQLiteStatement.bindString(3, tastename);
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public GoodTaste readEntity(Cursor cursor, int i) {
        String str = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        String string = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        if (!cursor.isNull(i + 2)) {
            str = cursor.getString(i + 2);
        }
        return new GoodTaste(valueOf, string, str);
    }

    public void readEntity(Cursor cursor, GoodTaste goodTaste, int i) {
        String str = null;
        goodTaste.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        goodTaste.setTastename(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        if (!cursor.isNull(i + 2)) {
            str = cursor.getString(i + 2);
        }
        goodTaste.setGoodsid(str);
    }

    protected Long updateKeyAfterInsert(GoodTaste goodTaste, long j) {
        goodTaste.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(GoodTaste goodTaste) {
        if (goodTaste != null) {
            return goodTaste.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
