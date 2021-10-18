package com.hl.indpark.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hl.indpark.App;
import com.hl.indpark.BuildConfig;
import com.hl.indpark.uis.activities.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


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

    public static String getUserId() {
        String userId = SharePreferenceUtil.getKeyValue("userId");
        return userId;
    }

    public static boolean getIsRoleld() {
        boolean isTrue = false;
        String userId = SharePreferenceUtil.getKeyValue("roleId");
        if (userId != null && userId.equals("1")) {
            isTrue = true;
        }
        return isTrue;
    }

    public static String getEnterpriseId() {
        String userId = SharePreferenceUtil.getKeyValue("enterpriseId");
        return userId;
    }

    public static String getEnterpriseName() {
        String userId = SharePreferenceUtil.getKeyValue("enterpriseName");
        return userId;
    }

    public static void login(String code, Activity activity) {

        if (code.equals("10015")) {
            showToast("登录过期，请重新登录", activity);
            activity.finish();
            SharePreferenceUtil.clearAllValue(activity);
            App.closeAllActivityByMap();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }else if (code.equals("10016")){
            showToast("你的账号已在别处登录", activity);
            activity.finish();
            SharePreferenceUtil.clearAllValue(activity);
            App.closeAllActivityByMap();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }

    }

    private static Toast toast = null;

    public static void showToast(String msg, Activity activity) {
        if (toast != null) {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static int getRandomColor(Random random) {
        return colors[random.nextInt(colors.length)];
    }

    /**
     * String数组转Set
     *
     * @param values
     * @return
     */
    public static Set<String> array2Set(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    /**
     * 以“,”隔开的字符串转数组
     *
     * @param values
     * @return
     */
    public static String[] string2Array(String values) {
        if (!TextUtils.isEmpty(values)) {
            return values.split(",");
        }
        return new String[0];
    }

    public static void hideInputManager(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  //强制隐藏
        }
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
