package com.smart.kitchen.smartkitchen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class MessageCenterDao extends AbstractDao<MessageCenter, Long> {
    public static final String TABLENAME = "MESSAGE_CENTER";

    public static class Properties {
        public static final Property Flag = new Property(3, Integer.class, "flag", false, "FLAG");
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Msg_content = new Property(4, String.class, "msg_content", false, "MSG_CONTENT");
        public static final Property Msg_id = new Property(1, Long.class, "msg_id", false, "MSG_ID");
        public static final Property Msg_type = new Property(2, Integer.class, "msg_type", false, "MSG_TYPE");
        public static final Property Status = new Property(5, Integer.class, "status", false, "STATUS");
        public static final Property Times = new Property(6, String.class, "times", false, "TIMES");
    }

    public MessageCenterDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public MessageCenterDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "'MESSAGE_CENTER' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'MSG_ID' INTEGER," + "'MSG_TYPE' INTEGER," + "'FLAG' INTEGER," + "'MSG_CONTENT' TEXT," + "'STATUS' INTEGER," + "'TIMES' TEXT);");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "'MESSAGE_CENTER'");
    }

    protected void bindValues(SQLiteStatement sQLiteStatement, MessageCenter messageCenter) {
        sQLiteStatement.clearBindings();
        Long id = messageCenter.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        id = messageCenter.getMsg_id();
        if (id != null) {
            sQLiteStatement.bindLong(2, id.longValue());
        }
        Integer msg_type = messageCenter.getMsg_type();
        if (msg_type != null) {
            sQLiteStatement.bindLong(3, (long) msg_type.intValue());
        }
        msg_type = messageCenter.getFlag();
        if (msg_type != null) {
            sQLiteStatement.bindLong(4, (long) msg_type.intValue());
        }
        String msg_content = messageCenter.getMsg_content();
        if (msg_content != null) {
            sQLiteStatement.bindString(5, msg_content);
        }
        msg_type = messageCenter.getStatus();
        if (msg_type != null) {
            sQLiteStatement.bindLong(6, (long) msg_type.intValue());
        }
        msg_content = messageCenter.getTimes();
        if (msg_content != null) {
            sQLiteStatement.bindString(7, msg_content);
        }
    }

    public Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
    }

    public MessageCenter readEntity(Cursor cursor, int i) {
        String str = null;
        Long valueOf = cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0));
        Long valueOf2 = cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1));
        Integer valueOf3 = cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2));
        Integer valueOf4 = cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3));
        String string = cursor.isNull(i + 4) ? null : cursor.getString(i + 4);
        Integer valueOf5 = cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5));
        if (!cursor.isNull(i + 6)) {
            str = cursor.getString(i + 6);
        }
        return new MessageCenter(valueOf, valueOf2, valueOf3, valueOf4, string, valueOf5, str);
    }

    public void readEntity(Cursor cursor, MessageCenter messageCenter, int i) {
        String str = null;
        messageCenter.setId(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        messageCenter.setMsg_id(cursor.isNull(i + 1) ? null : Long.valueOf(cursor.getLong(i + 1)));
        messageCenter.setMsg_type(cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2)));
        messageCenter.setFlag(cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)));
        messageCenter.setMsg_content(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        messageCenter.setStatus(cursor.isNull(i + 5) ? null : Integer.valueOf(cursor.getInt(i + 5)));
        if (!cursor.isNull(i + 6)) {
            str = cursor.getString(i + 6);
        }
        messageCenter.setTimes(str);
    }

    protected Long updateKeyAfterInsert(MessageCenter messageCenter, long j) {
        messageCenter.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(MessageCenter messageCenter) {
        if (messageCenter != null) {
            return messageCenter.getId();
        }
        return null;
    }

    protected boolean isEntityUpdateable() {
        return true;
    }
}
