package com.hl.indpark.entities.events;

public class loginEvent {

    /**
     * code : 200
     * msg : success
     * data : {"id":1,"token":"54f1510b02bb4358a3309bc4aa9535f8","date":"2021-03-05 17:33:58","expirationTime":"2021-03-05 19:33:58","name":"管理员","deptId":11,"deptName":"工程技术部","enterpriseId":358,"enterpriseName":"中卫市管委会","category":2,"roleId":1,"roleName":"超级管理员","menuList":null,"authURL":null,"ico":null}
     */

    private int code;
    private String msg;
    private DataBean data;
    public static class DataBean {
        /**
         * id : 1
         * token : 54f1510b02bb4358a3309bc4aa9535f8
         * date : 2021-03-05 17:33:58
         * expirationTime : 2021-03-05 19:33:58
         * name : 管理员
         * deptId : 11
         * deptName : 工程技术部
         * enterpriseId : 358
         * enterpriseName : 中卫市管委会
         * category : 2
         * roleId : 1
         * roleName : 超级管理员
         * menuList : null
         * authURL : null
         * ico : null
         */
        private int id;
        private String token;
        private String date;
        private String expirationTime;
        private String name;
        private int deptId;
        private String deptName;
        private int enterpriseId;
        private String enterpriseName;
        private int category;
        private int roleId;
        private String roleName;
        private Object menuList;
        private Object authURL;
        private Object ico;

    }
}
