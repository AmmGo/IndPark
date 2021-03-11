package com.hl.indpark.nets;


import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UserInfoEvent;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by yjl on 2021/3/8 9:45
 * Function：
 * Desc：
 */
public interface Api {
    //    String BASE_URL = "http://192.168.119.237:11035/";
    String BASE_URL = "http://192.168.119.248:11035/";

    /*=======登陆注册======*/
    @POST("loginPhone")
    LiveData<Resource<Response<LoginResultEntity>>> login(@Body HashMap<String, String> map);

    /*=======环保报警======*/
    @GET("/phone/environment/")
    LiveData<Resource<Response<EPAlarmEvent>>> getEpAlarm(@Query("type") String type);

    /*=======危险源报警======*/
    @GET("/phone/hazard/")
    LiveData<Resource<Response<HSAlarmEvent>>> getHSAlarm(@Query("type") String type);

    /*=======上报事件type======*/
    @GET("/eventType/list/")
    LiveData<Resource<Response<List<ReportTypeEvent>>>> getReportType();

    /*=======通讯录======*/
    @GET("/phone/person/")
    LiveData<Resource<Response<List<PhoneEvent>>>> getPhoneEvent();

    /*=======用户信息======*/
    @GET("/phone/user/")
    LiveData<Resource<Response<UserInfoEvent>>> getUserInfoEvent();

    /*=======我的上报or我的审批======*/
    @GET("/phone/events/")
    LiveData<Resource<Response<SelfReportEvent>>> getSelfReportEvent();

    /*=======企业列表======*/
    @GET("/transmission/enterpriseList/")
    LiveData<Resource<Response<List<EntEvent>>>> getEnterpriseEvent();

    /*=======企业工艺列表======*/
    @GET("/perilousCraft/queryTechnologyIdByEnterpriseId/")
    LiveData<Resource<Response<List<EntEvent>>>> getEntTypeEvent(@Query("enterpriseId") int id);

    /*=======危险源信息======*/
    @GET("/transmission/querySourceDangerRealDate/")
    LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid);


}
