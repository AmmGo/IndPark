package com.hl.indpark.entities.new2;

import java.util.List;

public class SpEvent {

    /**
     * records : [{"id":6,"name":"华御发生爆炸","eventType":4,"level":3,"happenedTime":"2021-10-28 00:00:00","handleOpinions":null,"enterpriseId":1,"handlePersonId":null,"phone":"15555545456","address":"中卫市沙坡头区","longitude":"105.23657","latitude":"37.62143","detail":"华御厂区内厂房发生爆炸，暂无人员伤亡","enclosure":null,"sceneImages":"2021/10/28/022b33c4-ac5b-4b15-ade7-09c22d59d20d.jfif","status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":2,"updateId":null,"createTime":"2021-10-28 20:50:30","updateTime":null,"code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":null,"isAssign":null,"isEnd":null,"isUpdate":null,"createName":"测试管委会"},{"id":7,"name":"测试的事件","eventType":1,"level":1,"happenedTime":"2021-10-28 21:07:55","handleOpinions":"没有处理意见","enterpriseId":1,"handlePersonId":7803,"phone":"123456","address":"123456","longitude":"123","latitude":"456","detail":"没有详情","enclosure":null,"sceneImages":null,"status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":2,"updateId":null,"createTime":"2021-10-28 21:07:55","updateTime":null,"code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":null,"isAssign":null,"isEnd":null,"isUpdate":null,"createName":"测试管委会"},{"id":8,"name":"利安隆发生爆炸","eventType":5,"level":1,"happenedTime":"2021-10-28 21:13:48","handleOpinions":null,"enterpriseId":1,"handlePersonId":null,"phone":"15565654354","address":null,"longitude":"105.237772","latitude":"37.705783","detail":"利安隆厂区发生爆炸","enclosure":null,"sceneImages":"2021/10/28/61476ceb-1804-4558-acd5-479026a479e1.jfif","status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":2,"updateId":2,"createTime":"2021-10-28 21:14:34","updateTime":"2021-10-28 21:15:14","code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":null,"isAssign":null,"isEnd":null,"isUpdate":null,"createName":"测试管委会"},{"id":10,"name":"飞洒","eventType":1,"level":1,"happenedTime":null,"handleOpinions":null,"enterpriseId":1,"handlePersonId":7004,"phone":null,"address":null,"longitude":"0.0","latitude":"0.0","detail":"撒地方","enclosure":null,"sceneImages":null,"status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":2,"updateId":null,"createTime":"2021-11-05 17:19:28","updateTime":null,"code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":null,"isAssign":null,"isEnd":null,"isUpdate":null,"createName":"测试管委会"},{"id":11,"name":"app测试","eventType":1,"level":4,"happenedTime":"2021-11-05 17:41:29","handleOpinions":null,"enterpriseId":1,"handlePersonId":7004,"phone":null,"address":null,"longitude":"0.0","latitude":"0.0","detail":"app测试","enclosure":null,"sceneImages":"2021/11/05/c7d4ce70-e899-4472-a421-667a951ee7ca.jpg","status":null,"reportingDepartment":null,"employee":null,"personnelHanding":null,"handlingOpinions":null,"creatorId":2,"updateId":null,"createTime":"2021-11-05 17:42:02","updateTime":null,"code":null,"assignId":null,"assignName":null,"likeName":null,"dateLike":null,"type":null,"isReport":null,"isAssign":null,"isEnd":null,"isUpdate":null,"createName":"测试管委会"}]
     * total : 5
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
         * id : 6
         * name : 华御发生爆炸
         * eventType : 4
         * level : 3
         * happenedTime : 2021-10-28 00:00:00
         * handleOpinions : null
         * enterpriseId : 1
         * handlePersonId : null
         * phone : 15555545456
         * address : 中卫市沙坡头区
         * longitude : 105.23657
         * latitude : 37.62143
         * detail : 华御厂区内厂房发生爆炸，暂无人员伤亡
         * enclosure : null
         * sceneImages : 2021/10/28/022b33c4-ac5b-4b15-ade7-09c22d59d20d.jfif
         * status : null
         * reportingDepartment : null
         * employee : null
         * personnelHanding : null
         * handlingOpinions : null
         * creatorId : 2
         * updateId : null
         * createTime : 2021-10-28 20:50:30
         * updateTime : null
         * code : null
         * assignId : null
         * assignName : null
         * likeName : null
         * dateLike : null
         * type : null
         * isReport : null
         * isAssign : null
         * isEnd : null
         * isUpdate : null
         * createName : 测试管委会
         */
        public int id;
        public String name;
        public int eventType;
        public int level;
        public String happenedTime;
        public String handleOpinions;
        public int enterpriseId;
        public String handlePersonId;
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
        public String updateId;
        public String createTime;
        public String updateTime;
        public String code;
        public String assignId;
        public String assignName;
        public String likeName;
        public String dateLike;
        public String type;
        public String isReport;
        public String isAssign;
        public String isEnd;
        public String isUpdate;
        public String createName;


    }
}
