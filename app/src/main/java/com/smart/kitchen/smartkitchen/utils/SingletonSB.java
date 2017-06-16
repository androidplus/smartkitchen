package com.smart.kitchen.smartkitchen.utils;

public class SingletonSB {
    private static SingletonSB instance = null;
    private boolean isChecked = false;

    public static synchronized SingletonSB getInstance() {
        SingletonSB singletonSB;
        synchronized (SingletonSB.class) {
            if (instance == null) {
                instance = new SingletonSB();
            }
            singletonSB = instance;
        }
        return singletonSB;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }
}
