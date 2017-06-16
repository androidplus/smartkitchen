package com.vilyever.socketclient.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.vilyever.socketclient.PollingHelper;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.SocketClient.SocketDelegate;
import com.vilyever.socketclient.SocketPacket;
import com.vilyever.socketclient.SocketResponsePacket;
import com.vilyever.socketclient.util.ExceptionThrower;
import com.vilyever.socketclient.util.StringValidation;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SocketServer implements SocketDelegate {
    public static final int MaxPort = 65535;
    public static final int NoPort = -1;
    private String charsetName;
    private long heartBeatInterval = SocketClient.DefaultHeartBeatInterval;
    private byte[] heartBeatMessage = SocketPacket.DefaultHeartBeatMessage;
    private ListenThread listenThread;
    private boolean listening;
    private PollingHelper pollingHelper;
    private int port = -1;
    private long remoteNoReplyAliveTimeout = SocketClient.DefaultRemoteNoReplyAliveTimeout;
    private ServerSocket runningServerSocket;
    private ArrayList<SocketServerClient> runningSocketServerClients;
    final SocketServer self = this;
    private ArrayList<SocketServerDelegate> socketServerDelegates;
    private boolean supportReadLine = true;
    private UIHandler uiHandler;

    private class ListenThread extends Thread {
        private boolean running;

        private ListenThread() {
        }

        protected ListenThread setRunning(boolean z) {
            this.running = z;
            return this;
        }

        protected boolean isRunning() {
            return this.running;
        }

        public void run() {
            super.run();
            setRunning(true);
            while (!Thread.interrupted() && SocketServer.this.self.checkServerSocketAvailable()) {
                try {
                    SocketServerClient socketServerClient = SocketServer.this.self.getSocketServerClient(SocketServer.this.self.getRunningServerSocket().accept());
                    Message obtain = Message.obtain();
                    obtain.what = UIHandler.MessageType.ClientConnected.what();
                    obtain.obj = socketServerClient;
                    SocketServer.this.self.getUiHandler().sendMessage(obtain);
                } catch (IOException e) {
                }
            }
            setRunning(false);
            Message obtain2 = Message.obtain();
            obtain2.what = UIHandler.MessageType.StopListen.what();
            SocketServer.this.self.getUiHandler().sendMessage(obtain2);
        }
    }

    public interface SocketServerDelegate {

        public static class SimpleSocketServerDelegate implements SocketServerDelegate {
            public void onServerBeginListen(SocketServer socketServer, int i) {
            }

            public void onServerStopListen(SocketServer socketServer, int i) {
            }

            public void onClientConnected(SocketServer socketServer, SocketServerClient socketServerClient) {
            }

            public void onClientDisconnected(SocketServer socketServer, SocketServerClient socketServerClient) {
            }
        }

        void onClientConnected(SocketServer socketServer, SocketServerClient socketServerClient);

        void onClientDisconnected(SocketServer socketServer, SocketServerClient socketServerClient);

        void onServerBeginListen(SocketServer socketServer, int i);

        void onServerStopListen(SocketServer socketServer, int i);
    }

    protected static class UIHandler extends Handler {
        private WeakReference<SocketServer> referenceSocketServer;

        public enum MessageType {
            StopListen,
            ClientConnected;

            public static MessageType typeFromWhat(int i) {
                return values()[i];
            }

            public int what() {
                return ordinal();
            }
        }

        public UIHandler(SocketServer socketServer) {
            super(Looper.getMainLooper());
            this.referenceSocketServer = new WeakReference(socketServer);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (MessageType.typeFromWhat(message.what)) {
                case StopListen:
                    ((SocketServer) this.referenceSocketServer.get()).onSocketServerStopListen();
                    return;
                case ClientConnected:
                    ((SocketServer) this.referenceSocketServer.get()).onSocketServerClientConnected((SocketServerClient) message.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public boolean beginListen(int i) {
        if (isListening()) {
            return false;
        }
        setPort(i);
        if (getRunningServerSocket() == null) {
            return false;
        }
        onSocketServerBeginListen();
        getListenThread().start();
        return true;
    }

    public int beginListenFromPort(int i) {
        if (isListening()) {
            return -1;
        }
        for (int i2 = i; i2 <= 65535; i2++) {
            if (beginListen(i2)) {
                return i2;
            }
        }
        return -1;
    }

    public void stopListen() {
        if (isListening()) {
            getListenThread().interrupt();
            try {
                getRunningServerSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getIP() {
        return getRunningServerSocket().getLocalSocketAddress().toString().substring(1);
    }

    public void disableHeartBeat() {
        setHeartBeatInterval(-1);
    }

    public void disableRemoteNoReplyAliveTimeout() {
        setRemoteNoReplyAliveTimeout(-1);
    }

    public SocketServer registerSocketServerDelegate(SocketServerDelegate socketServerDelegate) {
        if (!getSocketServerDelegates().contains(socketServerDelegate)) {
            getSocketServerDelegates().add(socketServerDelegate);
        }
        return this;
    }

    public SocketServer removeSocketServerDelegate(SocketServerDelegate socketServerDelegate) {
        getSocketServerDelegates().remove(socketServerDelegate);
        return this;
    }

    protected ServerSocket getRunningServerSocket() {
        if (this.runningServerSocket == null) {
            try {
                this.runningServerSocket = new ServerSocket(getPort());
            } catch (IOException e) {
            }
        }
        return this.runningServerSocket;
    }

    public int getPort() {
        return this.port;
    }

    protected SocketServer setPort(int i) {
        if (!StringValidation.validateRegex(String.format("%d", new Object[]{Integer.valueOf(i)}), StringValidation.RegexPort)) {
            ExceptionThrower.throwIllegalStateException("we need a correct remote port to listen");
        }
        if (!isListening()) {
            this.port = i;
        }
        return this;
    }

    protected SocketServer setListening(boolean z) {
        this.listening = z;
        return this;
    }

    public boolean isListening() {
        return this.listening;
    }

    protected ListenThread getListenThread() {
        if (this.listenThread == null) {
            this.listenThread = new ListenThread();
        }
        return this.listenThread;
    }

    public SocketServer setSupportReadLine(boolean z) {
        this.supportReadLine = z;
        return this;
    }

    public boolean isSupportReadLine() {
        return this.supportReadLine;
    }

    public SocketServer setCharsetName(String str) {
        this.charsetName = str;
        return this;
    }

    public String getCharsetName() {
        if (this.charsetName == null) {
            this.charsetName = "UTF-8";
        }
        return this.charsetName;
    }

    public SocketServer setHeartBeatMessage(String str) {
        return setHeartBeatMessageString(str);
    }

    public SocketServer setHeartBeatMessageString(String str) {
        return setHeartBeatMessage(str, getCharsetName());
    }

    public SocketServer setHeartBeatMessage(String str, String str2) {
        return setHeartBeatMessageString(str, str2);
    }

    public SocketServer setHeartBeatMessageString(String str, String str2) {
        if (str != null) {
            return setHeartBeatMessage(str.getBytes(Charset.forName(str2)));
        }
        this.heartBeatMessage = null;
        return this;
    }

    public SocketServer setHeartBeatMessage(byte[] bArr) {
        return setHeartBeatMessageBytes(bArr);
    }

    public SocketServer setHeartBeatMessageBytes(byte[] bArr) {
        this.heartBeatMessage = bArr;
        return this;
    }

    public byte[] getHeartBeatMessage() {
        return this.heartBeatMessage;
    }

    public SocketServer setHeartBeatInterval(long j) {
        if (j < 0) {
            j = -1;
        }
        this.heartBeatInterval = j;
        ArrayList arrayList = (ArrayList) getRunningSocketServerClients().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerClient) arrayList.get(i)).setHeartBeatInterval(j);
        }
        return this;
    }

    public long getHeartBeatInterval() {
        return this.heartBeatInterval;
    }

    public SocketServer setRemoteNoReplyAliveTimeout(long j) {
        if (j < 0) {
            j = -1;
        }
        this.remoteNoReplyAliveTimeout = j;
        ArrayList arrayList = (ArrayList) getRunningSocketServerClients().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerClient) arrayList.get(i)).setRemoteNoReplyAliveTimeout(j);
        }
        return this;
    }

    public long getRemoteNoReplyAliveTimeout() {
        return this.remoteNoReplyAliveTimeout;
    }

    public PollingHelper getPollingHelper() {
        if (this.pollingHelper == null) {
            this.pollingHelper = new PollingHelper(getCharsetName());
        }
        return this.pollingHelper;
    }

    protected ArrayList<SocketServerClient> getRunningSocketServerClients() {
        if (this.runningSocketServerClients == null) {
            this.runningSocketServerClients = new ArrayList();
        }
        return this.runningSocketServerClients;
    }

    protected ArrayList<SocketServerDelegate> getSocketServerDelegates() {
        if (this.socketServerDelegates == null) {
            this.socketServerDelegates = new ArrayList();
        }
        return this.socketServerDelegates;
    }

    protected UIHandler getUiHandler() {
        if (this.uiHandler == null) {
            this.uiHandler = new UIHandler(this);
        }
        return this.uiHandler;
    }

    public void onConnected(SocketClient socketClient) {
    }

    public void onDisconnected(SocketClient socketClient) {
        onSocketServerClientDisconnected((SocketServerClient) socketClient);
    }

    public void onResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket) {
    }

    protected SocketServerClient getSocketServerClient(Socket socket) {
        return new SocketServerClient(socket);
    }

    protected void onSocketServerBeginListen() {
        setListening(true);
        ArrayList arrayList = (ArrayList) getSocketServerDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerDelegate) arrayList.get(i)).onServerBeginListen(this, getPort());
        }
    }

    protected void onSocketServerStopListen() {
        setListening(false);
        this.listenThread = null;
        this.runningServerSocket = null;
        disconnectAllClients();
        ArrayList arrayList = (ArrayList) getSocketServerDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerDelegate) arrayList.get(i)).onServerStopListen(this, getPort());
        }
    }

    protected void onSocketServerClientConnected(SocketServerClient socketServerClient) {
        getRunningSocketServerClients().add(socketServerClient);
        socketServerClient.registerSocketDelegate(this);
        socketServerClient.setSupportReadLine(isSupportReadLine());
        socketServerClient.setCharsetName(getCharsetName());
        socketServerClient.setHeartBeatMessage(getHeartBeatMessage());
        socketServerClient.setHeartBeatInterval(getHeartBeatInterval());
        socketServerClient.setRemoteNoReplyAliveTimeout(getRemoteNoReplyAliveTimeout());
        socketServerClient.getPollingHelper().append(getPollingHelper());
        ArrayList arrayList = (ArrayList) getSocketServerDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerDelegate) arrayList.get(i)).onClientConnected(this, socketServerClient);
        }
    }

    protected void onSocketServerClientDisconnected(SocketServerClient socketServerClient) {
        this.self.getRunningSocketServerClients().remove(socketServerClient);
        ArrayList arrayList = (ArrayList) getSocketServerDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketServerDelegate) arrayList.get(i)).onClientDisconnected(this, socketServerClient);
        }
    }

    private boolean checkServerSocketAvailable() {
        return (getRunningServerSocket() == null || getRunningServerSocket().isClosed()) ? false : true;
    }

    private void disconnectAllClients() {
        while (getRunningSocketServerClients().size() > 0) {
            SocketServerClient socketServerClient = (SocketServerClient) getRunningSocketServerClients().get(0);
            getRunningSocketServerClients().remove(socketServerClient);
            socketServerClient.disconnect();
        }
    }
}
