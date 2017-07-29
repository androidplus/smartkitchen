package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class UserInfoDao extends AbstractDao<UserInfo, Long> {
    public static final String TABLENAME = "USER_INFO";

    public static class Properties {
        public static final Property Account = new Property(2, String.class, "account", false, "ACCOUNT");
        public static final Property Authority = new Property(9, String.class, "authority", false, "AUTHORITY");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Onwerid = new Property(10, Long.class, "onwerid", false, "ONWERID");
        public static final Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public static final Property Phone = new Property(5, String.class, "phone", false, "PHONE");
        public static final Property Realname = new Property(4, String.class, "realname", false, "REALNAME");
        public static final Property Role = new Property(6, String.class, "role", false, "ROLE");
        public static final Property Userid = new Property(1, String.class, "userid", false, "USERID");
        public static final Property Workofftime = new Property(8, String.class, "workofftime", false, "WORKOFFTIME");
        public static final Property Workontime = new Property(7, String.class, "workontime", false, "WORKONTIME");
    }

    public UserInfoDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public UserInfoDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'USER_INFO' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'USERID' TEXT," + "'ACCOUNT' TEXT," + "'PASSWORD' TEXT," + "'REALNAME' TEXT," + "'PHONE' TEXT," + "'ROLE' TEXT," + "'WORKONTIME' TEXT," + "'WORKOFFTIME' TEXT," + "'AUTHORITY' TEXT," + "'ONWERID' INTEGER);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'USER_INFO'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, UserInfo userInfo) {
        sQLiteStatement.clearBindings();
        Long id = userInfo.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        String userid = userInfo.getUserid();
        if (userid != null) {
            sQLiteStatement.bindString(2, userid);
        }
        userid = userInfo.getAccount();
        if (userid != null) {
            sQLiteStatement.bindString(3, userid);
        }
        userid = userInfo.getPassword();
        if (userid != null) {
            sQLiteStatement.bindString(4, userid);
        }
        userid = userInfo.getRealname();
        if (userid != null) {
            sQLiteStatement.bindString(5, userid);
        }
        userid = userInfo.getPhone();
        if (userid != null) {
            sQLiteStatement.bindString(6, userid);
        }
        userid = userInfo.getRole();
        if (userid != null) {
            sQLiteStatement.bindString(7, userid);
        }
        userid = userInfo.getWorkontime();
        if (userid != null) {
            sQLiteStatement.bindString(8, userid);
        }
        userid = userInfo.getWorkofftime();
        if (userid != null) {
            sQLiteStatement.bindString(9, userid);
        }
        userid = userInfo.getAuthority();
        if (userid != null) {
            sQLiteStatement.bindString(10, userid);
        }
        id = userInfo.getOnwerid();
        if (id != null) {
            sQLiteStatement.bindLong(11, id.longValue());
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public UserInfo readEntity(Cursor cursor, int i) {
        return new UserInfo(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6), cursor.isNull(i + 7) ? null : cursor.getString(i + 7), cursor.isNull(i + 8) ? null : cursor.getString(i + 8), cursor.isNull(i + 9) ? null : cursor.getString(i + 9), cursor.isNull(i + 10) ? null : Long.valueOf(cursor.getLong(i + 10)));
    }

    public void readEntity(Cursor cursor, UserInfo userInfo, int i) {
        Long l = null;
        userInfo.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        userInfo.setUserid(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        userInfo.setAccount(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        userInfo.setPassword(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        userInfo.setRealname(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        userInfo.setPhone(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        userInfo.setRole(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        userInfo.setWorkontime(cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        userInfo.setWorkofftime(cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
        userInfo.setAuthority(cursor.isNull(i + 9) ? null : cursor.getString(i + 9));
        if (!cursor.isNull(i + 10)) {
            l = Long.valueOf(cursor.getLong(i + 10));
        }
        userInfo.setOnwerid(l);
    }

    protected Long updateKeyAfterInsert(UserInfo userInfo, long j) {
        userInfo.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(UserInfo userInfo) {
        if (userInfo != null) {
            return userInfo.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
