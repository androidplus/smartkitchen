package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.os.Process;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler ch = new CrashHandler();
    public final String TAG = getClass().getSimpleName();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return ch;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        LogUtils.e(this.TAG, "error : " + th);
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
