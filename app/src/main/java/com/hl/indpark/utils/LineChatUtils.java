package com.hl.indpark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LineChatUtils {


    public static Map<String, String> map = new HashMap<>();

    public static Map<String, String> getHbWrw() {
        map.put("w21003", "氨氮");
        map.put("w21001", "总氮");
        map.put("w21017", "氟化物(水)");
        map.put("w21011", "总磷");
        map.put("w01018", "化学需氧量(COD)");
        map.put("w20116", "总铬");
        map.put("w20117", "六价铬");
        map.put("a34013", "烟尘");
        map.put("a21026", "二氧化硫");
        map.put("a21002", "氮氧化物");
        return map;
    }

    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM-dd";

    /* //日期转换为时间戳 */
    public long timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public String dateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public String StringX(String date, int type) {
        String xStr = "";
        if (type == 1) {
            xStr = dateToString(timeToStamp(date), dateFormat_day);
        } else if (type == 2) {
            xStr = dateToString(timeToStamp(date), dateFormat_month);
        }
        return xStr;

    }
}
