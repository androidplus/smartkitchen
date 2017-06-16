package com.vilyever.socketclient;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketPacket {
    public static final byte[] DefaultHeartBeatMessage = "$HB$".getBytes(Charset.forName("UTF-8"));
    public static final byte[] DefaultPollingQueryMessage = "$PQ$".getBytes(Charset.forName("UTF-8"));
    public static final byte[] DefaultPollingResponseMessage = "$PR$".getBytes(Charset.forName("UTF-8"));
    private static final AtomicInteger IDAtomic = new AtomicInteger();
    private final int ID = IDAtomic.getAndIncrement();
    private final byte[] data;
    private final String message;
    private final SocketPacket self = this;

    public SocketPacket(byte[] bArr) {
        this.data = Arrays.copyOf(bArr, bArr.length);
        this.message = null;
    }

    public SocketPacket(String str) {
        this.message = str;
        this.data = null;
    }

    public static SocketPacket newInstanceWithBytes(byte[] bArr) {
        return new SocketPacket(bArr);
    }

    public static SocketPacket newInstanceWithString(String str) {
        return new SocketPacket(str);
    }

    public int getID() {
        return this.ID;
    }

    public String getMessage() {
        return this.message;
    }

    public byte[] getData() {
        return this.data;
    }
}
