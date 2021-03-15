package com.hl.indpark.nets.repositories;

import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntSEPEvent;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.events.MyPeportEvent;
import com.hl.indpark.entities.events.MyPeportIDEvent;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.Net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


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
    public static LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(String id, String tlid,int pageNum,int pageSize,int timeType,int dataType) {
        return Net.api().getEntSHSEvent(id, tlid,pageNum,pageSize,timeType,dataType);
    }

    public static LiveData<Resource<Response<List<EntSEPTypeEvent>>>> getEntSEPTypeEvent(int id) {
        return Net.api().getEntSEPTypeEvent(id);
    }

    public static LiveData<Resource<Response<EntSEPEvent>>> getEntSEPEvent(String id, String tlid, String page, String sizePage, String isP) {
        return Net.api().getEntSEPEvent(id, tlid, page, sizePage, isP);
    }


    public static LiveData<Resource<Response<MyMsgEvent>>> getMyMsgEvent(int sizePage, int isP) {
        return Net.api().getMyMsgEvent(sizePage, isP);
    }

    public static LiveData<Resource<Response<MyPeportIDEvent>>> getMyPeportIDEvent(String id) {
        return Net.api().getMyPeportIDEvent(id);
    }

    public static LiveData<Resource<Response<MyPeportEvent>>> getMyPeportEvent(int sizePage, int isP,String state) {
        return Net.api().getMyPeportEvent(sizePage, isP,state);
    }

    public static LiveData<Resource<Response<MyApprovalEvent>>> getMyApprovalEvent(int sizePage, int isP,String state) {
        return Net.api().getMyApprovalEvent(sizePage, isP,state);
    }

    public static LiveData<Resource<Response<String>>> getUserInfoUpdateEvent(Map map) {
        return Net.api().getUserInfoUpdateEvent((HashMap<String, String>) map);
    }

    public static LiveData<Resource<Response<String>>> getMyPeportIDUpdateEvent(Map map) {
        return Net.api().getMyPeportIDUpdateEvent((HashMap<String, String>) map);
    }

    public static LiveData<Resource<Response<String>>> getReportEvent(Map map) {
        return Net.api().getReportEvent((HashMap<String, String>) map);
    }

    public static LiveData<Resource<Response<String>>> getUploadImg(MultipartBody.Part MultipartFile) {
        return Net.api().getUploadImg(MultipartFile);
    }

    public static LiveData<Resource<Response<String>>> getUploadImgS(List<MultipartBody.Part> maps) {
        return Net.api().getUploadImgS(maps);
    }
}
