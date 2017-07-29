package driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.vilyever.socketclient.util.SocketSplitter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class LabelUsbPrintDriver implements Contants {
    private static final int STATE_CONNECTED = 1;
    private static final int STATE_NONE = 0;
    private static LabelUsbPrintDriver mUsbPrintDriver;
    private final String TAG = "LabelUsbPrintDriver";
    private ConnectedThread mConnectedThread;
    private Handler mHandler;
    private int mState = 0;
    private UsbManager mUsbManager;

    private class ConnectedThread extends Thread {
        private UsbEndpoint mInEndpoint;
        private UsbEndpoint mOutEndpoint;
        private UsbDeviceConnection mUsbDeviceConnection;
        private UsbInterface mUsbInterface;
        private boolean stop = false;

        public ConnectedThread(UsbDeviceConnection usbDeviceConnection, UsbInterface usbInterface) {
            super("ConnectedThread");
            this.mUsbDeviceConnection = usbDeviceConnection;
            this.mUsbInterface = usbInterface;
            this.mInEndpoint = this.mUsbInterface.getEndpoint(0);
            this.mOutEndpoint = this.mUsbInterface.getEndpoint(1);
            this.stop = false;
        }

        public void run() {
            byte[] bArr = new byte[1024];
            while (!this.stop) {
                synchronized (this) {
                    this.mUsbDeviceConnection.bulkTransfer(this.mInEndpoint, bArr, bArr.length, 3000);
                }
            }
        }

        public void write(byte[] bArr) {
            int i = 0;
            int i2 = 0;
            while (i != bArr.length) {
                i = i2 + 16384 < bArr.length ? i2 + 16384 : bArr.length;
                byte[] copyOfRange = Arrays.copyOfRange(bArr, i2, i);
                this.mUsbDeviceConnection.bulkTransfer(this.mOutEndpoint, copyOfRange, copyOfRange.length, 3000);
                i2 = i;
            }
        }

        public void cancel() {
            this.stop = true;
            if (this.mUsbDeviceConnection != null) {
                if (this.mUsbInterface != null) {
                    this.mUsbDeviceConnection.releaseInterface(this.mUsbInterface);
                    this.mUsbInterface = null;
                }
                this.mUsbDeviceConnection.close();
                this.mUsbDeviceConnection = null;
            }
        }
    }

    private LabelUsbPrintDriver() {
    }

    public static LabelUsbPrintDriver getInstance() {
        if (mUsbPrintDriver == null) {
            mUsbPrintDriver = new LabelUsbPrintDriver();
        }
        return mUsbPrintDriver;
    }

    public void setUsbManager(UsbManager usbManager) {
        this.mUsbManager = usbManager;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void sendMessageToMainThread(int i) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", i);
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void sendMessageToMainThread(int i, int i2) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", i);
            bundle.putInt("state", i2);
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    private synchronized void setState(int i) {
        this.mState = i;
        switch (this.mState) {
            case 0:
                sendMessageToMainThread(32, 16);
                break;
            case 1:
                sendMessageToMainThread(32, 18);
                break;
        }
    }

    public synchronized void stop() {
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        setState(0);
    }

    public void connect(UsbDevice usbDevice) {
        if (this.mUsbManager == null) {
            throw new UsbManagerNotInitedException("the UsbManager has not been set,please invoke setUsbManager mothod to set UsbManager");
        }
        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbDeviceConnection openDevice = this.mUsbManager.openDevice(usbDevice);
        if (openDevice == null) {
            sendMessageToMainThread(33);
        } else if (openDevice.claimInterface(usbInterface, false)) {
            this.mConnectedThread = new ConnectedThread(openDevice, usbInterface);
            this.mConnectedThread.start();
            sendMessageToMainThread(34);
            setState(1);
        } else {
            openDevice.close();
            sendMessageToMainThread(33);
        }
    }

    public void USB_Write(String str) {
        byte[] bArr = null;
        if (this.mState == 1) {
            ConnectedThread connectedThread = this.mConnectedThread;
            try {
                bArr = str.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            connectedThread.write(bArr);
        }
    }

    public void USB_Write(String str, boolean z) {
        byte[] bArr = null;
        if (this.mState == 1) {
            ConnectedThread connectedThread = this.mConnectedThread;
            if (z) {
                try {
                    bArr = str.getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                }
            } else {
                bArr = str.getBytes();
            }
            connectedThread.write(bArr);
        }
    }

    public void USB_Write(byte[] bArr) {
        if (this.mState == 1) {
            this.mConnectedThread.write(bArr);
        }
    }

    public boolean IsNoConnection() {
        if (this.mState != 1) {
            return true;
        }
        return false;
    }

    public void Begin() {
        WakeUpPritner();
        InitPrinter();
    }

    public boolean InitPrinter() {
        byte[] bArr = new byte[]{(byte) 27, (byte) 64};
        if (this.mState != 1) {
            return false;
        }
        USB_Write(bArr);
        return true;
    }

    public void WakeUpPritner() {
        try {
            USB_Write(new byte[]{(byte) 0, (byte) 0, (byte) 0});
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetPrintRotate(byte b) {
        USB_Write(new byte[]{(byte) 31, (byte) 106, b});
    }

    public void SetSize(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SIZE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = " ";
        strArr[8] = "mm";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetGAP(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "GAP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = " ";
        strArr[8] = "mm";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetOFFSET(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "OFFSET";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetSPEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SPEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetDENSITY(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DENSITY";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetDIRECTION(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DIRECTION";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetREFERENCE(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "REFERENCE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetSHIFT(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SHIFT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetCODEPAGE(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "CODEPAGE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetCLS() {
        USB_Write("CLS\r\n".getBytes());
    }

    public void SetFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "FEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetBACKFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BACKFEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetFORMFEED() {
        USB_Write("FORMFEED\r\n".getBytes());
    }

    public void SetHOME() {
        USB_Write("HOME\r\n".getBytes());
    }

    public void SetPRINT(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PRINT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetSOUND(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SOUND";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetLIMITFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "LIMITFEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SelftestPrint() {
        USB_Write("SELFTEST\r\n".getBytes());
    }

    public void SetBAR(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BAR";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void drawBox(String str, String str2, String str3, String str4, String str5) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BOX";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = ",";
        strArr[10] = str5;
        strArr[11] = SocketSplitter.Splitter;
        while (i < 12) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void drawBitMap(String str, String str2, String str3, String str4, String str5, byte[] bArr) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BITMAP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = ",";
        strArr[10] = str5;
        strArr[11] = ",";
        while (i < 12) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
        USB_Write(bArr);
        USB_Write(SocketSplitter.Splitter);
    }

    public void PutBmp(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PUTBMP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void PutPcx(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PUTPCX";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void ClearData(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "ERASE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void SetReverse(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "REVERSE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void PrintText(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "TEXT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = ",";
        strArr[10] = str4;
        strArr[11] = ",";
        strArr[12] = str5;
        strArr[13] = ",";
        strArr[14] = str6;
        strArr[15] = ",";
        strArr[16] = "\"";
        strArr[17] = str7.replaceAll(" ", "\b");
        strArr[18] = "\"";
        strArr[19] = SocketSplitter.Splitter;
        while (i < 20) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void loadProFile(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DOWNLOAD";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void loadDateFile(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DOWNLOAD";
        strArr[1] = " ";
        strArr[2] = "\"";
        strArr[3] = str;
        strArr[4] = "\"";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = ",";
        strArr[8] = str3;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void endPro() {
        USB_Write("EOP\r\n".getBytes());
    }

    public void setCounter(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SET COUNTER";
        strArr[1] = " ";
        strArr[2] = "@";
        strArr[3] = str;
        strArr[4] = ",";
        strArr[5] = str2;
        strArr[6] = SocketSplitter.Splitter;
        while (i < 7) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }

    public void setDhcp(boolean z) {
        byte[] bArr = new byte[4];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 98;
        bArr[2] = (byte) 68;
        if (z) {
            bArr[3] = (byte) 1;
        } else {
            bArr[3] = (byte) 0;
        }
        USB_Write(bArr);
    }

    public void setStaticIp(String paramString1, String paramString2, String paramString3){
        String[]ss1 = paramString1.split("\\.");
        String[]ss2 = paramString2.split("\\.");
        String[]ss3 = paramString3.split("\\.");
        USB_Write(new byte[] { 31, 105, (byte)Short.parseShort(ss1[0]), (byte)Short.parseShort(ss1[1]), (byte)Short.parseShort(ss1[2]), (byte)Short.parseShort(ss1[3]), 31, 37, 0, (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), 31, 37, 1, (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]) });
    }

    private void setOpen(String str, byte b) {
        byte[] bArr = new byte[(((str.length() + 3) + 3) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 3;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i + 1;
            bArr[i] = (byte) str.charAt(i2);
            i2++;
            i = i3;
        }
        i2 = i + 1;
        bArr[i] = (byte) 0;
        i = i2 + 1;
        bArr[i2] = (byte) 0;
        i2 = i + 1;
        bArr[i] = (byte) 0;
        if (b == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        } else {
            int i4 = i2 + 1;
            bArr[i2] = (byte) 1;
        }
        USB_Write(bArr);
    }

    private void setWPA(String str, String str2, byte b, byte b2, byte b3) {
        byte[] bArr = new byte[((((str.length() + 3) + 3) + str2.length()) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 0;
        int i2 = 3;
        while (i < str.length()) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) str.charAt(i);
            i++;
            i2 = i3;
        }
        if (b2 == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 2;
            i2 = i;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 3;
            i2 = i;
        }
        if (b3 == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 1;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        }
        i2 = i + 1;
        bArr[i] = (byte) str2.length();
        i = 0;
        while (i < str2.length()) {
            int i4 = i2 + 1;
            bArr[i2] = (byte) str2.charAt(i);
            i++;
            i2 = i4;
        }
        if (b == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 1;
        }
        USB_Write(bArr);
    }

    private void setWEP(String str, String str2, byte b, byte b2) {
        byte[] bArr = new byte[((((str.length() + 3) + 3) + str2.length()) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 3;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i + 1;
            bArr[i] = (byte) str.charAt(i2);
            i2++;
            i = i3;
        }
        int i3;
        if (b2 == (byte) 0) {
            i3 = i + 1;
            bArr[i] = (byte) 0;
            i2 = i3 + 1;
            bArr[i3] = (byte) 1;
        } else {
            i3 = i + 1;
            bArr[i] = (byte) 1;
            i2 = i3 + 1;
            bArr[i3] = (byte) 0;
        }
        i = i2 + 1;
        bArr[i2] = (byte) str2.length();
        i2 = 0;
        while (i2 < str2.length()) {
            i3 = i + 1;
            bArr[i] = (byte) str2.charAt(i2);
            i2++;
            i = i3;
        }
        if (b == (byte) 0) {
            i2 = i + 1;
            bArr[i] = (byte) 0;
        } else {
            i2 = i + 1;
            bArr[i] = (byte) 1;
        }
        USB_Write(bArr);
    }

    public void setWifiParam(String str, String str2, byte b, byte b2, byte b3, byte b4, byte b5) {
        if (str != null && str.length() >= 0) {
            if (b == (byte) 0) {
                try {
                    setOpen(str, b5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((byte) 1 == b) {
                setWPA(str, str2, b5, b2, b3);
            } else {
                setWEP(str, str2, b5, b4);
            }
        }
    }

    public void CodePrint(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BARCODE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = ",";
        strArr[10] = str4;
        strArr[11] = ",";
        strArr[12] = str5;
        strArr[13] = ",";
        strArr[14] = str6;
        strArr[15] = ",";
        strArr[16] = str7;
        strArr[17] = ",";
        strArr[18] = str8;
        strArr[19] = ",";
        strArr[20] = "\"";
        strArr[21] = str9;
        strArr[22] = "\"";
        strArr[23] = SocketSplitter.Splitter;
        while (i < 24) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        USB_Write(stringBuffer.toString());
    }
}
