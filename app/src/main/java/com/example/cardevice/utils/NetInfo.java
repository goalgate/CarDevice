package com.example.cardevice.utils;

import android.os.Build;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017-07-13.
 */

public class NetInfo {
    //取有线网卡MAC
    public String getMac() {
        return getMacBelow("eth0");
    }

    //取无线网卡MAC
    public String getWifiMac() {
        return getMacBelow("wlan0");
    }

    public String getMacBelow(String name) {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/" + name + "/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }


    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }


    //取macID
    public String getMacId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            return macToId(getWifiMac());
        }else{
            return macToId(getMachineHardwareAddress());
        }
    }


    public String macToId(String mac) {
        String s = "";
        if (mac == null) {
            return "";
        }
        String[] ss = mac.split(":");
        if (ss.length < 6) {
            return "";
        }
        try {
            for (int i = 0; i < 6; i++) {
                int b = Integer.parseInt(ss[i].trim(), 16);
                s += formatStr(String.valueOf(b), 3);
                if (i == 1 || i == 3) {
                    s += "-";
                }
            }

        } catch (Exception ex) {
            s = "";
        }
        return s;
    }

    public String formatStr(String str, int len) {
        String s = "";
        if (str.length() == len) {
            s = str;
        } else if (str.length() < len) {
            for (int i = str.length(); i < len; i++) {
                s = '0' + s;
            }
            s = s + str;
        } else if (str.length() > len) {
            s = str.substring(str.length() - len);

        }
        return s;
    }


    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

}
