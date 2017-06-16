package com.vilyever.socketclient.server;

import com.vilyever.socketclient.SocketClient;
import java.net.Socket;

public class SocketServerClient extends SocketClient {
    final SocketServerClient self = this;

    public SocketServerClient(Socket socket) {
        super(socket.getLocalAddress().toString().substring(1), socket.getLocalPort());
        setRunningSocket(socket);
        getUiHandler().sendEmptyMessage(UIHandler.MessageType.Connected.what());
    }
}
