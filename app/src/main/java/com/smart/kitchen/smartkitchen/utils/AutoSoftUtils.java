package com.smart.kitchen.smartkitchen.utils;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSoftUtils {
    private static final String TAG = "AutoSoftUtils";
    private static Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            EditText editText = (EditText) message.obj;
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            ((InputMethodManager) editText.getContext().getSystemService("input_method")).showSoftInput(editText, 0);
        }
    };

    public static void showSoftInput(final EditText editText) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                LogUtils.e(AutoSoftUtils.TAG, "run: ");
                Message obtain = Message.obtain();
                obtain.what = 0;
                obtain.obj = editText;
                AutoSoftUtils.handler.sendMessage(obtain);
            }
        }, 100);
    }

    public static void hideInput(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static boolean isShouldHideInput(View view, MotionEvent motionEvent) {
        if (view == null || !(view instanceof EditText)) {
            return false;
        }
        int[] iArr = new int[]{0, 0};
        view.getLocationInWindow(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        int height = view.getHeight() + i2;
        int width = view.getWidth() + i;
        if (motionEvent.getX() <= ((float) i) || motionEvent.getX() >= ((float) width) || motionEvent.getY() <= ((float) i2) || motionEvent.getY() >= ((float) height)) {
            return true;
        }
        return false;
    }
}
