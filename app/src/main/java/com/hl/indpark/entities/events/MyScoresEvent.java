package com.hl.indpark.entities.events;

import java.util.List;

public class MyScoresEvent {
    /**
     * records : [{"accumulatedIntegral":1,"createId":1,"createTime":"2021-04-06 15:08:48","credit":1,"id":1,"integralType":1,"isDelete":2,"name":"签到获得积分","type":true,"unusedIntegral":1,"updateId":null,"updateTime":"2021-04-06 15:08:48","usedIntegral":0}]
     * total : 1
     * size : 10
     * current : 1
     * orders : []
     * optimizeCountSql : true
     * hitCount : false
     * countId : null
     * maxLimit : null
     * searchCount : true
     * pages : 1
     */
    public List<RecordsBean> records;

    public static class RecordsBean {
        /**
         * accumulatedIntegral : 1
         * createId : 1
         * createTime : 2021-04-06 15:08:48
         * credit : 1
         * id : 1
         * integralType : 1
         * isDelete : 2
         * name : 签到获得积分
         * type : true
         * unusedIntegral : 1
         * updateId : null
         * updateTime : 2021-04-06 15:08:48
         * usedIntegral : 0
         */
        public int accumulatedIntegral;
        public int createId;
        //创建时间
        public String createTime;
        //积分数字
        public int credit;
        public int id;
        public int integralType;
        public int isDelete;
        //签到name
        public String name;
        //收入or支出 1or2
        public int type;
        public int unusedIntegral;
        public Object updateId;
        public String updateTime;
        public int usedIntegral;
    }
}
