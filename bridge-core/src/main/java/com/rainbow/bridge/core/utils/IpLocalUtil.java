package com.rainbow.bridge.core.utils;

import java.net.*;
import java.util.Enumeration;

/**
 * 
 * 获取本机ip地址
 * @author gujiachun
 *
 */
public class IpLocalUtil {
	
	//public static final String DEFAULT_IP = "127.0.0.1";
	 
    /**
     * 直接根据第一个网卡地址作为其内网ipv4地址，避免返回 127.0.0.1
     * 获取本机ip地址
     * @return
     */
    public static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        return inet4Address.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
 
    /**
     * 获取本机ip地址 此方法虚拟机有可能有问题，推荐使用getLocalIpByNetcard
     * @return
     */
    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
