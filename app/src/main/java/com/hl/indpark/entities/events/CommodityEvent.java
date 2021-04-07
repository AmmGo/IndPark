package com.hl.indpark.entities.events;

import java.util.List;

public class CommodityEvent {

    /**
     * records : [{"createId":1,"createTime":"2021-04-06 15:12:19","credit":10,"id":3,"inventory":1,"isDelete":2,"name":"晚餐券","updateId":1,"updateTime":"2021-04-06 15:12:19","image":null},{"createId":1,"createTime":"2021-04-06 15:11:59","credit":7,"id":2,"inventory":4,"isDelete":2,"name":"早餐券","updateId":1,"updateTime":"2021-04-06 15:11:59","image":null},{"createId":1,"createTime":"2021-04-06 15:11:24","credit":50,"id":1,"inventory":5,"isDelete":2,"name":"午餐券","updateId":1,"updateTime":"2021-04-06 15:11:24","image":null}]
     * total : 3
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
         * createTime : 2021-04-06 15:12:19
         * credit : 10
         * id : 3
         * inventory : 1
         * isDelete : 2
         * name : 晚餐券
         * updateId : 1
         * updateTime : 2021-04-06 15:12:19
         * image : null
         */

        public int createId;
        public String createTime;
        public int credit;
        public int id;
        public int inventory;
        public int isDelete;
        public String name;
        public int updateId;
        public String updateTime;
        public String image;

    }
}
