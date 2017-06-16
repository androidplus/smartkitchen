package com.vilyever.socketclient;

import com.vilyever.socketclient.util.BytesWrapper;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

public class PollingHelper {
    private final String defaultCharsetName;
    private HashMap<BytesWrapper, BytesWrapper> queryResponseMap;
    final PollingHelper self = this;

    public PollingHelper(String str) {
        this.defaultCharsetName = str;
        registerQueryResponse(SocketPacket.DefaultPollingQueryMessage, SocketPacket.DefaultPollingResponseMessage);
    }

    public PollingHelper registerQueryResponse(String str, String str2) {
        return registerQueryResponse(str, str2, getDefaultCharsetName());
    }

    public PollingHelper registerQueryResponse(String str, String str2, String str3) {
        return registerQueryResponse(str.getBytes(Charset.forName(str3)), str2.getBytes(Charset.forName(str3)));
    }

    public PollingHelper registerQueryResponse(byte[] bArr, byte[] bArr2) {
        getQueryResponseMap().put(new BytesWrapper(bArr), new BytesWrapper(bArr2));
        return this;
    }

    public PollingHelper registerQueryResponse(HashMap<BytesWrapper, BytesWrapper> hashMap) {
        getQueryResponseMap().putAll(hashMap);
        return this;
    }

    public PollingHelper removeQueryResponse(String str) {
        return removeQueryResponse(str, getDefaultCharsetName());
    }

    public PollingHelper removeQueryResponse(String str, String str2) {
        return removeQueryResponse(str.getBytes(Charset.forName(str2)));
    }

    public PollingHelper removeQueryResponse(byte[] bArr) {
        getQueryResponseMap().remove(bArr);
        return this;
    }

    public PollingHelper clear() {
        getQueryResponseMap().clear();
        return this;
    }

    public PollingHelper append(PollingHelper pollingHelper) {
        registerQueryResponse(pollingHelper.getQueryResponseMap());
        return this;
    }

    public boolean containsQuery(byte[] bArr) {
        for (Entry key : getQueryResponseMap().entrySet()) {
            if (((BytesWrapper) key.getKey()).equalsBytes(bArr)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsResponse(byte[] bArr) {
        for (Entry value : getQueryResponseMap().entrySet()) {
            if (((BytesWrapper) value.getValue()).equalsBytes(bArr)) {
                return true;
            }
        }
        return false;
    }

    public byte[] getResponse(byte[] bArr) {
        return getResponse(new BytesWrapper(bArr)).getBytes();
    }

    public BytesWrapper getResponse(BytesWrapper bytesWrapper) {
        return (BytesWrapper) getQueryResponseMap().get(bytesWrapper);
    }

    public String getDefaultCharsetName() {
        return this.defaultCharsetName;
    }

    protected HashMap<BytesWrapper, BytesWrapper> getQueryResponseMap() {
        if (this.queryResponseMap == null) {
            this.queryResponseMap = new HashMap();
        }
        return this.queryResponseMap;
    }
}
