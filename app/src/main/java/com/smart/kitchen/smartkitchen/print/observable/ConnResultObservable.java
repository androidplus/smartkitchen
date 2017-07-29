package com.smart.kitchen.smartkitchen.print.observable;

import java.util.Observable;

public class ConnResultObservable extends Observable {
    private static ConnResultObservable mConnResultObservable;

    private ConnResultObservable() {
    }

    public static ConnResultObservable getInstance() {
        if (mConnResultObservable == null) {
            mConnResultObservable = new ConnResultObservable();
        }
        return mConnResultObservable;
    }

    public void setChanged() {
        super.setChanged();
    }
}
