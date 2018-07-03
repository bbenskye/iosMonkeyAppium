package com.ett.monkey;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

import java.util.Random;

/**
 * Created by mff on 17/09/12.
 */
public class MonkeyTapEvent extends MonkeyEvent{
    private static final double WEIGHT_1 = 0.05;
    private static final double WEIGHT_2 = 0.10;
    private static final double WEIGHT_3 = 0.05;

    private static final double WEIGHT_4 = 0.10;
    private static final double WEIGHT_5 = 0.15;
    private static final double WEIGHT_6 = 0.10;

    private static final double WEIGHT_7 = 0.15;
    private static final double WEIGHT_8 = 0.15;
    private static final double WEIGHT_9 = 0.15;

    private static double max_2 = WEIGHT_1+WEIGHT_2;
    private static double max_3 = WEIGHT_1+WEIGHT_2+WEIGHT_3;
    private static double max_4 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4;
    private static double max_5 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4+WEIGHT_5;
    private static double max_6 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4+WEIGHT_5+WEIGHT_6;
    private static double max_7 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4+WEIGHT_5+WEIGHT_6+WEIGHT_7;
    private static double max_8 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4+WEIGHT_5+WEIGHT_6+WEIGHT_7+WEIGHT_8;
    private static double max_9 = WEIGHT_1+WEIGHT_2+WEIGHT_3+WEIGHT_4+WEIGHT_5+WEIGHT_6+WEIGHT_7+WEIGHT_8+WEIGHT_9;

    private int width, height;
    private IOSDriver driver;


    public MonkeyTapEvent(IOSDriver driver, int width, int height) {
        super(MonkeyEvent.EVENT_TYPE_TAP);
        this.width = width;
        this.height = height;
        this.driver = driver;
    }

    public int injectEvent() throws Exception {
        double x = Math.ceil(Math.random() * (width - 1));
        double y = Math.ceil(Math.random() * (height - 1));
        System.out.println("sending Tap Event : Tap->(" + x + ", " + y + ")");
        TouchAction action = new TouchAction(driver);
        action.tap((int)x,  (int)y).perform();
        //driver.touchAsync("tap", jSONObject);
        return MonkeyEvent.INJECT_SUCCESS;
    }

    public int injectEventWithRegion(){
        int max_X = width;
        int max_Y = height;
        int min_X = 0, min_Y = 0;
        switch (getRandomRegion()){
            case 1:
                min_X = 0;
                max_X = width / 3;
                min_Y = 0;
                max_Y = height / 3;
                break;
            case 2:
                min_X = width / 3;
                max_X = width / 3 * 2;
                min_Y = 0;
                max_Y = height / 3;
                break;
            case 3:
                min_X = width / 3 * 2;
                max_X = width;
                min_Y = 0;
                max_Y = height / 3;
                break;
            case 4:
                min_X = 0;
                max_X = width / 3;
                min_Y = height / 3;
                max_Y = height / 3 * 2;
                break;
            case 5:
                min_X = width / 3;
                max_X = width / 3 * 2;
                min_Y = height / 3;
                max_Y = height / 3 * 2;
                break;
            case 6:
                min_X = width / 3 * 2;
                max_X = width;
                min_Y = height / 3;
                max_Y = height / 3 * 2;
                break;
            case 7:
                min_X = 0;
                max_X = width / 3;
                min_Y = height / 3 * 2;
                max_Y = height;
                break;
            case 8:
                min_X = width / 3;
                max_X = width / 3 * 2;
                min_Y = height / 3 * 2;
                max_Y = height;
                break;
            case 9:
                min_X = width / 3 * 2;
                max_X = width;
                min_Y = height / 3 * 2;
                max_Y = height;
                break;
        }
        Random random = new Random();
        int x = random.nextInt(max_X) % (max_X - min_X + 1) + min_X;
        int y = random.nextInt(max_Y) % (max_Y - min_Y + 1) + min_Y;
        System.out.println("sending Tap Event : Tap->(" + x + ", " + y + ")");
        TouchAction action = new TouchAction(driver);
        action.tap(x, y).perform();
        //driver.touchAsync("tap", jSONObject);
        return MonkeyEvent.INJECT_SUCCESS;
    }

    private static int getRandomRegion(){
        double random = Math.random();
        if (random>=0 && random <=WEIGHT_1){
            return 1;
        }else if (random<=max_2){
            return 2;
        }else if (random<=max_3){
            return 3;
        }else if (random<=max_4){
            return 4;
        }else if (random<=max_5){
            return 5;
        }else if (random<=max_6){
            return 6;
        }else if (random<=max_7){
            return 7;
        }else if (random<=max_8){
            return 8;
        }else{
            return 9;
        }
    }

}
