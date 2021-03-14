package com.hl.indpark.entities.events;

import java.util.List;

public class MyPeportEvent {
    public List<RecordsBean> records;

    public static class RecordsBean {
        public String eventType;

        public int id;

        public String createTime;

        public String reportedName;

        public String phone;

        public String status;
    }

}
