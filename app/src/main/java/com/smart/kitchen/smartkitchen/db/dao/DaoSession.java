package com.smart.kitchen.smartkitchen.db.dao;

import android.database.sqlite.SQLiteDatabase;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.StoreInfo;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;
import java.util.Map;

public class DaoSession extends AbstractDaoSession {
    private final FoodTypeDao foodTypeDao;
    private final DaoConfig foodTypeDaoConfig;
    private final GoodSizeDao goodSizeDao ;
    private final DaoConfig goodSizeDaoConfig;
    private final GoodTasteDao goodTasteDao;
    private final DaoConfig goodTasteDaoConfig;
    private final GoodsDao goodsDao;
    private final DaoConfig goodsDaoConfig;
    private final MessageCenterDao messageCenterDao;
    private final DaoConfig messageCenterDaoConfig;
    private final StoreInfoDao storeInfoDao;
    private final DaoConfig storeInfoDaoConfig;
    private final TableAreaDao tableAreaDao;
    private final DaoConfig tableAreaDaoConfig;
    private final TableNumberDao tableNumberDao;
    private final DaoConfig tableNumberDaoConfig;
    private final UserInfoDao userInfoDao;
    private final DaoConfig userInfoDaoConfig;

    public DaoSession(SQLiteDatabase sQLiteDatabase, IdentityScopeType identityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(sQLiteDatabase);
        this.storeInfoDaoConfig = ((DaoConfig) map.get(StoreInfoDao.class)).clone();
        this.storeInfoDaoConfig.initIdentityScope(identityScopeType);
        this.goodSizeDaoConfig = ((DaoConfig) map.get(GoodSizeDao.class)).clone();
        this.goodSizeDaoConfig.initIdentityScope(identityScopeType);
        this.goodTasteDaoConfig = ((DaoConfig) map.get(GoodTasteDao.class)).clone();
        this.goodTasteDaoConfig.initIdentityScope(identityScopeType);
        this.userInfoDaoConfig = ((DaoConfig) map.get(UserInfoDao.class)).clone();
        this.userInfoDaoConfig.initIdentityScope(identityScopeType);
        this.foodTypeDaoConfig = ((DaoConfig) map.get(FoodTypeDao.class)).clone();
        this.foodTypeDaoConfig.initIdentityScope(identityScopeType);
        this.goodsDaoConfig = ((DaoConfig) map.get(GoodsDao.class)).clone();
        this.goodsDaoConfig.initIdentityScope(identityScopeType);
        this.tableAreaDaoConfig = ((DaoConfig) map.get(TableAreaDao.class)).clone();
        this.tableAreaDaoConfig.initIdentityScope(identityScopeType);
        this.tableNumberDaoConfig = ((DaoConfig) map.get(TableNumberDao.class)).clone();
        this.tableNumberDaoConfig.initIdentityScope(identityScopeType);
        this.messageCenterDaoConfig = ((DaoConfig) map.get(MessageCenterDao.class)).clone();
        this.messageCenterDaoConfig.initIdentityScope(identityScopeType);

        foodTypeDao = new FoodTypeDao(this.foodTypeDaoConfig, this);
        goodSizeDao = new GoodSizeDao(this.goodSizeDaoConfig, this);
        goodTasteDao = new GoodTasteDao(this.goodTasteDaoConfig, this);
        goodsDao = new GoodsDao(this.goodsDaoConfig, this);
        messageCenterDao = new MessageCenterDao(this.messageCenterDaoConfig, this);
        storeInfoDao = new StoreInfoDao(this.storeInfoDaoConfig, this);
        tableAreaDao = new TableAreaDao(this.tableAreaDaoConfig, this);
        tableNumberDao = new TableNumberDao(this.tableNumberDaoConfig, this);
        userInfoDao = new UserInfoDao(this.userInfoDaoConfig, this);

        registerDao(StoreInfo.class, this.storeInfoDao);
        registerDao(GoodSize.class, this.goodSizeDao);
        registerDao(GoodTaste.class, this.goodTasteDao);
        registerDao(UserInfo.class, this.userInfoDao);
        registerDao(FoodType.class, this.foodTypeDao);
        registerDao(Goods.class, this.goodsDao);
        registerDao(TableArea.class, this.tableAreaDao);
        registerDao(TableNumber.class, this.tableNumberDao);
        registerDao(MessageCenter.class, this.messageCenterDao);
    }

    public void clear() {
        this.storeInfoDaoConfig.getIdentityScope().clear();
        this.goodSizeDaoConfig.getIdentityScope().clear();
        this.goodTasteDaoConfig.getIdentityScope().clear();
        this.userInfoDaoConfig.getIdentityScope().clear();
        this.foodTypeDaoConfig.getIdentityScope().clear();
        this.goodsDaoConfig.getIdentityScope().clear();
        this.tableAreaDaoConfig.getIdentityScope().clear();
        this.tableNumberDaoConfig.getIdentityScope().clear();
        this.messageCenterDaoConfig.getIdentityScope().clear();
    }

    public StoreInfoDao getStoreInfoDao() {
        return this.storeInfoDao;
    }

    public GoodSizeDao getGoodSizeDao() {
        return this.goodSizeDao;
    }

    public GoodTasteDao getGoodTasteDao() {
        return this.goodTasteDao;
    }

    public UserInfoDao getUserInfoDao() {
        return this.userInfoDao;
    }

    public FoodTypeDao getFoodTypeDao() {
        return this.foodTypeDao;
    }

    public GoodsDao getGoodsDao() {
        return this.goodsDao;
    }

    public TableAreaDao getTableAreaDao() {
        return this.tableAreaDao;
    }

    public TableNumberDao getTableNumberDao() {
        return this.tableNumberDao;
    }

    public MessageCenterDao getMessageCenterDao() {
        return this.messageCenterDao;
    }
}
