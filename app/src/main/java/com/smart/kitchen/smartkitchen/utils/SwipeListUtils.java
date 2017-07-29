package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.util.TypedValue;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;

public class SwipeListUtils {
    private static final String TAG = "SwipeListUtils";

    private static int dp2px(Context context, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    public static void addMenuItem(final Context context, final String[][] strArr, final int[] iArr,
                                   SwipeMenuListView swipeMenuListView, OnMenuItemClickListener aVar) {
        swipeMenuListView.setMenuCreator(new SwipeMenuCreator() {
            public void create(com.baoyz.swipemenulistview.SwipeMenu aVar) {
                String[] strAr = strArr[0];
                LogUtils.e(SwipeListUtils.TAG, "create: " + aVar.getViewType());
                SwipeMenuItem dVar = new SwipeMenuItem(context);
                dVar.setWidth(SwipeListUtils.dp2px(context, 90));
                dVar.setTitleSize(18);
                dVar.setTitleColor(-1);
                dVar.setBackground(context.getResources().getDrawable(iArr[0]));
                dVar.setTitle(strAr[0]);
                aVar.addMenuItem(dVar);
            }
        });
        swipeMenuListView.setOnMenuItemClickListener(aVar);
    }
}
