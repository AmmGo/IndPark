package com.hl.indpark.nets.repositories;

import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.CommodityEvent;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntNewEp;
import com.hl.indpark.entities.events.EntSEPEvent;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.entities.events.MyExchangeRecordEvent;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.events.MyPeportEvent;
import com.hl.indpark.entities.events.MyPeportIDEvent;
import com.hl.indpark.entities.events.MyScoresEvent;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.ScoresDetailsEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.Net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;


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

    public static LiveData<Resource<Response<List<EntNewEp>>>> getEntNewEp() {
        return Net.api().getEntNewEp();
    }

    public static LiveData<Resource<Response<List<EntTypeEvent>>>> getEntTypeEvent(int id) {
        return Net.api().getEntTypeEvent(id);
    }

    public static LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(String id, String tlid) {
        return Net.api().getEntSHSEvent(id, tlid);
    }

    public static LiveData<Resource<Response<EntSHSEvent>>> getEntSHSEvent(String id, String tlid, int pageNum, int pageSize, int timeType, String dataType) {
        return Net.api().getEntSHSEvent(id, tlid, pageNum, pageSize, timeType, dataType);
    }

    public static LiveData<Resource<Response<List<EntSEPTypeEvent>>>> getEntSEPTypeEvent(int id) {
        return Net.api().getEntSEPTypeEvent(id);
    }

    public static LiveData<Resource<Response<EntSEPEvent>>> getEntSEPEvent(String id, String tlid, int page, int sizePage, int timeType, String isP) {
        return Net.api().getEntSEPEvent(id, tlid, page, sizePage, timeType, isP);
    }


    public static LiveData<Resource<Response<MyMsgEvent>>> getMyMsgEvent(int sizePage, int isP) {
        return Net.api().getMyMsgEvent(sizePage, isP);
    }

    public static LiveData<Resource<Response<MyPeportIDEvent>>> getMyPeportIDEvent(String id) {
        return Net.api().getMyPeportIDEvent(id);
    }

    public static LiveData<Resource<Response<String>>> getVideoPush(String content, String userId) {
        return Net.api().getVideoPush(content, userId);
    }

    public static LiveData<Resource<Response<MyPeportEvent>>> getMyPeportEvent(int sizePage, int isP, String state) {
        return Net.api().getMyPeportEvent(sizePage, isP, state);
    }

    public static LiveData<Resource<Response<MyApprovalEvent>>> getMyApprovalEvent(int sizePage, int isP, String state) {
        return Net.api().getMyApprovalEvent(sizePage, isP, state);
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

    public static LiveData<Resource<Response<String>>> getMsgUpdate(Map map) {
        return Net.api().getMspUpdate((HashMap<String, String>) map);
    }

    public static LiveData<Resource<Response<String>>> getMsgRead() {
        return Net.api().getMspRead();
    }

    public static LiveData<Resource<Response<UpdateVersion>>> getUpdateVersion(String id) {
        return Net.api().getUpdateVersion(id);
    }

    public static LiveData<Resource<Response<MyScoresEvent>>> getMyScores(int sizePage, int isP, String state) {
        return Net.api().getMyScoresEvent(sizePage, isP, state);
    }

    public static LiveData<Resource<Response<MyExchangeRecordEvent>>> getMyExchangeRecord(int sizePage, int isP, String state) {
        return Net.api().getMyExchangeRecord(sizePage, isP, state);
    }

    public static LiveData<Resource<Response<CommodityEvent>>> getCommodity(int sizePage, int isP) {
        return Net.api().getCommodity(sizePage, isP);
    }

    public static LiveData<Resource<Response<ScoresDetailsEvent>>> getScoresDetails() {
        return Net.api().getScoresDetails();
    }

    public static LiveData<Resource<Response<String>>> getSignIn(String lon, String lat) {
        return Net.api().getSignIn(lon, lat);
    }

    public static LiveData<Resource<Response<String>>> getScoresExchangeCommodity(int id) {
        return Net.api().getScoresExchangeCommodity(id);
    }

    public static LiveData<Resource<Response<String>>> getCheckReportEvent(Map map) {
        return Net.api().getCheckReportEvent((HashMap<String, String>) map);
    }

    public static LiveData<Resource<Response<MyMsgEvent>>> getLogManager(int sizePage, int isP, String qid, String date) {
        return Net.api().getLogManager(sizePage, isP, qid, date);
    }
}
