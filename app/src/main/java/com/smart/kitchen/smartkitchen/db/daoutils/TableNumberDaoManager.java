package com.smart.kitchen.smartkitchen.db.daoutils;

import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.TableNumberDao.Properties;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import de.greenrobot.dao.AbstractDao;
import java.util.ArrayList;
import java.util.List;

public class TableNumberDaoManager extends DbManager {
    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getTableNumberDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getTableNumberDao();
    }

    public List<TableNumber> getAll(Long l) {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Pid);
        List arrayList2 = new ArrayList();
        arrayList2.add(l);
        return queryMultObject(TableNumber.class, arrayList, arrayList2);
    }

    public void compare(List<TableNumber> list) {
        Long valueOf = Long.valueOf(-1);
        if (list != null && list.size() > 0) {
            valueOf = ((TableNumber) list.get(0)).getPid();
        }
        List all = getAll(valueOf);
        for (int i = 0; i < all.size(); i++) {
            int i2 = 0;
            for (int i3 = 0; i3 < list.size(); i3++) {
                if (((TableNumber) all.get(i)).getId() == ((TableNumber) list.get(i3)).getId()) {
                    i2 = 1;
                }
            }
            if (i2 == 0) {
                delete(TableNumber.class, all.get(i));
            }
        }
    }
}
