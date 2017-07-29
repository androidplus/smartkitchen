package com.vilyever.socketclient.util;

import java.util.Arrays;

public class BytesWrapper {
    private final byte[] bytes;
    final BytesWrapper self = this;

    public BytesWrapper(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        this.bytes = bArr;
    }

    public boolean equalsBytes(byte[] bArr) {
        return Arrays.equals(getBytes(), bArr);
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BytesWrapper) {
            return equalsBytes(((BytesWrapper) obj).getBytes());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(getBytes());
    }
}
