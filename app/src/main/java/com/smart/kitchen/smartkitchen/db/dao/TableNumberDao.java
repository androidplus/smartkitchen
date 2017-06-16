package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class TableNumberDao extends AbstractDao<TableNumber, Long> {
    public static final String TABLENAME = "TABLE_NUMBER";

    public static class Properties {
        public static final Property Area_name = new Property(1, String.class, "area_name", false, "AREA_NAME");
        public static final Property Eating_count = new Property(5, Integer.class, "eating_count", false, "EATING_COUNT");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Pid = new Property(3, Long.class, "pid", false, "PID");
        public static final Property Table_name = new Property(2, String.class, "table_name", false, "TABLE_NAME");
        public static final Property Table_person = new Property(4, Integer.class, "table_person", false, "TABLE_PERSON");
        public static final Property Table_type_count = new Property(6, Integer.class, "table_type_count", false, "TABLE_TYPE_COUNT");
    }

    public TableNumberDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public TableNumberDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'TABLE_NUMBER' (" + "'_id' INTEGER PRIMARY KEY ," + "'AREA_NAME' TEXT," + "'TABLE_NAME' TEXT," + "'PID' INTEGER," + "'TABLE_PERSON' INTEGER," + "'EATING_COUNT' INTEGER," + "'TABLE_TYPE_COUNT' INTEGER);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'TABLE_NUMBER'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, TableNumber tableNumber) {
        sQLiteStatement.clearBindings();
        Long id = tableNumber.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String area_name = tableNumber.getArea_name();
        if (area_name != null) {
            sQLiteStatement.bindString(2, area_name);
        }
        area_name = tableNumber.getTable_name();
        if (area_name != null) {
            sQLiteStatement.bindString(3, area_name);
        }
        id = tableNumber.getPid();
        if (id != null) {
            sQLiteStatement.bindLong(4, id.longValue());
        }
        Integer table_person = tableNumber.getTable_person();
        if (table_person != null) {
            sQLiteStatement.bindLong(5, (long) table_person.intValue());
        }
        table_person = tableNumber.getEating_count();
        if (table_person != null) {
            sQLiteStatement.bindLong(6, (long) table_person.intValue());
        }
        table_person = tableNumber.getTable_type_count();
        if (table_person != null) {
            sQLiteStatement.bindLong(7, (long) table_person.intValue());
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public TableNumber readEntity(Cursor cursor, int i) {
        Integer num = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        String string = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        String string2 = cursor.isNull(i + 2) ? null : cursor.getString(i + 2);
        Long valueOf2 = cursor.isNull(i + 3) ? null : Long.valueOf(cursor.getLong(i + 3));
        Integer valueOf3 = cursor.isNull(i + 4) ? null : Integer.valueOf(cursor.getInt(i + 4));
        Integer valueOf4 = cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5));
        if (!cursor.isNull(i + 6)) {
            num = Integer.valueOf(cursor.getInt(i + 6));
        }
        return new TableNumber(valueOf, string, string2, valueOf2, valueOf3, valueOf4, num);
    }

    public void readEntity(Cursor cursor, TableNumber tableNumber, int i) {
        Integer num = null;
        tableNumber.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        tableNumber.setArea_name(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        tableNumber.setTable_name(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        tableNumber.setPid(cursor.isNull(i + 3) ? null : Long.valueOf(cursor.getLong(i + 3)));
        tableNumber.setTable_person(cursor.isNull(i + 4) ? null : Integer.valueOf(cursor.getInt(i + 4)));
        tableNumber.setEating_count(cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5)));
        if (!cursor.isNull(i + 6)) {
            num = Integer.valueOf(cursor.getInt(i + 6));
        }
        tableNumber.setTable_type_count(num);
    }

    protected Long updateKeyAfterInsert(TableNumber tableNumber, long j) {
        tableNumber.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(TableNumber tableNumber) {
        if (tableNumber != null) {
            return tableNumber.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
