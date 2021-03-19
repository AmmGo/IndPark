package com.hl.indpark.entities.events;

import java.util.List;

public class EntNewEp {


    /**
     * name : 宁夏华御化工有限公司
     * value : [{"address":null,"createId":1,"createTime":"2020-12-21 19:44:19","description":null,"enterpriseId":283,"enterpriseName":"宁夏华御化工有限公司","id":40,"isDelete":2,"latitude":37.658333,"longitude":105.206667,"name":"废气排放口","project":null,"psCode":"640500000077","type":2,"updateId":null,"updateTime":"2021-02-08 15:01:47","likeName":null,"iocode":"7"},{"address":null,"createId":1,"createTime":"2020-12-21 19:44:19","description":null,"enterpriseId":283,"enterpriseName":"宁夏华御化工有限公司","id":5,"isDelete":2,"latitude":37.651111,"longitude":105.188056,"name":"废水排污口","project":null,"psCode":"640500000077","type":1,"updateId":null,"updateTime":"2021-01-28 15:44:26","likeName":null,"iocode":"1"},{"address":null,"createId":1,"createTime":"2020-12-21 19:44:19","description":null,"enterpriseId":283,"enterpriseName":"宁夏华御化工有限公司","id":14,"isDelete":2,"latitude":37.591944,"longitude":105.236944,"name":"废水排放口","project":null,"psCode":"640500000077","type":1,"updateId":null,"updateTime":"2021-01-28 15:44:26","likeName":null,"iocode":"2"}]
     */

    public String name;
    public List<ValueBean> value;

    public static class ValueBean {
        /**
         * address : null
         * createId : 1
         * createTime : 2020-12-21 19:44:19
         * description : null
         * enterpriseId : 283
         * enterpriseName : 宁夏华御化工有限公司
         * id : 40
         * isDelete : 2
         * latitude : 37.658333
         * longitude : 105.206667
         * name : 废气排放口
         * project : null
         * psCode : 640500000077
         * type : 2
         * updateId : null
         * updateTime : 2021-02-08 15:01:47
         * likeName : null
         * iocode : 7
         */

        public Object address;
        public int createId;
        public String createTime;
        public Object description;
        public int enterpriseId;
        public String enterpriseName;
        public int id;
        public int isDelete;
        public double latitude;
        public double longitude;
        public String name;
        public Object project;
        public String psCode;
        public int type;
        public Object updateId;
        public String updateTime;
        public Object likeName;
        public String iocode;

    }
}
