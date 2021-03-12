package com.hl.indpark.entities.events;

import java.util.List;

public class MyPeportIDEvent {

    public Integer id;

//    @TableField(value = "title")
//    @ApiModelProperty(value = "标题", name = "title")
    public String title;

//    @TableField(value = "content")
//    @ApiModelProperty(value = "内容", name = "content")
    public String content;

//    @TableField(value = "image")
//    @ApiModelProperty(value = "上报图片", name = "image")
    public String image;

//    @TableField(value = "longitude")
//    @ApiModelProperty(value = "经度", name = "longitude")
    public double longitude;

//    @TableField(value = "latitude")
//    @ApiModelProperty(value = "纬度", name = "latitude")
    public double latitude;

//    @TableField(value = "phone")
//    @ApiModelProperty(value = "电话", name = "phone")
    public String phone;

//    @TableField(value = "create_time")
//    @ApiModelProperty(value = "创建时间", name = "createTime")
    public String createTime;

//    @TableField(value = "update_time")
//    @ApiModelProperty(value = "修改时间", name = "updateTime")
    public String updateTime;

//    @TableField(value = "update_id")
//    @ApiModelProperty(value = "修改人ID", name = "updateId")
    public Integer updateId;
//
//    @TableField(value = "create_id")
//    @ApiModelProperty(value = "创建人ID", name = "createId")
    public Integer createId;
//
//    @TableField(value = "is_delete")
//    @ApiModelProperty(value = "是否删除 1是 2否", name = "isDelete")
    public Integer isDelete;

//    @TableField(value = "status")
//    @ApiModelProperty(value = "处理状态", name = "status")
    public Integer status;

//    @TableField(value = "handle_image")
//    @ApiModelProperty(value = "处理图片", name = "handleImage")
    public String handleImage;

//    @TableField(value = "result_image")
//    @ApiModelProperty(value = "处理完成图片", name = "resultImage")
    public String resultImage;

//    @TableField(value = "handle_opinion")
//    @ApiModelProperty(value = "处理意见", name = "handleOpinion")
    public String handleOpinion;

//    @TableField(value = "review")
//    @ApiModelProperty(value = "审核意见", name = "review")
    public String review;

//    @TableField(value = "reported_name")
//    @ApiModelProperty(value = "上报人姓名", name = "reportedName")
    public String reportedName;

//    @TableField(value = "type")
//    @ApiModelProperty(value = "类型", name = "type")
    public String type;
//
//    @TableField(value = "reported_id")
//    @ApiModelProperty(value = "上报人id", name = "reportedId")
    public Integer reportedId;

//    @TableField(value = "enterprise_id")
//    @ApiModelProperty(value = "企业id", name = "enterpriseId")
    public Integer enterpriseId;

//    @TableField(value = "approve_id")
//    @ApiModelProperty(value = "审批人id", name = "approveId")
    public Integer approveId;

//    @TableField(exist = false)
//    @ApiModelProperty(value = "上报图片集合", name = "imageList")
    public List<String> imageList;

//    @TableField(exist = false)
//    @ApiModelProperty(value = "类型名称", name = "enterpriseId")
    public String typeName;

}
