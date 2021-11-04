package com.hl.indpark.entities.events;

import java.util.List;

public class SelfCheck {
    public List<SelfCheck.RecordsBean> records;
    public String getType(int type) {
        String type1 = "";
        switch (type) {
            case 1:
                type1 = "安全自检";
                break;
            case 2:
                type1 = "消防自检";
                break;
            case 3:
                type1 = "设备自检";
                break;
            case 4:
                type1 = "其他自检";
                break;
            default:
                type1 = "其他自检";
        }
        return type1;
    }
    public static class RecordsBean {
        //@ApiModelProperty(value = "单位自检主键id", name = "checkingId")
        public int checkingId;
        public int id;

        //@ApiModelProperty(value = "自检类型 1：安全自检 2： 消防自检 3：设备自检 4：其他自检", name = "checkType")
        public int checkType;

        //@ApiModelProperty(value = "详细地址", name = "address")
        public String address;

        // @ApiModelProperty(value = "自检详情", name = "details")
        public String details;

        //@ApiModelProperty(value = "上传图片 多个用逗号隔开", name = "images")
        public String images;

        // @ApiModelProperty(value = "企业id", name = "enterpriseId")
        public int enterpriseId;

        //@ApiModelProperty(value = "创建时间", name = "createTime")
        public String createTime;

        //@ApiModelProperty(value = "更新时间", name = "updateTime")
        public String updateTime;

        //@ApiModelProperty(value = "修改人ID", name = "updateId")
        public int updateId;

        //@ApiModelProperty(value = "是否删除；1：删除; 2：正常；", name = "isDelete")
        public int isDelete;

        //@ApiModelProperty(value = "图片列表", name = "imageList")
        public List<String> imageList;
    }
}
