package com.vilyever.socketclient.util;

public class ExceptionThrower {
    final ExceptionThrower self = this;

    public static void throwIllegalStateException(String str) {
        throw new IllegalStateException(str);
    }
}
