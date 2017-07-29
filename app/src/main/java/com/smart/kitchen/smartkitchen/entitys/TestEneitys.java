package com.smart.kitchen.smartkitchen.entitys;

import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import java.util.ArrayList;
import java.util.List;

public class TestEneitys {
    public static String BACK_FOODS = "back_foods";
    public static String DISTORY_FOODS = "distory_foods";
    public static String GET_MONEY = "get_money";
    public static String GET_ORDER = "get_order";
    public static String HISTORY_ORDER = "history_order";
    public static String HOLD = "hold";
    public static String LOCKER = "locker";
    public static String REPEAT = "repeat";
    public static String SETTING = "setting";
    public static String USERS = "users";
    public static String WRITEOFF = "writeOff";
    public static List<LeftMenu> leftMenus;

    public static List<LeftMenu> getLeftMenuData() {
        if (leftMenus == null) {
            leftMenus = new ArrayList();
            leftMenus.add(new LeftMenu(R.mipmap.get_money, R.mipmap.get_money_w, "点单", GET_MONEY));
            leftMenus.add(new LeftMenu(R.mipmap.get_order, R.mipmap.get_order_w, "取单", GET_ORDER));
            leftMenus.add(new LeftMenu(R.mipmap.history_order, R.mipmap.history_order_w, "历史订单", HISTORY_ORDER));
            if (AuthorityUtil.getInstance().getRoleFlag() == 2) {
                leftMenus.add(new LeftMenu(R.mipmap.back_foods, R.mipmap.back_foods_w, "退货", BACK_FOODS));
            }
            leftMenus.add(new LeftMenu(R.mipmap.distory_foods, R.mipmap.distory_foods_w, "报损", DISTORY_FOODS));
            leftMenus.add(new LeftMenu(R.mipmap.hold, R.mipmap.hold_w, "盘点", HOLD));
            leftMenus.add(new LeftMenu(R.mipmap.users, R.mipmap.users_w, "会员查询", USERS));
            leftMenus.add(new LeftMenu(R.mipmap.setting, R.mipmap.setting_w, "本地设置", SETTING));
            if (AuthorityUtil.getInstance().getRoleFlag() == 2) {
                leftMenus.add(new LeftMenu(R.mipmap.repeat, R.mipmap.repeat_w, "交接", REPEAT));
            }
            leftMenus.add(new LeftMenu(R.mipmap.locker, R.mipmap.locker_w, "锁屏", LOCKER));
            if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                leftMenus.add(new LeftMenu(R.mipmap.write_off, R.mipmap.write_off_w, "注销", WRITEOFF));
            }
        }
        return leftMenus;
    }

    public static void setEmpty() {
        leftMenus = null;
    }
}
