package com.ett.monkey;

import com.ett.monkey.util.DeviceUtil;
import com.ett.monkey.util.FileUtil;
import com.ett.monkey.util.Shell;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mff on 17/09/12.
 */

public class Monkey {

    private static Monkey monkey;
    private IOSDriver driver;
    private int width, height, submitX_mim, submitX_max, submitY_mim, submitY_max, contentX_mim, contentX_max, contentY_mim, contentY_max, special_point_x, special_point_y;
    private int backX_min, backX_max, backY_min, backY_max;
    private static boolean needhelp = false;
    private static String UDID;
    //    private static String BUNDLEID = "com.vipkid.vipkidParent";
//    private static String BUNDLEID = "com.vipkid.vipkidParentDev";
    private static String BUNDLEID = "com.vipkid.app-study-iPad";
    private static String PORT = "4723";
    private static String PROXYPORT = "8100";
    private int backX = 25, backY = 40;
    private int eventcount = 0;
    private int countMax = 0;
    private long startTime = 0;
    private double timeMax = 0; //单位小时
    private String configFile = null;

    private int tapCount, swipeCount, backCount, errorCount;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> ids = DeviceUtil.getDevices();
        UDID = ids.size() > 0 ? ids.get(0) : "";
//        UDID = "0f39c30fbc3695e5d99871c2b60f315e023eec97";
        monkey = new Monkey();
        monkey.executeParameter(args);

    }


    private void executeParameter(String[] args) {
        int optSetting = 0;

        for (; optSetting < args.length; optSetting++) {
            if ("-u".equals(args[optSetting])) {
                UDID = args[++optSetting];
            } else if ("-b".equals(args[optSetting])) {
                BUNDLEID = args[++optSetting];
            } else if ("-e".equals(args[optSetting])) {
                countMax = Integer.parseInt(args[++optSetting]);
            } else if ("-t".equals(args[optSetting])) {
                timeMax = Double.parseDouble(args[++optSetting]);
            } else if ("-c".equals(args[optSetting])) {
                configFile = args[++optSetting];
            } else if ("-port".equals(args[optSetting])) {
                PORT = args[++optSetting];
            } else if ("-proxyport".equals(args[optSetting])) {
                PROXYPORT = args[++optSetting];
            } else if ("-h".equals(args[optSetting])) {
                needhelp = true;
                System.out.println(
                        "-u:设备的UDID\n" +
                                "-b:测试App的Bundle\n" +
                                "-e:注入事件次数\n" +
                                "-t:设定monkey运行时长\n" +
                                "-port:macaca服务的端口，默认4723\n" +
                                "-proxyport:usb代理端口，默认8100");
                break;
            }

        }
        if (configFile != null) {
            File file = new File(System.getProperty("user.dir") + "/" + configFile);
            //读取文件并参数赋值
            getConfigValue(file);
        }
        if (!needhelp) {
            if (countMax == 0 && timeMax == 0) {
                System.out.println("请确认事件和时长至少配置一个,需要帮助请输入 java -jar iosMonkey.jar -h");
                return;
            }
            try {
                System.out.println("测试设备:" + UDID
                        + "\n测试App:" + BUNDLEID
                        + "\n测试事件:" + countMax
                        + "\n测试时长（小时）:" + timeMax);
                org.testng.Assert.assertTrue((!UDID.equals(null)) && (!BUNDLEID.equals(null)));
            } catch (Exception e) {
                System.out.println("请确认参数配置,需要帮助请输入 java -jar iosMonkey.jar -h\n"
                        + "ERROR信息" + e.toString());
            }
            try {
                monkey.run();
            } catch (Exception e) {
                errorCount++;
                e.printStackTrace();
                System.out.println("ERROR FOUND: \n" + "deviceName: " + DeviceUtil.getName(UDID)
                        + " version: " + DeviceUtil.getVersion(UDID) + " time: " + String.format("%1$tY-%1$tm-%1$td %1$tT", new Date())
                        + "\nappInfo: " + DeviceUtil.getAppVersion(UDID, BUNDLEID));
                monkey.executeParameter(new String[]{});

            }
        }
    }


    public void run() throws Exception {
        if (driver == null) {
            init();
        }
        width = driver.manage().window().getSize().getWidth();
        height = driver.manage().window().getSize().getHeight();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        submitX_max = width - 1;
        submitX_mim = width / 10;
        submitY_max = height - 1;
        submitY_mim = height / 10 * 9;

        backX_max = width - 1;
        backX_min = width / 5 * 4;
        backY_max = height / 2;
        backY_min = 1;

        contentX_max = width - width / 10;
        contentX_mim = width / 10;
        contentY_max = height / 2 + height / 10;
        contentY_mim = height / 2 - height / 10;
        special_point_x = width / 2;
        special_point_y = (int) (height * 0.94);

        while (true) {
            if (eventcount == 0) {
                startTime = System.currentTimeMillis();
            }
            long currentTime = System.currentTimeMillis();
            if ((countMax != 0 && eventcount >= countMax) || (timeMax != 0 && (currentTime - startTime) >= (timeMax * 3600 * 1000))) {

                List<String> infos = FileUtil.copyCrashReport("/Users/vipkid/workspace/crashreport", BUNDLEID);
                System.out.println("crash count = " + infos.size());
                for (String str : infos) {
                    System.out.println(str);
                }
                System.out.println("执行完毕，请重新启动再次运行~~~~");
                System.out.println("随机点击：" + tapCount + "次");
                System.out.println("随机拖动：" + swipeCount + "次");
                System.out.println("返回事件：" + backCount + "次");
                System.out.println("出现错误：" + infos.size() + "次");
                break;
            }
            Thread.sleep(100);
            switch (MathRandom.PercentageRandom()) {
                case 0: {
                    new MonkeyTapEvent(driver, width, height).injectEventWithRegion();
                    eventcount = eventcount + 1;
                    tapCount++;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 1: {
                    new MonkeySwipeEvent(driver, width, height).injectEvent();
                    eventcount = eventcount + 1;
                    swipeCount++;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 2: {
                    new MonkeyBackEvent(driver, backX, backY).injectEvent();
                    eventcount = eventcount + 1;
                    backCount++;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 7: {
                    new MonkeyBackRegionEvent(driver, backX_min, backX_max, backY_min, backY_max).injectEvent();
                    eventcount = eventcount + 1;
                    backCount++;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 3: {
                    new MonkeySubmitEvent(driver, submitX_mim, submitX_max, submitY_mim, submitY_max).injectEvent();
                    eventcount = eventcount + 1;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 4: {
                    new MonkeyContentEvent(driver, contentX_mim, contentX_max, contentY_mim, contentY_max).injectEvent();
                    eventcount = eventcount + 1;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 5: {
                    new MonkeyTapSpecialPointEvent(driver, special_point_x, special_point_y).injectEvent();
                    eventcount = eventcount + 1;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
                case 6: {
                    new MonkeyHomeKeyEvent(driver, UDID, BUNDLEID).injectEvent();
                    eventcount = eventcount + 1;
//                    System.out.println("---EVENT执行了：" + eventcount + "次---");
                    break;
                }
            }

            System.out.println("---EVENT执行了：" + eventcount + "次---已运行了（h）：" + String.format("%.4f", (currentTime - startTime) / 1000.0 / 3600));
        }
    }

    private void init() throws IOException, InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "iOS");
        caps.setCapability("platformVersion", "10.1");
        caps.setCapability("deviceName", "ipad mini4");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("autoAcceptAlerts", "true");
        caps.setCapability("noReset", "true");
        caps.setCapability("udid", UDID);
        caps.setCapability("bundleId", BUNDLEID);
// 重装app时使用，一般不用
//        final File classpathRoot = new File(System.getProperty("user.dir"));
//        final File appDir = new File(classpathRoot, "src/test/resource/");
//        final File app = new File(appDir, "ettAiXuePaiNextGen.app");
        // caps.setCapability("app", app.getAbsolutePath());
        try {
            System.out.println("应用启动了~~~");
            URL remoteUrl = new URL("http://127.0.0.1:" + PORT + "/wd/hub");
            driver = new IOSDriver(remoteUrl, caps);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("*******************************************\n\n\n" +
                    "请在命令行输入 appium 启动服务\n\n\n" +
                    "*******************************************\n");
        }
        //启动app守护进程
        Shell.launchAPP(UDID, BUNDLEID);
    }

    private void getConfigValue(File file) {
        List<String> lines = FileUtil.readFileByLines(file);
        for (String s : lines) {
            System.out.println(s);
        }
    }
}
