package com.hl.indpark.entities.events;


import java.util.List;

/**
 * Created by yjl on 2021/3/8 17:13
 * Function：Hazard sources 报警类型；1：高高报；2：高报；3：低报；4：低低报
 * Desc：危险源报警
 */
public class HSAlarmEvent {
    /**
     * key : 667
     * value : [{"num":268,"type":1},{"num":259,"type":3},{"num":86,"type":2},{"num":54,"type":4}]
     */
    public String name;
    public List<ValueBean> value;

    public static class ValueBean {
        /**
         * num : 268
         * type : 1
         */
        public int num;
        public int type;

    }
    public static String getType(int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "高高报";
                break;
            case 2:
                typeStr = "高报";
                break;
            case 3:
                typeStr = "低报";
                break;
            case 4:
                typeStr = "低低报";
                break;
            default:
        }
        return typeStr;
    }
}


