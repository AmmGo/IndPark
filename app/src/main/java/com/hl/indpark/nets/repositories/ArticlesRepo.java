package com.hl.indpark.nets.repositories;

import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.Net;

import java.util.List;


/**
 * Created by yjl on 2021/3/8 17:22
 * Function：
 * Desc：
 */
public class ArticlesRepo {
    public static LiveData<Resource<Response<EPAlarmEvent>>> getEpAlarm(String type) {
        return Net.api().getEpAlarm(type);
    }

    public static LiveData<Resource<Response<HSAlarmEvent>>> getHSAlarm(String type) {
        return Net.api().getHSAlarm(type);
    }

    public static LiveData<Resource<Response<List<ReportTypeEvent>>>> getReportType() {
        return Net.api().getReportType();
    }

    public static LiveData<Resource<Response<List<PhoneEvent>>>> getPhoneEvent() {
        return Net.api().getPhoneEvent();
    }

    public static LiveData<Resource<Response<UserInfoEvent>>> getUserInfoEvent() {
        return Net.api().getUserInfoEvent();
    }

    public static LiveData<Resource<Response<SelfReportEvent>>> getSelfReportEvent() {
        return Net.api().getSelfReportEvent();
    }

    public static LiveData<Resource<Response<List<EntNameEvent>>>> getEnterpriseEvent() {
        return Net.api().getEnterpriseEvent();
    }

    public static LiveData<Resource<Response<List<EntTypeEvent>>>> getEntTypeEvent(int id) {
        return Net.api().getEntTypeEvent(id);
    }

    public static LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(String id, String tlid) {
        return Net.api().getEntSHSEvent(id, tlid);
    }
}
