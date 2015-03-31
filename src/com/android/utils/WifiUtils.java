package com.android.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class WifiUtils {

private static final String TAG = "WifiUtils";

public static String getMacFromArpCache(String ip) {
    if (ip == null)
        return null;
    BufferedReader br = null;
    try {
        br = new BufferedReader(new FileReader("/proc/net/arp"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(" +");
            if (splitted != null && splitted.length >= 4 && ip.equals(splitted[0])) {
                // Basic sanity check
                String mac = splitted[3];
                if (mac.matches("..:..:..:..:..:..")) {
                    return mac;
                } else {
                    return null;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return null;
}

public static String getClientMacFromArpCache() {
    BufferedReader br = null;
    try {
        br = new BufferedReader(new FileReader("/proc/net/arp"));
        String line;
        Log.i(TAG, "line");
        if ((line = br.readLine()) != null) {
        	line = br.readLine();
        	Log.i(TAG, line);
            String[] splitted = line.split(" +");
            if (splitted != null && splitted.length >= 4) {
                // Basic sanity check
                String mac = splitted[3];
                Log.i(TAG, mac);
                if (mac.matches("..:..:..:..:..:..")) {
                    return mac;
                } else {
                    return null;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return null;
}

//this is for client side
public static String getServerIpFromArpCache() {
    BufferedReader br = null;
    try {
        br = new BufferedReader(new FileReader("/proc/net/arp"));
        String line;
        if ((line = br.readLine()) != null) {
        	line = br.readLine();
            String[] splitted = line.split(" +");
                // Basic sanity check
                String ip = splitted[0];
            	return ip;
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return null;
}

}
