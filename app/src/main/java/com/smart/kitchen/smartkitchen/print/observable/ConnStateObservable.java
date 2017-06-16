package com.smart.kitchen.smartkitchen.print.observable;

import java.util.Observable;

public class ConnStateObservable extends Observable {
    private static ConnStateObservable mConnStateObservable;

    private ConnStateObservable() {
    }

    public static ConnStateObservable getInstance() {
        if (mConnStateObservable == null) {
            mConnStateObservable = new ConnStateObservable();
        }
        return mConnStateObservable;
    }

    public void setChanged() {
        super.setChanged();
    }
}
