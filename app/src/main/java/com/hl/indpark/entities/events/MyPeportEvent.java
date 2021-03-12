package com.hl.indpark.entities.events;

import java.util.List;

public class MyPeportEvent {
    public List<RecordsBean> records;

    public static class RecordsBean {
        private String eventType;

        public int id;

        private String createTime;

        private String reportedName;

        private String phone;

        private Integer status;
    }

}
