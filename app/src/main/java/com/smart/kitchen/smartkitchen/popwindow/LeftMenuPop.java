package com.smart.kitchen.smartkitchen.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.LeftMenuAdapter;
import com.smart.kitchen.smartkitchen.entitys.LeftMenu;
import java.util.List;

public class LeftMenuPop {
    private static final String TAG = "LeftMenuPop";
    private Context context;
    private OnItemClickListener mListener;
    public PopupWindow popWindow;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public LeftMenuPop(Context context) {
        this.context = context;
    }

    public void leftMenuPopWindow(List<LeftMenu> list, View view, View view2, OnItemClickListener onItemClickListener) {
        if (this.popWindow == null) {
            this.mListener = onItemClickListener;
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.left_menu, null, false);
            ListView listView = (ListView) inflate.findViewById(R.id.left_menu);
            this.popWindow = new PopupWindow(inflate, (view2.getWidth() * 2) / 3, -1, true);
            initDateList(list, this.popWindow, listView, this.mListener);
            this.popWindow.setTouchable(true);
            this.popWindow.setTouchInterceptor(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            this.popWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        this.popWindow.showAsDropDown(view, 0, 0);
    }

    private void initDateList(List<LeftMenu> list, PopupWindow popupWindow, ListView listView, final OnItemClickListener onItemClickListener) {
        final LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(list, this.context);
        listView.setAdapter(leftMenuAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                leftMenuAdapter.setSelect(i);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }
}
