package com.vilyever.a;

import android.app.Application;
import android.content.Context;

/* compiled from: ContextHolder */
public class a {
    static Context a;

    public static Context a() {
        if (a != null) {
            return a;
        }
        Application application;
        try {
            application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, (Object[]) null);
            if (application != null) {
                a = application;
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke(null, (Object[]) null);
            if (application != null) {
                a = application;
                return application;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
    }
}
