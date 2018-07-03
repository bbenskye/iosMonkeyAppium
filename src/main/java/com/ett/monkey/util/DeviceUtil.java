package com.ett.monkey.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceUtil {
    private static final String DEVICE_ID = "idevice_id --list";
    private static final String DEVICE_NAME = "/usr/local/bin/ideviceinfo -u %s -k DeviceName";
    private static final String DEVICE_VERSION = "/usr/local/bin/ideviceinfo -u %s -k ProductVersion";
    private static final String APP_VERSION = "/usr/local/bin/ideviceinstaller -u %s -l";

    public static List<String> getDevices(){
        List<String> udids = new ArrayList<String>();
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(DEVICE_ID).getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                udids.add(line.trim());
            }
            System.out.println("udids count = " + udids.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return udids;
    }
    public static String getAppVersion(String udid, String bundleId){
        String command = String.format(APP_VERSION, udid);

        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(command).getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(bundleId)){
                    return line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getName(String udid){
        String command = String.format(DEVICE_NAME, udid);
//        System.out.println(command);
        return run(command).trim();
    }

    public static String getVersion(String udid){
        String command = String.format(DEVICE_VERSION, udid);
//        System.out.println(command);
        return run(command).trim();
    }
    public static String run(String command) {
        Runtime runtime = Runtime.getRuntime();
        StringBuffer b = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(command).getInputStream()));
            //StringBuffer b = new StringBuffer();
            String line = null;

            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b.toString();
    }

    public static void main(String[] args) {
//        System.out.println(getAppVersion("221c0b31b7bb765dc682b8b2c197e2233c9f50e0", "com.vipkid.app-study-iPadTest"));
        System.out.println(String.format("%.2f", 1234567/100000.0));
//        DeviceUtil.run("idevice_id --list");
//        getDevices();
//        String name = getName("362fcd6ac4aad943a7e7f41341f2f9b18d484bf9");
//        String version = getVersion("362fcd6ac4aad943a7e7f41341f2f9b18d484bf9");
//        for (int i =0; i<11;i++) {
//            System.out.println(String.format("%1$tY-%1$tm-%1$td %1$tT", new Date()));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
