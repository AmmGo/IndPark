package com.hl.indpark.entities.events;

import java.util.List;

public class ReportEvent {
    /**
     * content : string
     * createId : 0
     * createTime : 2021-03-10T05:45:36.875Z
     * handleImage : string
     * handleOpinion : string
     * id : 0
     * image : string
     * imageList : ["string"]
     * isDelete : 0
     * latitude : 0
     * longitude : 0
     * phone : string
     * reportedName : string
     * resultImage : string
     * review : string
     * status : 0
     * title : string
     * type : string
     * updateId : 0
     * updateTime : 2021-03-10T05:45:36.875Z
     */

    /**
     * 内容
     * */
    public String content;
    public int createId;
    public String createTime;
    public String handleImage;
    public String handleOpinion;
    public int id;
    /**
     * 上报图片
     * */
    public String image;
    public int isDelete;
    /**
     * 经纬度
     * */
    public int latitude;
    public int longitude;
    public String phone;
    public String reportedName;
    public String resultImage;
    public String review;
    public int status;
    /**
     * 标题
     * */
    public String title;
    /**
     * 事件type
     * */
    public String type;
    public int updateId;
    public String updateTime;
    public List<String> imageList;


}
