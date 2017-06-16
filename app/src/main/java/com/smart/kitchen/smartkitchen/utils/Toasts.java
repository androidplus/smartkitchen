package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.widget.Toast;

public class Toasts {
    public static boolean isShow = true;

    private Toasts() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(Context context, CharSequence charSequence) {
        if (isShow) {
            Toast.makeText(context, charSequence, 0).show();
        }
    }

    public static void showShort(Context context, int i) {
        if (isShow) {
            Toast.makeText(context, i, 0).show();
        }
    }

    public static void showLong(Context context, CharSequence charSequence) {
        if (isShow) {
            Toast.makeText(context, charSequence, 1).show();
        }
    }

    public static void showLong(Context context, int i) {
        if (isShow) {
            Toast.makeText(context, i, 1).show();
        }
    }

    public static void show(Context context, CharSequence charSequence, int i) {
        if (isShow) {
            Toast.makeText(context, charSequence, i).show();
        }
    }

    public static void show(Context context, int i, int i2) {
        if (isShow) {
            Toast.makeText(context, i, i2).show();
        }
    }

    public static void show(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static void show(Context context, int i) {
        Toast.makeText(context, i, 0).show();
    }
}
