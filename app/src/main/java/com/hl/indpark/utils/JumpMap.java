package com.hl.indpark.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;

import net.arvin.baselib.utils.ToastUtil;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @author 导航
 * @time 2019/1/4 14:54
 */
public class JumpMap {
    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     */
    public static LatLng GCJ2BD(LatLng bd) {
        double x = bd.longitude, y = bd.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new LatLng(tempLat, tempLon);
    }

    public static void openBaiduMap(Activity activity, LatLng bd) {
        if (isAvilible(activity, "com.baidu.BaiduMap")) {
            try {
                LatLng bdNew = GCJ2BD(bd);
                Intent intent = Intent.getIntent("intent://map/direction?" +
                        "destination=latlng:" + bdNew.latitude + "," + bdNew.longitude + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                activity.startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {
            ToastUtil.showToast(activity, "您尚未安装百度地图");
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }
    }

    public static void openGaodeMap(Activity activity, LatLng bd) {
        if (isAvilible(activity, "com.autonavi.minimap")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("amapuri://route/plan/?did=BGVIS2&dlat=" + bd.latitude + "&dlon=" + bd.longitude + "&dname=我的目的地&dev=0&t=0");
            intent.setData(uri);

            activity.startActivity(intent);
        } else {
            ToastUtil.showToast(activity, "您尚未安装高德地图");
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
