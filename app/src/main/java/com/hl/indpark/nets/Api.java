package com.hl.indpark.nets;


import androidx.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.CameraVideoEvent;
import com.hl.indpark.entities.events.CommodityEvent;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntNewEp;
import com.hl.indpark.entities.events.EntSEPEvent;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.MapPointEvent;
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
//        String BASE_JAVA = "";
//        String BASE_URL_IMG = "http://appimg.hlx.com/";
    /**
     * 内网服务
     */
//    String BASE_URL = "http://192.168.119.248:11035/";
//    String BASE_JAVA = "";
//    String BASE_URL_IMG = "http://appimg.hlx.com/";
    /**
     * 外网测试
     */
    String BASE_URL = "http://222.75.227.14:11036/";
    String BASE_URL_IMG = "http://222.75.227.14:30000/";
        String BASE_JAVA = "";

    /**
     * 外网发布
     */
//    String BASE_URL = "https://www.nxzwgyyqgwh.com.cn/";
//    String BASE_URL_IMG = "https://www.nxzwgyyqgwh.com.cn/img/";
//    String BASE_JAVA = "java";

    /*=======登陆注册======*/
    @POST(BASE_JAVA + "/loginPhone")
    LiveData<Resource<Response<LoginResultEntity>>> login(@Body HashMap<String, String> map);

    /*=======环保报警======*/
    @GET(BASE_JAVA + "/phone/environment/")
    LiveData<Resource<Response<EPAlarmEvent>>> getEpAlarm(@Query("type") String type);

    /*=======危险源报警======*/
    @GET(BASE_JAVA + "/phone/hazard/")
    LiveData<Resource<Response<HSAlarmEvent>>> getHSAlarm(@Query("type") String type);

    /*=======上报事件type======*/
    @GET(BASE_JAVA + "/eventType/list/")
    LiveData<Resource<Response<List<ReportTypeEvent>>>> getReportType();

    /*=======通讯录======*/
    @GET(BASE_JAVA + "/phone/person/")
    LiveData<Resource<Response<List<PhoneEvent>>>> getPhoneEvent();

    /*=======用户信息======*/
    @GET(BASE_JAVA + "/phone/user/")
    LiveData<Resource<Response<UserInfoEvent>>> getUserInfoEvent();

    /*=======我的上报or我的审批======*/
    @GET(BASE_JAVA + "/phone/events/")
    LiveData<Resource<Response<SelfReportEvent>>> getSelfReportEvent();

    /*=======企业列表======*/
    @GET(BASE_JAVA + "/transmission/enterpriseList/")
    LiveData<Resource<Response<List<EntNameEvent>>>> getEnterpriseEvent();

    /*=======企业工艺列表======*/
    @GET(BASE_JAVA + "/perilousCraft/queryTechnologyIdByEnterpriseId/")
    LiveData<Resource<Response<List<EntTypeEvent>>>> getEntTypeEvent(@Query("enterpriseId") int id);

    /*=======危险源信息======*/
    @GET(BASE_JAVA + "/transmission/querySourceDangerRealDate/")
    LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid);

    /*=======危险源信息分页======*/
    @GET(BASE_JAVA + "/phone/hazardDetail")
    LiveData<Resource<Response<EntSHSEvent>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid, @Query("current") int pageNum, @Query("size") int pageSiz, @Query("dataType") int timeType, @Query("type") String Type);

    /*=======企业排污口列表======*/
    @GET(BASE_JAVA + "/environmentpoint/list/")
    LiveData<Resource<Response<List<EntSEPTypeEvent>>>> getEntSEPTypeEvent(@Query("enterpriseId") int id);

    /*=======环保信息======*/
    @GET(BASE_JAVA + "/phone/EnvironmentMonitor/")
    LiveData<Resource<Response<EntSEPEvent>>> getEntSEPEvent(@Query("psCode") String id, @Query("pointCode") String tlid, @Query("current") int page, @Query("size") int pageSize, @Query("type") int timeType, @Query("isException") String isp);

    /*=======消息列表======*/
    @GET(BASE_JAVA + "/push/pageList/")
    LiveData<Resource<Response<MyMsgEvent>>> getMyMsgEvent(@Query("current") int page, @Query("size") int pageSize);

    /*=======上报列表======*/
    @GET(BASE_JAVA + "/phone/events/")
    LiveData<Resource<Response<MyPeportEvent>>> getMyPeportEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======审批列表======*/
    @GET(BASE_JAVA + "/phone/approveEvents/")
    LiveData<Resource<Response<MyApprovalEvent>>> getMyApprovalEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======审批列表-ID-查询事件======*/
    @GET(BASE_JAVA + "/event/findById/")
    LiveData<Resource<Response<MyPeportIDEvent>>> getMyPeportIDEvent(@Query("id") String id);

    /*=======视频呼叫======*/
    @GET(BASE_JAVA + "/phone/push/")
    LiveData<Resource<Response<String>>> getVideoPush(@Query("content") String content, @Query("userId") String userId);

    /*=======提交审批======*/
    @POST(BASE_JAVA + "/event/update/")
    LiveData<Resource<Response<String>>> getMyPeportIDUpdateEvent(@Body HashMap<String, String> mape);

    /*=======用户信息修改======*/
    @POST(BASE_JAVA + "/phone/update/")
    LiveData<Resource<Response<String>>> getUserInfoUpdateEvent(@Body HashMap<String, String> map);

    /*=======是否推送======*/
    @POST(BASE_JAVA + "/user/update")
    LiveData<Resource<Response<String>>> getPushMsgEvent(@Body HashMap<String, String> map);

    /*=======上报事件======*/
    @POST(BASE_JAVA + "/event/insert/")
    LiveData<Resource<Response<String>>> getReportEvent(@Body HashMap<String, String> map);

    /**
     * 上传单张图片
     */
    @Multipart
    @POST(BASE_JAVA + "/file/upload")
    LiveData<Resource<Response<String>>> getUploadImg(@Part MultipartBody.Part MultipartFile);

    /**
     * 上传多张图片
     */
    @Multipart
    @POST(BASE_JAVA + "/file/multiUpload")
    LiveData<Resource<Response<String>>> getUploadImgS(@Part List<MultipartBody.Part> maps);

    /*=======企业列表New======*/
    @GET(BASE_JAVA + "/environmentpoint/listTree")
    LiveData<Resource<Response<List<EntNewEp>>>> getEntNewEp();

    /*=======修改消息状态======*/
    @POST(BASE_JAVA + "/push/update")
    LiveData<Resource<Response<String>>> getMspUpdate(@Body HashMap<String, String> map);

    /*=======消息未读数量======*/
    @GET(BASE_JAVA + "/push/read/")
    LiveData<Resource<Response<String>>> getMspRead();

    /**
     * 版本更新
     */
    @GET(BASE_JAVA + "/appmanagement/newest")
    LiveData<Resource<Response<UpdateVersion>>> getUpdateVersion(@Query("version") String version);

    /*=======用户签到======*/
    @GET(BASE_JAVA + "/phone/sign/")
    LiveData<Resource<Response<String>>> getSignIn(@Query("longitude") String longitude, @Query("latitude") String latitude);

    /*=======可用积分======*/
    @GET(BASE_JAVA + "/integral/count/")
    LiveData<Resource<Response<ScoresDetailsEvent>>> getScoresDetails();

    /*=======商品列表======*/
    @GET(BASE_JAVA + "/product/pageList/")
    LiveData<Resource<Response<CommodityEvent>>> getCommodity(@Query("current") int page, @Query("size") int pageSize);

    /*=======积分记录======*/
    @GET(BASE_JAVA + "/integral/pageList/")
    LiveData<Resource<Response<MyScoresEvent>>> getMyScoresEvent(@Query("current") int page, @Query("size") int pageSize, @Query("type") String state);

    /*=======积分兑换记录======*/
    @GET(BASE_JAVA + "/creditexchange/pageList/")
    LiveData<Resource<Response<MyExchangeRecordEvent>>> getMyExchangeRecord(@Query("current") int page, @Query("size") int pageSize, @Query("state") String state);

    /*=======积分兑换商品======*/
    @GET(BASE_JAVA + "/creditexchange/exchange")
    LiveData<Resource<Response<String>>> getScoresExchangeCommodity(@Query("productId") int productId);

    /*=======自检上报======*/
    @POST(BASE_JAVA + "/appChecking/insert")
    LiveData<Resource<Response<String>>> getCheckReportEvent(@Body HashMap<String, String> map);

    /*=======日志管理=====*/
    @GET(BASE_JAVA + "/push/journal")
    LiveData<Resource<Response<MyMsgEvent>>> getLogManager(@Query("current") int page, @Query("size") int pageSize,@Query("enterpriseId") String enterpriseId,@Query("date") String date);

    /*=======企业点位======*/
    @GET(BASE_JAVA + "/phone/navigationEnterprise")
    LiveData<Resource<Response<List<MapPointEvent>>>> getEpPoint();

    /*=======巡检设备======*/
    @GET(BASE_JAVA + "/phone/navigationDevice")
    LiveData<Resource<Response<List<MapPointEvent>>>> getCheckPoint();

    /*=======事件点位======*/
    @GET(BASE_JAVA + "/phone/navigationEvent")
    LiveData<Resource<Response<List<MapPointEvent>>>> getEventPoint();

    /*=======   监控系统-高空瞭望视频======*/
    @GET(BASE_JAVA + "/phone/lookout")
    LiveData<Resource<Response<List<CameraVideoEvent>>>> getLookout(@Query("current") int page, @Query("size") int pageSize);

    /*======= 监控系统-危险源视频======*/
    @GET(BASE_JAVA + "/phone/hazardCamera")
    LiveData<Resource<Response<List<CameraVideoEvent>>>> getHazardCamera(@Query("current") int page, @Query("size")int pageSize ,@Query("enterpriseId") String enterpriseId);
}
