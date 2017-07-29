package com.smart.kitchen.smartkitchen.db.daoutils;

import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import de.greenrobot.dao.AbstractDao;

public class FoodTypeDaoManager extends DbManager {
    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getFoodTypeDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getFoodTypeDao();
    }
}
