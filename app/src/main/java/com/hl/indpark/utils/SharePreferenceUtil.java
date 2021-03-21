package com.hl.indpark.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hl.indpark.App;
import com.hl.indpark.entities.LoginResultEntity;
import java.util.List;
/**
 * @method
 * @description
 * @date: 2021/3/5 14:53
 * @author: yjl
 * @param
 * @return
 */
public class SharePreferenceUtil {

   private static final String KEY_SEARCH_LIST = "search_list";
   private static final String KEY_USER = "user";

   private static final String KEY_SETTING_NO_IMAGE = "no_image";
   private static final String KEY_SETTING_DARK_STYLE = "dark_style";

   private static SharedPreferences getPreferences() {
       return PreferenceManager.getDefaultSharedPreferences(App.getApp());
   }

   public static void saveBooleanKeyValue(String key, Boolean value) {
       getPreferences().edit().putBoolean(key, value).apply();
   }

   public static boolean getBooleanKeyValue(String key) {
       return getPreferences().getBoolean(key, false);
   }

   public static void saveKeyValue(String key, String value) {
       getPreferences().edit().putString(key, value).apply();
   }

   public static String getKeyValue(String key) {
       return getPreferences().getString(key, "");
   }

   public static void saveIntKeyValue(String key, int value) {
       getPreferences().edit().putInt(key, value).apply();
   }

   public static int getIntKeyValue(String key, int defVal) {
       return getPreferences().getInt(key, defVal);
   }

   public static void saveSearchList(List<String> searchList) {
       getPreferences().edit().putString(KEY_SEARCH_LIST, new Gson().toJson(searchList)).apply();
   }

   //todo 改成数据库存储
   public static List<String> getSearchList() {
       return new Gson().fromJson(getPreferences().getString(KEY_SEARCH_LIST, "[]"), new TypeToken<List<String>>() {
       }.getType());
   }

   public static void saveUser(LoginResultEntity loginResultEntity) {
       if (loginResultEntity == null) {
           getPreferences().edit().putString(KEY_USER, "").apply();
           return;
       }
       getPreferences().edit().putString(KEY_USER, new Gson().toJson(loginResultEntity)).apply();
   }
    public static void clearAllValue(Context context){
        SharedPreferences sharedData =  context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedData.edit();
        editor.clear();
        editor.commit();
    }

   public static LoginResultEntity getUser() {
       return new Gson().fromJson(getPreferences().getString(KEY_USER, null), LoginResultEntity.class);
   }

   public static void saveNoImage(boolean isChecked) {
       saveBooleanKeyValue(KEY_SETTING_NO_IMAGE, isChecked);
   }

   public static boolean isNoImage() {
       return getBooleanKeyValue(KEY_SETTING_NO_IMAGE);
   }

   public static void changeDarkStyle(boolean isChecked) {
       saveBooleanKeyValue(KEY_SETTING_DARK_STYLE, isChecked);
   }

   public static boolean isDarkStyle() {
       return getBooleanKeyValue(KEY_SETTING_DARK_STYLE);
   }
}
