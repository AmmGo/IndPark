package com.hl.indpark.nets;


import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntNewEp;
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

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by yjl on 2021/3/8 9:45
 * Function：
 * Desc：
 */
public interface Api {
    /**
     * java测试*/
//    String BASE_URL = "http://192.168.119.237:11035/";
    /**
     * 内网服务
     */
//    String BASE_URL = "http://192.168.119.248:11035/";
    /**
     * 外网测试
     */
    String BASE_URL = "http://222.75.227.14:11036/";
    String BASE_URL_IMG = "http://222.75.227.14:30000/";
    /**
     * 外网发布
     */
//    String BASE_URL = "http://industry.zwhldk.com/java/";
//    String BASE_URL_IMG = "http://industry.zwhldk.com/img/";
//    String BASE_URL_IMG = "http://appimg.hlx.com/";

    /*=======登陆注册======*/
    @POST("/loginPhone")
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
    LiveData<Resource<Response<List<EntNameEvent>>>> getEnterpriseEvent();

    /*=======企业工艺列表======*/
    @GET("/perilousCraft/queryTechnologyIdByEnterpriseId/")
    LiveData<Resource<Response<List<EntTypeEvent>>>> getEntTypeEvent(@Query("enterpriseId") int id);

    /*=======危险源信息======*/
    @GET("/transmission/querySourceDangerRealDate/")
    LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid);

    /*=======危险源信息分页======*/
    @GET("/phone/hazardDetail")
    LiveData<Resource<Response<EntSHSEvent>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid, @Query("current") int pageNum, @Query("size") int pageSiz, @Query("dataType") int timeType, @Query("type") String Type);

    /*=======企业排污口列表======*/
    @GET("/environmentpoint/list/")
    LiveData<Resource<Response<List<EntSEPTypeEvent>>>> getEntSEPTypeEvent(@Query("enterpriseId") int id);

    /*=======环保信息======*/
    @GET("/phone/EnvironmentMonitor/")
    LiveData<Resource<Response<EntSEPEvent>>> getEntSEPEvent(@Query("psCode") String id, @Query("pointCode") String tlid, @Query("current") int page, @Query("size") int pageSize, @Query("type") int timeType, @Query("isException") String isp);

    /*=======消息列表======*/
    @GET("/push/pageList/")
    LiveData<Resource<Response<MyMsgEvent>>> getMyMsgEvent(@Query("current") int page, @Query("size") int pageSize);

    /*=======上报列表======*/
    @GET("/phone/events/")
    LiveData<Resource<Response<MyPeportEvent>>> getMyPeportEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======审批列表======*/
    @GET("/phone/approveEvents/")
    LiveData<Resource<Response<MyApprovalEvent>>> getMyApprovalEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======审批列表-ID-查询事件======*/
    @GET("/event/findById/")
    LiveData<Resource<Response<MyPeportIDEvent>>> getMyPeportIDEvent(@Query("id") String id);

    /*=======提交审批======*/
    @POST("/event/update/")
    LiveData<Resource<Response<String>>> getMyPeportIDUpdateEvent(@Body HashMap<String, String> mape);

    /*=======用户信息修改======*/
    @POST("/phone/update/")
    LiveData<Resource<Response<String>>> getUserInfoUpdateEvent(@Body HashMap<String, String> map);

    /*=======是否推送======*/
    @POST("/user/update")
    LiveData<Resource<Response<String>>> getPushMsgEvent(@Body HashMap<String, String> map);

    /*=======上报事件======*/
    @POST("/event/insert/")
    LiveData<Resource<Response<String>>> getReportEvent(@Body HashMap<String, String> map);

    /**
     * 上传单张图片
     */
    @Multipart
    @POST("/file/upload")
    LiveData<Resource<Response<String>>> getUploadImg(@Part MultipartBody.Part MultipartFile);

    /**
     * 上传多张图片
     */
    @Multipart
    @POST("/file/multiUpload")
    LiveData<Resource<Response<String>>> getUploadImgS(@Part List<MultipartBody.Part> maps);

    /*=======企业列表New======*/
    @GET("/environmentpoint/listTree")
    LiveData<Resource<Response<List<EntNewEp>>>> getEntNewEp();

    /*=======修改消息状态======*/
    @POST("/push/update")
    LiveData<Resource<Response<String>>> getMspUpdate(@Body HashMap<String, String> map);

    /*=======消息未读数量======*/
    @GET("/push/read/")
    LiveData<Resource<Response<String>>> getMspRead();
}
