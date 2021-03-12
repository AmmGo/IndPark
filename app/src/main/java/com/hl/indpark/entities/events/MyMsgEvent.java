package com.hl.indpark.entities.events;

import java.util.List;

public class MyMsgEvent {
    public List<RecordsBean> records;

    public static class RecordsBean {
        public int id;

        public int enterpriseId;

        public String name;

        public String remarks;

        public String pushTime;

        public Integer isDelete;
    }
}
