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
