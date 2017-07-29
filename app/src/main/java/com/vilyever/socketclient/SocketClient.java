package com.vilyever.socketclient;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.vilyever.socketclient.util.ExceptionThrower;
import com.vilyever.socketclient.util.SocketInputReader;
import com.vilyever.socketclient.util.StringValidation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {
    public static final int DefaultConnectionTimeout = 15000;
    public static final long DefaultHeartBeatInterval = 30000;
    public static final long DefaultRemoteNoReplyAliveTimeout = 60000;
    public static final long NoneHeartBeatInterval = -1;
    public static final long NoneRemoteNoReplyAliveTimeout = -1;
    private String charsetName;
    private ConnectionThread connectionThread;
    private int connectionTimeout;
    private CountDownTimer hearBeatCountDownTimer;
    private long heartBeatInterval;
    private byte[] heartBeatMessage;
    private long lastReceiveMessageTime;
    private long lastSendHeartBeatMessageTime;
    private PollingHelper pollingHelper;
    private ReceiveThread receiveThread;
    private String remoteIP;
    private long remoteNoReplyAliveTimeout;
    private int remotePort;
    private Socket runningSocket;
    final SocketClient self;
    private SendThread sendThread;
    private ArrayList<SocketDelegate> socketDelegates;
    private ArrayList<SocketHeartBeatDelegate> socketHeartBeatDelegates;
    private ArrayList<SocketPollingDelegate> socketPollingDelegate;
    private State state;
    private boolean supportReadLine;
    private UIHandler uiHandler;

    public interface SocketDelegate {

        public static class SimpleSocketDelegate implements SocketDelegate {
            public void onConnected(SocketClient socketClient) {
            }

            public void onDisconnected(SocketClient socketClient) {
            }

            public void onResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket) {
            }
        }

        void onConnected(SocketClient socketClient);

        void onDisconnected(SocketClient socketClient);

        void onResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket);
    }

    private class ConnectionThread extends Thread {
        private ConnectionThread() {
        }

        public void run() {
            super.run();
            try {
                SocketClient.this.self.getRunningSocket().connect(new InetSocketAddress(SocketClient.this.self.getRemoteIP(), SocketClient.this.self.getRemotePort()), SocketClient.this.self.getConnectionTimeout());
                SocketClient.this.self.getUiHandler().sendEmptyMessage(UIHandler.MessageType.Connected.what());
            } catch (IOException e) {
                e.printStackTrace();
                SocketClient.this.self.disconnect();
            }
        }
    }

    private class ReceiveThread extends Thread {
        private ReceiveThread() {
        }

        public void run() {
            super.run();
            try {


            SocketInputReader socketInputReader = new SocketInputReader(SocketClient.this.self.getRunningSocket().getInputStream());
            while (SocketClient.this.self.isConnected() && !Thread.interrupted()) {
                byte[] readBytes = socketInputReader.readBytes();
                if (readBytes == null) {
                    SocketClient.this.self.disconnect();
                    return;
                }
                String str;
                try {
                    str = new String(readBytes, Charset.forName(SocketClient.this.self.getCharsetName()));
                } catch (Exception e) {

                        str = null;

                        SocketClient.this.self.disconnect();
                }
                SocketResponsePacket socketResponsePacket = new SocketResponsePacket(readBytes, str);
                Message obtain = Message.obtain();
                obtain.what = UIHandler.MessageType.ReceiveResponse.what();
                obtain.obj = socketResponsePacket;
                SocketClient.this.self.getUiHandler().sendMessage(obtain);
            }
            }catch (Exception e){}
        }
    }

    private class SendThread extends Thread {
        private final Object sendLock = new Object();
        private LinkedBlockingQueue<SocketPacket> sendingQueue;

        protected LinkedBlockingQueue<SocketPacket> getSendingQueue() {
            if (this.sendingQueue == null) {
                this.sendingQueue = new LinkedBlockingQueue();
            }
            return this.sendingQueue;
        }

        public void enqueueSocketPacket(SocketPacket socketPacket) {
            getSendingQueue().add(socketPacket);
            synchronized (this.sendLock) {
                this.sendLock.notifyAll();
            }
        }

        public void cancel(int i) {
            Iterator it = getSendingQueue().iterator();
            while (it.hasNext()) {
                if (((SocketPacket) it.next()).getID() == i) {
                    it.remove();
                    return;
                }
            }
        }

        public void run() {
            super.run();
            while (SocketClient.this.self.isConnected() && !Thread.interrupted()) {
                while (true) {
                    SocketPacket socketPacket = (SocketPacket) getSendingQueue().poll();
                    if (socketPacket == null) {
                        break;
                    }
                    byte[] bytes = new byte[1024];
                    long currentTimeMillis = System.currentTimeMillis();
                    byte[] data = socketPacket.getData();
                    if (data == null && socketPacket.getMessage() != null) {
                        try {
                            bytes = socketPacket.getMessage().getBytes(SocketClient.this.self.getCharsetName());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (bytes != null) {
                            try {
                                if (SocketClient.this.self.isSupportReadLine()) {
                                    bytes = Arrays.copyOf(bytes, bytes.length + 2);
                                    bytes[bytes.length - 2] = (byte) 13;
                                    bytes[bytes.length - 1] = (byte) 10;
                                }
                                SocketClient.this.self.getRunningSocket().getOutputStream().write(bytes);
                                SocketClient.this.self.getRunningSocket().getOutputStream().flush();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (!SocketClient.this.self.isSupportReadLine()) {
                            while (System.currentTimeMillis() - currentTimeMillis < 3) {
                                Thread.yield();
                            }
                        }
                    }
                    bytes = data;
                    if (bytes != null) {
                        if (SocketClient.this.self.isSupportReadLine()) {
                            bytes = Arrays.copyOf(bytes, bytes.length + 2);
                            bytes[bytes.length - 2] = (byte) 13;
                            bytes[bytes.length - 1] = (byte) 10;
                        }
                        try {
                            SocketClient.this.self.getRunningSocket().getOutputStream().write(bytes);
                            SocketClient.this.self.getRunningSocket().getOutputStream().flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!SocketClient.this.self.isSupportReadLine()) {
                        while (System.currentTimeMillis() - currentTimeMillis < 3) {
                            Thread.yield();
                        }
                    }
                }
                synchronized (this.sendLock) {
                    try {
                        this.sendLock.wait();
                    } catch (InterruptedException e3) {
                    }
                }
            }
        }
    }

    public interface SocketHeartBeatDelegate {

        public static class SimpleSocketHeartBeatDelegate implements SocketHeartBeatDelegate {
            public void onHeartBeat(SocketClient socketClient) {
            }
        }

        void onHeartBeat(SocketClient socketClient);
    }

    public interface SocketPollingDelegate {

        public static class SimpleSocketPollingDelegate implements SocketPollingDelegate {
            public void onPollingQuery(SocketClient socketClient, SocketResponsePacket socketResponsePacket) {
            }

            public void onPollingResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket) {
            }
        }

        void onPollingQuery(SocketClient socketClient, SocketResponsePacket socketResponsePacket);

        void onPollingResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket);
    }

    public enum State {
        Disconnected,
        Connecting,
        Connected
    }

    protected static class UIHandler extends Handler {
        private WeakReference<SocketClient> referenceSocketClient;

        public enum MessageType {
            Connected,
            Disconnected,
            ReceiveResponse;

            public static MessageType typeFromWhat(int i) {
                return values()[i];
            }

            public int what() {
                return ordinal();
            }
        }

        public UIHandler(SocketClient socketClient) {
            super(Looper.getMainLooper());
            this.referenceSocketClient = new WeakReference(socketClient);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (MessageType.typeFromWhat(message.what)) {
                case Connected:
                    ((SocketClient) this.referenceSocketClient.get()).onConnected();
                    return;
                case Disconnected:
                    ((SocketClient) this.referenceSocketClient.get()).onDisconnected();
                    return;
                case ReceiveResponse:
                    ((SocketClient) this.referenceSocketClient.get()).onReceiveResponse((SocketResponsePacket) message.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public SocketClient(String str, int i) {
        this(str, i, DefaultConnectionTimeout);
    }

    public SocketClient(String str, int i, int i2) {
        this.self = this;
        this.supportReadLine = true;
        this.remoteNoReplyAliveTimeout = DefaultRemoteNoReplyAliveTimeout;
        this.heartBeatMessage = SocketPacket.DefaultHeartBeatMessage;
        this.heartBeatInterval = DefaultHeartBeatInterval;
        setRemoteIP(str);
        setRemotePort(i);
        setConnectionTimeout(i2);
    }

    public void connect() {
        if (isDisconnected()) {
            setState(State.Connecting);
            getConnectionThread().start();
        }
    }

    public void disconnect() {
        if (!isDisconnected()) {
            if (!getRunningSocket().isClosed() || isConnecting()) {
                try {
                    getRunningSocket().getOutputStream().close();
                    getRunningSocket().getInputStream().close();
                } catch (IOException e) {
                } finally {
                    try {
                        getRunningSocket().close();
                    } catch (IOException e2) {
                    }
                    this.runningSocket = null;
                }
            }
            if (this.connectionThread != null) {
                this.connectionThread.interrupt();
                this.connectionThread = null;
            }
            if (this.sendThread != null) {
                this.sendThread.interrupt();
                this.sendThread = null;
            }
            if (this.receiveThread != null) {
                this.receiveThread.interrupt();
                this.receiveThread = null;
            }
            getUiHandler().sendEmptyMessage(UIHandler.MessageType.Disconnected.what());
        }
    }

    public SocketPacket sendBytes(byte[] bArr) {
        return send(bArr);
    }

    public SocketPacket send(byte[] bArr) {
        if (!isConnected()) {
            return null;
        }
        SocketPacket socketPacket = new SocketPacket(bArr);
        getSendThread().enqueueSocketPacket(socketPacket);
        return socketPacket;
    }

    public SocketPacket sendString(String str) {
        return send(str);
    }

    public SocketPacket send(String str) {
        if (!isConnected()) {
            return null;
        }
        SocketPacket socketPacket = new SocketPacket(str);
        getSendThread().enqueueSocketPacket(socketPacket);
        return socketPacket;
    }

    public void cancelSend(SocketPacket socketPacket) {
        cancelSend(socketPacket.getID());
    }

    public void cancelSend(int i) {
        getSendThread().cancel(i);
    }

    public boolean isConnected() {
        return getState() == State.Connected;
    }

    public boolean isDisconnected() {
        return getState() == State.Disconnected;
    }

    public boolean isConnecting() {
        return getState() == State.Connecting;
    }

    public void disableHeartBeat() {
        setHeartBeatInterval(-1);
    }

    public void disableRemoteNoReplyAliveTimeout() {
        setRemoteNoReplyAliveTimeout(-1);
    }

    public SocketClient registerSocketDelegate(SocketDelegate socketDelegate) {
        if (!getSocketDelegates().contains(socketDelegate)) {
            getSocketDelegates().add(socketDelegate);
        }
        return this;
    }

    public SocketClient removeSocketDelegate(SocketDelegate socketDelegate) {
        getSocketDelegates().remove(socketDelegate);
        return this;
    }

    public SocketClient registerSocketHeartBeatDelegate(SocketHeartBeatDelegate socketHeartBeatDelegate) {
        if (!getSocketHeartBeatDelegates().contains(socketHeartBeatDelegate)) {
            getSocketHeartBeatDelegates().add(socketHeartBeatDelegate);
        }
        return this;
    }

    public SocketClient removeSocketHeartBeatDelegate(SocketDelegate socketDelegate) {
        getSocketHeartBeatDelegates().remove(socketDelegate);
        return this;
    }

    public SocketClient registerSocketPollingDelegate(SocketPollingDelegate socketPollingDelegate) {
        if (!getSocketPollingDelegate().contains(socketPollingDelegate)) {
            getSocketPollingDelegate().add(socketPollingDelegate);
        }
        return this;
    }

    public SocketClient removeSocketPollingDelegate(SocketPollingDelegate socketPollingDelegate) {
        getSocketPollingDelegate().remove(socketPollingDelegate);
        return this;
    }

    public Socket getRunningSocket() {
        if (this.runningSocket == null) {
            this.runningSocket = new Socket();
        }
        return this.runningSocket;
    }

    protected SocketClient setRunningSocket(Socket socket) {
        this.runningSocket = socket;
        return this;
    }

    public SocketClient setRemoteIP(String str) {
        if (!StringValidation.validateRegex(str, StringValidation.RegexIP)) {
            ExceptionThrower.throwIllegalStateException("we need a correct remote IP to connect");
        }
        this.remoteIP = str;
        return this;
    }

    public String getRemoteIP() {
        return this.remoteIP;
    }

    public SocketClient setRemotePort(int i) {
        if (!StringValidation.validateRegex(String.format("%d", new Object[]{Integer.valueOf(i)}), StringValidation.RegexPort)) {
            ExceptionThrower.throwIllegalStateException("we need a correct remote port to connect");
        }
        this.remotePort = i;
        return this;
    }

    public int getRemotePort() {
        return this.remotePort;
    }

    public SocketClient setSupportReadLine(boolean z) {
        this.supportReadLine = z;
        return this;
    }

    public boolean isSupportReadLine() {
        return this.supportReadLine;
    }

    public SocketClient setCharsetName(String str) {
        this.charsetName = str;
        return this;
    }

    public String getCharsetName() {
        if (this.charsetName == null) {
            this.charsetName = "UTF-8";
        }
        return this.charsetName;
    }

    public SocketClient setConnectionTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("we need connectionTimeout > 0");
        }
        this.connectionTimeout = i;
        return this;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public SocketClient setRemoteNoReplyAliveTimeout(long j) {
        if (j < 0) {
            j = -1;
        }
        this.remoteNoReplyAliveTimeout = j;
        return this;
    }

    public long getRemoteNoReplyAliveTimeout() {
        return this.remoteNoReplyAliveTimeout;
    }

    public SocketClient setHeartBeatMessage(String str) {
        return setHeartBeatMessageString(str);
    }

    public SocketClient setHeartBeatMessageString(String str) {
        return setHeartBeatMessage(str, getCharsetName());
    }

    public SocketClient setHeartBeatMessage(String str, String str2) {
        return setHeartBeatMessageString(str, str2);
    }

    public SocketClient setHeartBeatMessageString(String str, String str2) {
        if (str != null) {
            return setHeartBeatMessage(str.getBytes(Charset.forName(str2)));
        }
        this.heartBeatMessage = null;
        return this;
    }

    public SocketClient setHeartBeatMessage(byte[] bArr) {
        return setHeartBeatMessageBytes(bArr);
    }

    public SocketClient setHeartBeatMessageBytes(byte[] bArr) {
        this.heartBeatMessage = bArr;
        return this;
    }

    public byte[] getHeartBeatMessage() {
        return this.heartBeatMessage;
    }

    public SocketClient setHeartBeatInterval(long j) {
        if (j < 0) {
            j = -1;
        }
        this.heartBeatInterval = j;
        return this;
    }

    public long getHeartBeatInterval() {
        return this.heartBeatInterval;
    }

    protected SocketClient setLastSendHeartBeatMessageTime(long j) {
        this.lastSendHeartBeatMessageTime = j;
        return this;
    }

    protected long getLastSendHeartBeatMessageTime() {
        return this.lastSendHeartBeatMessageTime;
    }

    protected SocketClient setLastReceiveMessageTime(long j) {
        this.lastReceiveMessageTime = j;
        return this;
    }

    protected long getLastReceiveMessageTime() {
        return this.lastReceiveMessageTime;
    }

    protected CountDownTimer getHearBeatCountDownTimer() {
        if (this.hearBeatCountDownTimer == null) {
            this.hearBeatCountDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
                public void onTick(long j) {
                    SocketClient.this.self.onTimeTick();
                }

                public void onFinish() {
                    SocketClient.this.self.getHearBeatCountDownTimer().start();
                }
            };
        }
        return this.hearBeatCountDownTimer;
    }

    protected SocketClient setState(State state) {
        this.state = state;
        return this;
    }

    public State getState() {
        if (this.state == null) {
            return State.Disconnected;
        }
        return this.state;
    }

    public PollingHelper getPollingHelper() {
        if (this.pollingHelper == null) {
            this.pollingHelper = new PollingHelper(getCharsetName());
        }
        return this.pollingHelper;
    }

    protected ConnectionThread getConnectionThread() {
        if (this.connectionThread == null) {
            this.connectionThread = new ConnectionThread();
        }
        return this.connectionThread;
    }

    protected SendThread getSendThread() {
        if (this.sendThread == null) {
            this.sendThread = new SendThread();
        }
        return this.sendThread;
    }

    protected ReceiveThread getReceiveThread() {
        if (this.receiveThread == null) {
            this.receiveThread = new ReceiveThread();
        }
        return this.receiveThread;
    }

    protected ArrayList<SocketDelegate> getSocketDelegates() {
        if (this.socketDelegates == null) {
            this.socketDelegates = new ArrayList();
        }
        return this.socketDelegates;
    }

    protected ArrayList<SocketHeartBeatDelegate> getSocketHeartBeatDelegates() {
        if (this.socketHeartBeatDelegates == null) {
            this.socketHeartBeatDelegates = new ArrayList();
        }
        return this.socketHeartBeatDelegates;
    }

    protected ArrayList<SocketPollingDelegate> getSocketPollingDelegate() {
        if (this.socketPollingDelegate == null) {
            this.socketPollingDelegate = new ArrayList();
        }
        return this.socketPollingDelegate;
    }

    protected UIHandler getUiHandler() {
        if (this.uiHandler == null) {
            this.uiHandler = new UIHandler(this);
        }
        return this.uiHandler;
    }

    protected void onConnected() {
        setState(State.Connected);
        getSendThread().start();
        getReceiveThread().start();
        setLastSendHeartBeatMessageTime(System.currentTimeMillis());
        setLastReceiveMessageTime(System.currentTimeMillis());
        getHearBeatCountDownTimer().start();
        ArrayList arrayList = (ArrayList) getSocketDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketDelegate) arrayList.get(i)).onConnected(this);
        }
    }

    protected void onDisconnected() {
        setState(State.Disconnected);
        getHearBeatCountDownTimer().cancel();
        ArrayList arrayList = (ArrayList) getSocketDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketDelegate) arrayList.get(i)).onDisconnected(this);
        }
    }

    protected void onReceiveResponse(SocketResponsePacket socketResponsePacket) {
        setLastReceiveMessageTime(System.currentTimeMillis());
        if (socketResponsePacket.isMatch(getHeartBeatMessage())) {
            onReceiveHeartBeat();
        } else if (getPollingHelper().containsQuery(socketResponsePacket.getData())) {
            onReceivePollingQuery(socketResponsePacket);
        } else if (getPollingHelper().containsResponse(socketResponsePacket.getData())) {
            onReceivePollingResponse(socketResponsePacket);
        } else {
            ArrayList arrayList = (ArrayList) getSocketDelegates().clone();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((SocketDelegate) arrayList.get(i)).onResponse(this, socketResponsePacket);
            }
        }
    }

    protected void onReceiveHeartBeat() {
        ArrayList arrayList = (ArrayList) getSocketHeartBeatDelegates().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketHeartBeatDelegate) arrayList.get(i)).onHeartBeat(this);
        }
    }

    protected void onReceivePollingQuery(SocketResponsePacket socketResponsePacket) {
        send(getPollingHelper().getResponse(socketResponsePacket.getData()));
        ArrayList arrayList = (ArrayList) getSocketPollingDelegate().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketPollingDelegate) arrayList.get(i)).onPollingQuery(this, socketResponsePacket);
        }
    }

    protected void onReceivePollingResponse(SocketResponsePacket socketResponsePacket) {
        ArrayList arrayList = (ArrayList) getSocketPollingDelegate().clone();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((SocketPollingDelegate) arrayList.get(i)).onPollingResponse(this, socketResponsePacket);
        }
    }

    protected void onTimeTick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (!(getHeartBeatInterval() == -1 || getHeartBeatMessage() == null || currentTimeMillis - getLastSendHeartBeatMessageTime() < getHeartBeatInterval())) {
            send(getHeartBeatMessage());
            setLastSendHeartBeatMessageTime(currentTimeMillis);
        }
        if (getRemoteNoReplyAliveTimeout() != -1 && currentTimeMillis - getLastReceiveMessageTime() >= getRemoteNoReplyAliveTimeout()) {
            disconnect();
        }
    }
}
