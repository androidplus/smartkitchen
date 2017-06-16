package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class FoodTypeDao extends AbstractDao<FoodType, Long> {
    public static final String TABLENAME = "FOOD_TYPE";

    public static class Properties {
        public static final Property Catalog = new Property(1, String.class, "catalog", false, "CATALOG");
        public static final Property Catalogid = new Property(2, Long.class, "catalogid", false, "CATALOGID");
        public static final Property Delete_time = new Property(3, String.class, "delete_time", false, "DELETE_TIME");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    }

    public FoodTypeDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public FoodTypeDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'FOOD_TYPE' (" + "'_id' INTEGER PRIMARY KEY ," + "'CATALOG' TEXT," + "'CATALOGID' INTEGER," + "'DELETE_TIME' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'FOOD_TYPE'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, FoodType foodType) {
        sQLiteStatement.clearBindings();
        Long id = foodType.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String catalog = foodType.getCatalog();
        if (catalog != null) {
            sQLiteStatement.bindString(2, catalog);
        }
        id = foodType.getCatalogid();
        if (id != null) {
            sQLiteStatement.bindLong(3, id.longValue());
        }
        catalog = foodType.getDelete_time();
        if (catalog != null) {
            sQLiteStatement.bindString(4, catalog);
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public FoodType readEntity(Cursor cursor, int i) {
        String str = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        String string = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        Long valueOf2 = cursor.isNull(i + 2) ? null : Long.valueOf(cursor.getLong(i + 2));
        if (!cursor.isNull(i + 3)) {
            str = cursor.getString(i + 3);
        }
        return new FoodType(valueOf, string, valueOf2, str);
    }

    public void readEntity(Cursor cursor, FoodType foodType, int i) {
        String str = null;
        foodType.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        foodType.setCatalog(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        foodType.setCatalogid(cursor.isNull(i + 2) ? null : Long.valueOf(cursor.getLong(i + 2)));
        if (!cursor.isNull(i + 3)) {
            str = cursor.getString(i + 3);
        }
        foodType.setDelete_time(str);
    }

    protected Long updateKeyAfterInsert(FoodType foodType, long j) {
        foodType.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(FoodType foodType) {
        if (foodType != null) {
            return foodType.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
