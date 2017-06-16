package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class GoodsDao extends AbstractDao<Goods, Long> {
    public static final String TABLENAME = "GOODS";

    public static class Properties {
        public static final Property Count = new Property(11, Integer.class, "count", false, "COUNT");
        public static final Property Delete_time = new Property(13, String.class, "delete_time", false, "DELETE_TIME");
        public static final Property Describe = new Property(8, String.class, "describe", false, "DESCRIBE");
        public static final Property Goods_image_url = new Property(4, String.class, "goods_image_url", false, "GOODS_IMAGE_URL");
        public static final Property Goods_size = new Property(6, String.class, "goods_size", false, "GOODS_SIZE");
        public static final Property Goods_type = new Property(3, Long.class, "goods_type", false, "GOODS_TYPE");
        public static final Property Goodsbarcode = new Property(9, String.class, "goodsbarcode", false, "GOODSBARCODE");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Is_delete = new Property(12, Integer.TYPE, "is_delete", false, "IS_DELETE");
        public static final Property Is_warning_count = new Property(5, String.class, "is_warning_count", false, "IS_WARNING_COUNT");
        public static final Property Money = new Property(10, Double.class, "money", false, "MONEY");
        public static final Property Name = new Property(1, String.class, "name", false, "NAME");
        public static final Property Orderid = new Property(2, String.class, "orderid", false, "ORDERID");
        public static final Property Taste = new Property(7, String.class, "taste", false, "TASTE");
    }

    public GoodsDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public GoodsDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'GOODS' (" + "'_id' INTEGER PRIMARY KEY ," + "'NAME' TEXT," + "'ORDERID' TEXT," + "'GOODS_TYPE' INTEGER," + "'GOODS_IMAGE_URL' TEXT," + "'IS_WARNING_COUNT' TEXT," + "'GOODS_SIZE' TEXT," + "'TASTE' TEXT," + "'DESCRIBE' TEXT," + "'GOODSBARCODE' TEXT," + "'MONEY' REAL," + "'COUNT' INTEGER," + "'IS_DELETE' INTEGER NOT NULL ," + "'DELETE_TIME' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'GOODS'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, Goods goods) {
        sQLiteStatement.clearBindings();
        Long id = goods.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String name = goods.getName();
        if (name != null) {
            sQLiteStatement.bindString(2, name);
        }
        name = goods.getOrderid();
        if (name != null) {
            sQLiteStatement.bindString(3, name);
        }
        id = goods.getGoods_type();
        if (id != null) {
            sQLiteStatement.bindLong(4, id.longValue());
        }
        name = goods.getGoods_image_url();
        if (name != null) {
            sQLiteStatement.bindString(5, name);
        }
        name = goods.getIs_warning_count();
        if (name != null) {
            sQLiteStatement.bindString(6, name);
        }
        name = goods.getGoods_size();
        if (name != null) {
            sQLiteStatement.bindString(7, name);
        }
        name = goods.getTaste();
        if (name != null) {
            sQLiteStatement.bindString(8, name);
        }
        name = goods.getDescribe();
        if (name != null) {
            sQLiteStatement.bindString(9, name);
        }
        name = goods.getGoodsbarcode();
        if (name != null) {
            sQLiteStatement.bindString(10, name);
        }
        Double money = goods.getMoney();
        if (money != null) {
            sQLiteStatement.bindDouble(11, money.doubleValue());
        }
        Integer count = goods.getCount();
        if (count != null) {
            sQLiteStatement.bindLong(12, (long) count.intValue());
        }
        sQLiteStatement.bindLong(13, (long) goods.getIs_delete());
        name = goods.getDelete_time();
        if (name != null) {
            sQLiteStatement.bindString(14, name);
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public Goods readEntity(Cursor cursor, int i) {
        return new Goods(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : Long.valueOf(cursor.getLong(i + 3)), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6), cursor.isNull(i + 7) ? null : cursor.getString(i + 7), cursor.isNull(i + 8) ? null : cursor.getString(i + 8), cursor.isNull(i + 9) ? null : cursor.getString(i + 9), cursor.isNull(i + 10) ? null : Double.valueOf(cursor.getDouble(i + 10)), cursor.isNull(i + 11) ? null : Integer.valueOf(cursor.getInt(i + 11)), cursor.getInt(i + 12), cursor.isNull(i + 13) ? null : cursor.getString(i + 13));
    }

    public void readEntity(Cursor cursor, Goods goods, int i) {
        String str = null;
        goods.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        goods.setName(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        goods.setOrderid(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        goods.setGoods_type(cursor.isNull(i + 3) ? null : Long.valueOf(cursor.getLong(i + 3)));
        goods.setGoods_image_url(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        goods.setIs_warning_count(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        goods.setGoods_size(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        goods.setTaste(cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        goods.setDescribe(cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
        goods.setGoodsbarcode(cursor.isNull(i + 9) ? null : cursor.getString(i + 9));
        goods.setMoney(cursor.isNull(i + 10) ? null : Double.valueOf(cursor.getDouble(i + 10)));
        goods.setCount(cursor.isNull(i + 11) ? null : Integer.valueOf(cursor.getInt(i + 11)));
        goods.setIs_delete(cursor.getInt(i + 12));
        if (!cursor.isNull(i + 13)) {
            str = cursor.getString(i + 13);
        }
        goods.setDelete_time(str);
    }

    protected Long updateKeyAfterInsert(Goods goods, long j) {
        goods.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(Goods goods) {
        if (goods != null) {
            return goods.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
