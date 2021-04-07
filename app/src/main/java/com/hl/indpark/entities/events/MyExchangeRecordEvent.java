package com.hl.indpark.entities.events;

import java.util.List;

public class MyExchangeRecordEvent {
    /**
     * records : [{"createId":1,"createTime":"2021-04-06 16:43:54","expirationTime":"2021-04-07 16:43:45","id":1,"isDelete":2,"productId":1,"state":1,"updateId":1,"updateTime":"2021-04-06 16:43:54"}]
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
         * createId : 1
         * createTime : 2021-04-06 16:43:54
         * expirationTime : 2021-04-07 16:43:45
         * id : 1
         * isDelete : 2
         * productId : 1
         * state : 1
         * updateId : 1
         * updateTime : 2021-04-06 16:43:54
         */
        public int createId;
        //时间
        public String createTime;
        //name
        public String product;
        //num
        public String credit;

        public String expirationTime;
        public int id;
        public int isDelete;
        public int productId;
        public int state;
        public int updateId;
        public String updateTime;
    }
}
