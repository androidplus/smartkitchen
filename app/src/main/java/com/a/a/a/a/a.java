package com.a.a.a.a;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* compiled from: MigrationHelper */
public class a {
    private static a a;

    public static a a() {
        if (a == null) {
            a = new a();
        }
        return a;
    }

    public void a(SQLiteDatabase sQLiteDatabase, Class<? extends AbstractDao<?, ?>>... clsArr) {
        b(sQLiteDatabase, clsArr);
        a(sQLiteDatabase, true, clsArr);
        b(sQLiteDatabase, false, clsArr);
        c(sQLiteDatabase, clsArr);
    }

    private void b(SQLiteDatabase sQLiteDatabase, Class<? extends AbstractDao<?, ?>>... clsArr) {
        for (Class daoConfig : clsArr) {
            DaoConfig daoConfig2 = new DaoConfig(sQLiteDatabase, daoConfig);
            String str = "";
            String str2 = daoConfig2.tablename;
            String concat = daoConfig2.tablename.concat("_TEMP");
            ArrayList<String> arrayList = new ArrayList<String>();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CREATE TABLE ").append(concat).append(" (");
            for (int i = 0; i < daoConfig2.properties.length; i++) {
                String str3 = daoConfig2.properties[i].columnName;
                if (a(sQLiteDatabase, str2).contains(str3)) {
                    arrayList.add(str3);
                    String str4 = null;
                    try {
                        str4 = a(daoConfig2.properties[i].type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stringBuilder.append(str).append(str3).append(" ").append(str4);
                    if (daoConfig2.properties[i].primaryKey) {
                        stringBuilder.append(" PRIMARY KEY");
                    }
                    str = ",";
                }
            }
            stringBuilder.append(");");
            if (!stringBuilder.toString().contains(" ();")) {
                sQLiteDatabase.execSQL(stringBuilder.toString());
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("INSERT INTO ").append(concat).append(" (");
                stringBuilder2.append(TextUtils.join(",", arrayList));
                stringBuilder2.append(") SELECT ");
                stringBuilder2.append(TextUtils.join(",", arrayList));
                stringBuilder2.append(" FROM ").append(str2).append(";");
                sQLiteDatabase.execSQL(stringBuilder2.toString());
            }
        }
    }

    private void a(SQLiteDatabase sQLiteDatabase, boolean z, Class<? extends AbstractDao<?, ?>>... clsArr) {
        for (Class daoConfig : clsArr) {
            sQLiteDatabase.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"" + new DaoConfig(sQLiteDatabase, daoConfig).tablename + "\"");
        }
    }

    private void b(SQLiteDatabase sQLiteDatabase, boolean z, Class<? extends AbstractDao<?, ?>>... clsArr) {
        for (Class daoConfig : clsArr) {
            DaoConfig daoConfig2 = new DaoConfig(sQLiteDatabase, daoConfig);
            String str = "";
            String str2 = daoConfig2.tablename;
            ArrayList arrayList = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CREATE TABLE ").append(z ? "IF NOT EXISTS " : "").append(str2).append(" (");
            for (int i = 0; i < daoConfig2.properties.length; i++) {
                String str3 = daoConfig2.properties[i].columnName;
                arrayList.add(str3);
                str2 = null;
                try {
                    str2 = a(daoConfig2.properties[i].type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stringBuilder.append(str).append(str3).append(" ").append(str2);
                if (daoConfig2.properties[i].primaryKey) {
                    stringBuilder.append(" PRIMARY KEY");
                }
                str = ",";
            }
            stringBuilder.append(");");
            sQLiteDatabase.execSQL(stringBuilder.toString());
        }
    }

    private void c(SQLiteDatabase sQLiteDatabase, Class<? extends AbstractDao<?, ?>>... clsArr) {
        for (Class daoConfig : clsArr) {
            DaoConfig daoConfig2 = new DaoConfig(sQLiteDatabase, daoConfig);
            String str = daoConfig2.tablename;
            String concat = daoConfig2.tablename.concat("_TEMP");
            ArrayList arrayList = new ArrayList();
            for (Property property : daoConfig2.properties) {
                String str2 = property.columnName;
                if (a(sQLiteDatabase, concat).contains(str2)) {
                    arrayList.add(str2);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INSERT INTO ").append(str).append(" (");
            stringBuilder.append(TextUtils.join(",", arrayList));
            stringBuilder.append(") SELECT ");
            stringBuilder.append(TextUtils.join(",", arrayList));
            stringBuilder.append(" FROM ").append(concat).append(";");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("DROP TABLE ").append(concat);
            if (!stringBuilder.toString().contains("()")) {
                sQLiteDatabase.execSQL(stringBuilder.toString());
                sQLiteDatabase.execSQL(stringBuilder2.toString());
            }
        }
    }

    private String a(Class<?> cls) throws Exception {
        if (cls.equals(String.class)) {
            return "TEXT";
        }
        if (cls.equals(Long.class) || cls.equals(Integer.class) || cls.equals(Long.TYPE) || cls.equals(Date.class)) {
            return "INTEGER";
        }
        if (cls.equals(Boolean.class)) {
            return "BOOLEAN";
        }
        throw new Exception("MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS".concat(" - Class: ").concat(cls.toString()));
    }

    private static List<String> a(SQLiteDatabase sQLiteDatabase, String str) {
        Cursor cursor = null;
        List<String> arrayList = new ArrayList();
        try {
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM " + str + " limit 1", null);
            if (cursor != null) {
                arrayList = new ArrayList(Arrays.asList(cursor.getColumnNames()));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }
}
