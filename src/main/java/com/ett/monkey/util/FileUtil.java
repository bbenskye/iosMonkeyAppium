package com.ett.monkey.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String readFileByPath(String path, String bundleId) {
        File f = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains(bundleId)) {
                    return line;
                } else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getTodayLogs(String path, final String dateStr) {
        List<String> list = new ArrayList<String>();
        File file = new File(path);
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".ips") && name.contains(dateStr);
            }
        });
        for (File f : files) {
            if (f.isDirectory()) {
                System.out.println("direct===" + f.getName());
            } else {
                list.add(f.getName());
                System.out.println(f.getName());
            }
        }
        return list;
    }

    public static List<String> getCrashInfo(String path, String dateStr, String bundleId) {
        System.out.println("path = " + path + "\ndateStr = " + dateStr + "\nbundleId = " + bundleId);

        if (!path.endsWith("/")) {
            path += "/";
        }
        List<String> infos = new ArrayList<String>();
        List<String> files = getTodayLogs(path, dateStr);
        for (String fPath : files) {
            String info = readFileByPath(path + fPath, bundleId);
            if (info != null) {
                infos.add(info);
            }
        }
        return infos;
    }

    public static List<String> copyCrashReport(String path, String bundleId) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        String absPath = f.getAbsolutePath();
        DeviceUtil.run("idevicecrashreport -k " + path);

//        String dateStr = String.format("%1$tY-%1$tm-%1$td", new Date());
        String dateStr = String.format("%1$tY-%1$tm-%1$td", new Date());
        return getCrashInfo(path, dateStr, bundleId);
    }

    public static void main(String[] args) {
        String bundleID = "com.vipkid.app-study-iPadTest";
//        String line = readFileByPath("/Users/vipkid/app-study-iPad-2017-09-14-173959.ips", bundleID);
//        System.out.println(line);
        System.out.println("-------------");
        List<String> infos = copyCrashReport("/Users/vipkid/workspace/crashreport", bundleID);
        System.out.println("crash count = "+infos.size());
        for (String str : infos) {
            System.out.println(str);
        }
    }

}
