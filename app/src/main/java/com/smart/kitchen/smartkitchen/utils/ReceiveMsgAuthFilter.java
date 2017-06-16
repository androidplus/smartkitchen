package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import com.smart.kitchen.smartkitchen.db.daoutils.MessageCenterDaoManager;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;

public class ReceiveMsgAuthFilter {
    private static final String TAG = "ReceiveMsgAuthFilter";
    MessageCenter center;
    Context context;
    MessageCenterDaoManager m = new MessageCenterDaoManager();

    public ReceiveMsgAuthFilter(MessageCenter messageCenter, Context context) {
        this.center = messageCenter;
        this.context = context;
    }

    public boolean canSave() {
        if (this.center.getMsg_type().intValue() <= 4) {
            if (this.center.getFlag().intValue() == 6) {
                return canReceiveBackOrder();
            }
            return canReceiveOrder();
        } else if (this.center.getMsg_type().intValue() == 5) {
            return canReceivePay();
        } else {
            if (this.center.getMsg_type().intValue() == 9) {
                return canReceiveWarning();
            }
            return false;
        }
    }

    public boolean canReceiveOrder() {
        if (this.m.checkIsExist(this.center) != null) {
            return false;
        }
        return SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONHAVEORDER);
    }

    public boolean canReceivePay() {
        return SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONPAY);
    }

    public boolean canReceiveWarning() {
        if (this.m.getScockById(this.center.getMsg_id()) != null) {
            return false;
        }
        return SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONWARRING);
    }

    public boolean canReceiveBackOrder() {
        return SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONCANCEL);
    }
}
