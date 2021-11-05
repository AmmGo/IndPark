package com.hl.indpark.entities.new2;

import java.util.List;

public class SbEvent {

    /**
     * records : [{"id":4,"name":"小区外三条路路灯全熄了","eventType":1,"level":1,"happenedTime":"2021-10-26 14:59:34","handleOpinions":"已处理","enterpriseId":1,"handlePersonId":7805,"phone":"18395210456","address":null,"longitude":"105.23657","latitude":"37.62143","detail":"为何这里的路灯几个月都迟迟没有恢复照明？","enclosure":null,"sceneImages":"","status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":3,"updateId":5,"createTime":"2021-10-26 14:59:47","updateTime":"2021-10-28 16:21:57","code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":false,"isAssign":false,"isEnd":false,"isUpdate":false,"createName":"许蕊泊"}]
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

    public int total;
    public int size;
    public int current;
    public boolean optimizeCountSql;
    public boolean hitCount;
    public String countId;
    public String maxLimit;
    public boolean searchCount;
    public int pages;
    public List<RecordsBean> records;
    public List<?> orders;


    public static class RecordsBean {
        /**
         * id : 4
         * name : 小区外三条路路灯全熄了
         * eventType : 1
         * level : 1
         * happenedTime : 2021-10-26 14:59:34
         * handleOpinions : 已处理
         * enterpriseId : 1
         * handlePersonId : 7805
         * phone : 18395210456
         * address : null
         * longitude : 105.23657
         * latitude : 37.62143
         * detail : 为何这里的路灯几个月都迟迟没有恢复照明？
         * enclosure : null
         * sceneImages :
         * status : null
         * reportingDepartment : null
         * employee : null
         * personnelHanding : null
         * handlingOpinions : null
         * creatorId : 3
         * updateId : 5
         * createTime : 2021-10-26 14:59:47
         * updateTime : 2021-10-28 16:21:57
         * code : null
         * assignId : null
         * assignName : null
         * likeName : null
         * dateLike : null
         * type : null
         * isReport : false
         * isAssign : false
         * isEnd : false
         * isUpdate : false
         * createName : 许蕊泊
         */

        public int id;
        public String name;
        public int eventType;
        public int level;
        public String happenedTime;
        public String handleOpinions;
        public int enterpriseId;
        public int handlePersonId;
        public String phone;
        public String address;
        public String longitude;
        public String latitude;
        public String detail;
        public String enclosure;
        public String sceneImages;
        public String status;
        public String reportingDepartment;
        public String employee;
        public String personnelHanding;
        public String handlingOpinions;
        public int creatorId;
        public int updateId;
        public String createTime;
        public String updateTime;
        public String code;
        public String assignId;
        public String assignName;
        public String likeName;
        public String dateLike;
        public String type;
        public boolean isReport;
        public boolean isAssign;
        public boolean isEnd;
        public boolean isUpdate;
        public String createName;

    }
}
