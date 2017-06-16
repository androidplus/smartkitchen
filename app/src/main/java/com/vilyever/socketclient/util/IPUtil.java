package com.vilyever.socketclient.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class IPUtil {
    final IPUtil self = this;

    public static String getIPAddress(boolean z) {
        try {
            for (NetworkInterface inetAddresses : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetAddress : Collections.list(inetAddresses.getInetAddresses())) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        Object obj = hostAddress.indexOf(58) < 0 ? 1 : null;
                        if (z) {
                            if (obj != null) {
                                return hostAddress;
                            }
                        } else if (obj == null) {
                            int indexOf = hostAddress.indexOf(37);
                            return indexOf < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, indexOf).toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }
}
