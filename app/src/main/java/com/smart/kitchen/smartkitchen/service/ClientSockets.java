package com.smart.kitchen.smartkitchen.service;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.SocketClient.SocketDelegate;

public class ClientSockets {
    private static final String HEART_RECEIVER = "{\"type\":\"ping\"}";
    private static final String HEART_SEND = "{\"type\":\"pong\"}";
    private static final int PORT = 8272;
    private static final String SERVERIP = "120.55.165.172";
    private static final String TAG = "ClientSockets";
    private SocketClient socketClient;

    public ClientSockets() {
        configParams();
    }

    public void configParams() {
        this.socketClient = new SocketClient(SERVERIP, PORT);
        this.socketClient.disableHeartBeat();
        this.socketClient.setConnectionTimeout(5000);
        this.socketClient.setHeartBeatInterval(3000);
        this.socketClient.setHeartBeatMessage(HEART_SEND, "UTF-8");
        this.socketClient.setRemoteNoReplyAliveTimeout(15000);
        this.socketClient.setCharsetName("UTF-8");
    }

    public void registerConnectListener(SocketDelegate socketDelegate) {
        this.socketClient.registerSocketDelegate(socketDelegate);
    }

    public void unregisterConnectListener(SocketDelegate socketDelegate) {
        this.socketClient.removeSocketDelegate(socketDelegate);
    }

    public void connect() {
        try {
            this.socketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        this.socketClient.disconnect();
    }

    public void sendData(String str) {
        this.socketClient.sendString(str + "\n");
    }
}
