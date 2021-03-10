package com.hl.indpark.entities.events;

import java.util.List;

public class SelfReportEvent {

    /**
     * records : [{"eventType":"突发事件","createTime":"2020-12-18 09:52:39","reportedName":"高顺萍","phone":"17709556420","status":1},{"eventType":"突发事件","createTime":"2020-12-18 09:55:01","reportedName":"张燕","phone":"13709557820","status":1}]
     * total : 2
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
    public int total;
    public int size;
    public int current;
    public boolean optimizeCountSql;
    public boolean hitCount;
    public Object countId;
    public Object maxLimit;
    public boolean searchCount;
    public int pages;
    public List<RecordsBean> records;
    public List<?> orders;
    public static class RecordsBean {
        /**
         * eventType : 突发事件
         * createTime : 2020-12-18 09:52:39
         * reportedName : 高顺萍
         * phone : 17709556420
         * status : 1
         */
        public String eventType;
        public String createTime;
        public String reportedName;
        public String phone;
        public int status;
    }
}
