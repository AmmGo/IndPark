package com.hl.indpark.entities.events;


/**
 * Created by yjl on 2021/3/8 17:13
 * Function：查询类型 1 当天 2 当月 3 当季度 4 当年
 * Desc：环保报警
 */
public class EPAlarmEvent {

    /**
     * gasNumber : 23
     * waterNumber : 123
     * totalNumber : 146
     */
    public int gasNumber =0;
    public int waterNumber=0;
    public String gasStr = "废气报警";
    public String waterStr = "废水报警";
    public String ePAlarmStr = "环保报警";
    public int totalNumber;
}


