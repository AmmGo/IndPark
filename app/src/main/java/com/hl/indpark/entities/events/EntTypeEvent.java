package com.hl.indpark.entities.events;

/**
 * Created by yjl on 2021/3/11 13:32
 * Function：
 * Desc：企业工艺类型
 */
public class EntTypeEvent {
    public String id;
    public String hazardType;
    public String name;

    public EntTypeEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public EntTypeEvent() {
    }
}
