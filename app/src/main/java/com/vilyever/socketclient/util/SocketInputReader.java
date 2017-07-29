package com.vilyever.socketclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class SocketInputReader extends Reader {
    private InputStream inputStream;
    final SocketInputReader self = this;

    public SocketInputReader(InputStream inputStream) {
        super(inputStream);
        this.inputStream = inputStream;
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
        }
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        throw new IOException("read() is not support for SocketInputReader, try readBytes().");
    }

    public byte[] readBytes() throws IOException {
        synchronized (this.lock) {
            if (isOpen()) {
                try {
                    ArrayList arrayList = new ArrayList();
                    do {
                        int read = this.inputStream.read();
                        if (-1 == read) {
                            break;
                        }
                        arrayList.add(Byte.valueOf((byte) read));
                        if (arrayList.size() > 2 && ((Byte) arrayList.get(arrayList.size() - 1)).byteValue() == (byte) 10 && ((Byte) arrayList.get(arrayList.size() - 2)).byteValue() == (byte) 13) {
                            arrayList.remove(arrayList.size() - 1);
                            arrayList.remove(arrayList.size() - 1);
                            break;
                        }
                    } while (this.inputStream.available() != 0);
                    if (arrayList.size() == 0) {
                        return null;
                    }
                    byte[] bArr = new byte[arrayList.size()];
                    Iterator it = arrayList.iterator();
                    for (int i = 0; i < bArr.length; i++) {
                        bArr[i] = ((Byte) it.next()).byteValue();
                    }
                    return bArr;
                } catch (IOException e) {
                    return null;
                }
            }
            throw new IOException("InputStreamReader is closed");
        }
    }

    public boolean ready() throws IOException {
        boolean z = false;
        synchronized (this.lock) {
            if (this.inputStream == null) {
                throw new IOException("InputStreamReader is closed");
            }
            try {
                if (this.inputStream.available() > 0) {
                    z = true;
                }
            } catch (IOException e) {
            }
        }
        return z;
    }

    public static void checkOffsetAndCount(int i, int i2, int i3) {
        if ((i2 | i3) < 0 || i2 > i || i - i2 < i3) {
            throw new ArrayIndexOutOfBoundsException("arrayLength=" + i + "; offset=" + i2 + "; count=" + i3);
        }
    }

    private boolean isOpen() {
        return this.inputStream != null;
    }
}
