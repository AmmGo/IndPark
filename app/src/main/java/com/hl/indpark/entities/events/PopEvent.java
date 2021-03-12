package com.hl.indpark.entities.events;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 13:32
 * Function：entChose  100表示企业列表弹框 101表示工艺类型弹框
 * Desc：通用ent
 */
public class PopEvent {
    public int entChose;
    public List<EntTypeEvent> entTypeEvents;
    public List<EntSEPTypeEvent> entSEPTypeEvents;
    public List<EntNameEvent> entNameEvents;
    public List<ReportTypeEvent> reportTypeEventList;
}
