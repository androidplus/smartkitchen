package com.smart.kitchen.smartkitchen.utils;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class FinishActivity {
    private static List<Activity> list = new ArrayList();

    public static void add(Activity activity) {
        if (!list.contains(activity)) {
            list.add(activity);
        }
    }

    public static void finish() {
        for (int i = 0; i < list.size(); i++) {
            ((Activity) list.get(i)).finish();
        }
        clear();
    }

    public static void clear() {
        list.clear();
    }
}
