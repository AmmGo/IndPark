package com.hl.indpark.entities.events;


import java.util.List;

/**
 * Created by yjl on 2021/3/8 17:13
 * Function：Hazard sources
 * Desc：危险源报警
 */
public class HSAlarmEvent {
    /**
     * key : 667
     * value : [{"num":268,"type":1},{"num":259,"type":3},{"num":86,"type":2},{"num":54,"type":4}]
     */
    public String key;
    public List<ValueBean> value;
    public static class ValueBean {
        /**
         * num : 268
         * type : 1
         */
        public int num;
        public int type;
    }
}


