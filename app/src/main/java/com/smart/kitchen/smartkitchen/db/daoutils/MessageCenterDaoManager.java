package com.smart.kitchen.smartkitchen.db.daoutils;

import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.MessageCenterDao.Properties;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import de.greenrobot.dao.AbstractDao;
import java.util.ArrayList;
import java.util.List;

public class MessageCenterDaoManager extends DbManager {
    private static final String TAG = "MessageCenterDaoManager";

    protected AbstractDao getWriteDao(Class<? extends Object> cls) {
        daoSession = daoMaster_write.newSession();
        return daoSession.getMessageCenterDao();
    }

    protected AbstractDao getReadDao(Class<? extends Object> cls) {
        daoSession = daoMaster_read.newSession();
        return daoSession.getMessageCenterDao();
    }

    public void updateStatus(MessageCenter messageCenter) {
        messageCenter.setStatus(Integer.valueOf(1));
        update(MessageCenter.class, messageCenter);
    }

    public List<MessageCenter> getNoReadedList() {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Status);
        arrayList.add(Properties.Times);
        List arrayList2 = new ArrayList();
        arrayList2.add(Integer.valueOf(0));
        arrayList2.add(CalendarUtils.getNowFullDate());
        return DbManager.getInstance().queryMultObject(MessageCenter.class, arrayList, arrayList2);
    }

    public List<MessageCenter> getReadedList() {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Status);
        arrayList.add(Properties.Times);
        List arrayList2 = new ArrayList();
        arrayList2.add(Integer.valueOf(1));
        arrayList2.add(CalendarUtils.getNowFullDate());
        return DbManager.getInstance().queryMultObject(MessageCenter.class, arrayList, arrayList2);
    }

    public MessageCenter getScockById(Long l) {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Msg_id);
        arrayList.add(Properties.Msg_type);
        arrayList.add(Properties.Times);
        List arrayList2 = new ArrayList();
        arrayList2.add(l);
        arrayList2.add(Integer.valueOf(9));
        arrayList2.add(CalendarUtils.getNowFullDate());
        return (MessageCenter) DbManager.getInstance().query(MessageCenter.class, arrayList, arrayList2);
    }

    public MessageCenter checkIsExist(MessageCenter messageCenter) {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Msg_id);
        arrayList.add(Properties.Msg_type);
        arrayList.add(Properties.Flag);
        arrayList.add(Properties.Times);
        List arrayList2 = new ArrayList();
        arrayList2.add(messageCenter.getMsg_id());
        arrayList2.add(messageCenter.getMsg_type());
        arrayList2.add(messageCenter.getFlag());
        arrayList2.add(CalendarUtils.getNowFullDate());
        return (MessageCenter) DbManager.getInstance().query(MessageCenter.class, arrayList, arrayList2);
    }

    public void saveMsg(MessageCenter messageCenter) {
        messageCenter.setTimes(CalendarUtils.getNowFullDate());
        LogUtils.e(TAG, "saveMsg: " + messageCenter.toString());
        insert(MessageCenter.class, messageCenter);
    }
}
