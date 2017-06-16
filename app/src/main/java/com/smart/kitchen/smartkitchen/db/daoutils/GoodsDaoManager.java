package com.smart.kitchen.smartkitchen.db.daoutils;

import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import de.greenrobot.dao.AbstractDao;

public class GoodsDaoManager extends DbManager {
    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getGoodsDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getGoodsDao();
    }
}
