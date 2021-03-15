package com.hl.indpark.utils;

import android.graphics.Color;

import com.hl.indpark.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * Created by yjl on 2021/3/8 15:10
 * Function：
 * Desc：
 */
public class Util {
    private static Integer[] colors = {Color.parseColor("#757575"), Color.parseColor("#242524"), Color.parseColor("#49617e"),
            Color.parseColor("#965e75"), Color.parseColor("#3b9a58"), Color.parseColor("#05596e"),
            Color.parseColor("#943e4f"), Color.parseColor("#0a5d17")};

    public static int wxyDay = 1;
    public static int wxyMonthly = 2;
    public static int wxyQuarter = 3;
    public static int wxyYear = 4;
    public static int hbDay = 1;
    public static int hbMonthly = 2;
    public static int hbQuarter = 3;
    public static int hbYear = 4;

    public static int getRandomColor(Random random) {
        return colors[random.nextInt(colors.length)];
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * Md5加密
     *
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(str.getBytes());
        byte[] bytes = md.digest();

        String result = "";
        for (byte b : bytes) {
            // byte转换成16进制
            result += String.format("%02x", b);
        }
        return result;
    }
}
