package com.hl.indpark.entities.events;

import java.util.List;

public class LineChartsHb {

    /**
     * key : 水排口
     * value : [{"availableStatus":"0","avgFlow":null,"avgStrength":"15.790000","couFlow":"7.887000","dataSource":"Null","dataType":null,"equipmentCode":null,"id":2056516,"isException":null,"maxFlow":null,"maxStrength":"15.790000","minFlow":null,"minStrength":"15.790000","monitorTime":"2021-08-26 10:04:12.0","pointCode":"2","pollutantCode":"w01018","psCode":"640500000072","realTimeData":"15.790000","reviseCause":null,"revisedAvgFlow":null,"revisedFlow":null,"revisedStrength":null,"typerName":null,"typerUnit":null},{"availableStatus":"0","avgFlow":null,"avgStrength":"0.234000","couFlow":"0.117000","dataSource":"Null","dataType":null,"equipmentCode":null,"id":2056511,"isException":null,"maxFlow":null,"maxStrength":"0.234000","minFlow":null,"minStrength":"0.234000","monitorTime":"2021-08-26 10:05:12.0","pointCode":"2","pollutantCode":"w21011","psCode":"640500000072","realTimeData":"0.234000","reviseCause":null,"revisedAvgFlow":null,"revisedFlow":null,"revisedStrength":null,"typerName":null,"typerUnit":null}]
     */
    public String key;
    public List<ValueBean> value;
    public static class ValueBean {
        /**
         * availableStatus : 0
         * avgFlow : null
         * avgStrength : 15.790000
         * couFlow : 7.887000
         * dataSource : Null
         * dataType : null
         * equipmentCode : null
         * id : 2056516
         * isException : null
         * maxFlow : null
         * maxStrength : 15.790000
         * minFlow : null
         * minStrength : 15.790000
         * monitorTime : 2021-08-26 10:04:12.0
         * pointCode : 2
         * pollutantCode : w01018
         * psCode : 640500000072
         * realTimeData : 15.790000
         * reviseCause : null
         * revisedAvgFlow : null
         * revisedFlow : null
         * revisedStrength : null
         * typerName : null
         * typerUnit : null
         */
        public String monitorTime;
        public String pollutantCode;
        public String realTimeData;
    }
}
