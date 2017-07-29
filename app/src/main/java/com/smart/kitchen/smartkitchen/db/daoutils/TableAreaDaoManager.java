package com.smart.kitchen.smartkitchen.db.daoutils;

import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import de.greenrobot.dao.AbstractDao;
import java.util.ArrayList;
import java.util.List;

public class TableAreaDaoManager extends DbManager {
    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getTableAreaDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getTableAreaDao();
    }

    public List<TableArea> getAll() {
        return (List<TableArea>) queryAll(TableArea.class);
    }

    public void compare(List<TableArea> list) {
        TableNumberDaoManager tableNumberDaoManager = new TableNumberDaoManager();
        List all = getAll();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < all.size(); i++) {
            Object obj = null;
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (((TableArea) all.get(i)).getId() == ((TableArea) list.get(i2)).getId()) {
                    obj = 1;
                }
            }
            if (obj == null) {
                delete(TableArea.class, all.get(i));
                tableNumberDaoManager.deleteMultObject(TableNumber.class, tableNumberDaoManager.getAll(((TableArea) all.get(i)).getId()));
            }
        }
    }
}
