package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.CameraVideoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：企业通用选择
 * Desc：
 */
public class MonitorAdapter extends BaseQuickAdapter<CameraVideoEvent, BaseViewHolder> {
    public Activity activity;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    public MonitorAdapter(List<CameraVideoEvent> list) {
        super(R.layout.item_monitor, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraVideoEvent item) {
        try {
            helper.setText(R.id.tv_name, item.value);
            helper.setText(R.id.tv_time, StringData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String StringData(){
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
//        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
//        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return str+"  星期"+mWay;
    }



}
