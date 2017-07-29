package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class GoodSizeDao extends AbstractDao<GoodSize, Void> {
    public static final String TABLENAME = "GOOD_SIZE";

    public static class Properties {
        public static final Property Goodsid = new Property(3, String.class, "goodsid", false, "GOODSID");
        public static final Property Id = new Property(0, Long.class, "id", false, "ID");
        public static final Property Sale_price = new Property(2, Double.class, "sale_price", false, "SALE_PRICE");
        public static final Property Spec_name = new Property(1, String.class, "spec_name", false, "SPEC_NAME");
    }

    public GoodSizeDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public GoodSizeDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'GOOD_SIZE' (" + "'ID' INTEGER," + "'SPEC_NAME' TEXT," + "'SALE_PRICE' REAL," + "'GOODSID' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'GOOD_SIZE'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, GoodSize goodSize) {
        sQLiteStatement.clearBindings();
        Long id = goodSize.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String spec_name = goodSize.getSpec_name();
        if (spec_name != null) {
            sQLiteStatement.bindString(2, spec_name);
        }
        Double sale_price = goodSize.getSale_price();
        if (sale_price != null) {
            sQLiteStatement.bindDouble(3, sale_price.doubleValue());
        }
        spec_name = goodSize.getGoodsid();
        if (spec_name != null) {
            sQLiteStatement.bindString(4, spec_name);
        }
    }

    public Void readKey(Cursor cursor, int i) {
        return null;
    }

    public GoodSize readEntity(Cursor cursor, int i) {
        String str = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        String string = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        Double valueOf2 = cursor.isNull(i + 2) ? null : Double.valueOf(cursor.getDouble(i + 2));
        if (!cursor.isNull(i + 3)) {
            str = cursor.getString(i + 3);
        }
        return new GoodSize(valueOf, string, valueOf2, str);
    }

    public void readEntity(Cursor cursor, GoodSize goodSize, int i) {
        String str = null;
        goodSize.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        goodSize.setSpec_name(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        goodSize.setSale_price(cursor.isNull(i + 2) ? null : Double.valueOf(cursor.getDouble(i + 2)));
        if (!cursor.isNull(i + 3)) {
            str = cursor.getString(i + 3);
        }
        goodSize.setGoodsid(str);
    }

    protected Void updateKeyAfterInsert(GoodSize goodSize, long j) {
        return null;
    }

    public Void getKey(GoodSize goodSize) {
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
