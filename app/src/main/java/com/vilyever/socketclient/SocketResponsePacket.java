package com.vilyever.socketclient;

import java.util.Arrays;

public class SocketResponsePacket {
    private final byte[] data;
    private final String message;
    final SocketResponsePacket self = this;

    public SocketResponsePacket(byte[] bArr, String str) {
        this.data = bArr;
        this.message = str;
    }

    public boolean isMatch(String str) {
        if (getMessage() == null) {
            return false;
        }
        return getMessage().equals(str);
    }

    public boolean isMatch(byte[] bArr) {
        return Arrays.equals(getData(), bArr);
    }

    public byte[] getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }
}
