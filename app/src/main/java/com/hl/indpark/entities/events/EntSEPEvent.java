package com.hl.indpark.entities.events;

import java.util.List;

/**
 * Created by yjl on 2021/3/12 10:22
 * Function：     "iocode": "8""name": "废气排放口",
 * Desc：排污口列表
 */
public class EntSEPEvent {

    public List<RecordsBean> records;

    public static class RecordsBean {
        /**
         * 排口名称
         */
        public String equipmentName;
        /**
         * 监测时间
         */
        public String monitorTime;
        /**
         * 废气排口排放量
         */
        public String exhaustData;
        /**
         * 废水排口排放量
         */
        public String wastewaterData;
        /**
         * COD实测浓度mg/L
         */
        public String coddata;
        /**
         * 氨氮实测浓度mg/L
         */
        public String ammoniacalData;
        /**
         * 总磷实测浓度mg/L
         */
        public String totalData;
        /**
         * 烟尘实测浓度mg/m3
         */
        public String smokeData;
        /**
         * 二氧化硫实测浓度mg/m3
         */
        public String sulfurData;
        /**
         * 氮氧化物实测浓度mg/m3
         */
        public String nitrogenData;
        /**
         * 类型
         */
        public String isException ;

        public RecordsBean(String equipmentName, String monitorTime, String exhaustData, String wastewaterData, String coddata, String ammoniacalData, String totalData, String smokeData, String sulfurData, String nitrogenData, String isException) {
            this.equipmentName = equipmentName;
            this.monitorTime = monitorTime;
            this.exhaustData = exhaustData;
            this.wastewaterData = wastewaterData;
            this.coddata = coddata;
            this.ammoniacalData = ammoniacalData;
            this.totalData = totalData;
            this.smokeData = smokeData;
            this.sulfurData = sulfurData;
            this.nitrogenData = nitrogenData;
            this.isException = isException;
        }
    }
}
