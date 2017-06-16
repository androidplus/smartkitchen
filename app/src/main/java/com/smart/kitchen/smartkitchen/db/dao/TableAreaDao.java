package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class TableAreaDao extends AbstractDao<TableArea, Long> {
    public static final String TABLENAME = "TABLE_AREA";

    public static class Properties {
        public static final Property Area_name = new Property(1, String.class, "area_name", false, "AREA_NAME");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    }

    public TableAreaDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public TableAreaDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'TABLE_AREA' (" + "'_id' INTEGER PRIMARY KEY ," + "'AREA_NAME' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'TABLE_AREA'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, TableArea tableArea) {
        sQLiteStatement.clearBindings();
        Long id = tableArea.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String area_name = tableArea.getArea_name();
        if (area_name != null) {
            sQLiteStatement.bindString(2, area_name);
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public TableArea readEntity(Cursor cursor, int i) {
        String str = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        if (!cursor.isNull(i + 1)) {
            str = cursor.getString(i + 1);
        }
        return new TableArea(valueOf, str);
    }

    public void readEntity(Cursor cursor, TableArea tableArea, int i) {
        String str = null;
        tableArea.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        if (!cursor.isNull(i + 1)) {
            str = cursor.getString(i + 1);
        }
        tableArea.setArea_name(str);
    }

    protected Long updateKeyAfterInsert(TableArea tableArea, long j) {
        tableArea.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(TableArea tableArea) {
        if (tableArea != null) {
            return tableArea.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
