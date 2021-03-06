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
import com.hl.indpark.entities.events.LineChartWxy;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.entities.events.MachineCheck;
import com.hl.indpark.entities.events.MachineCheckId;
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
import com.hl.indpark.entities.events.SelfCheck;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.entities.events.WxyEvent;
import com.hl.indpark.entities.events.WxyTjEvent;

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
 * Function???
 * Desc???
 */
public interface Api {
    /**
     * java??????*/
//    String BASE_URL = "http://192.168.119.224:11035/";
//        String BASE_JAVA = "";
//        String BASE_URL_IMG = "http://appimg.hlx.com/";
    /**
     * ????????????1
     */
//    String BASE_URL = "http://192.168.119.248:11035/";
//    String BASE_JAVA = "";
//    String BASE_URL_IMG = "http://appimg.hlx.com/";
    /**
     * ????????????2
     */
    String BASE_URL = "http://192.168.119.249:11035/";
    String BASE_JAVA = "";
    String BASE_URL_IMG = "http://appimg.hlx.com/";
    /**
     * ????????????
     */
//    String BASE_URL = "http://222.75.227.14:11036/";
//    String BASE_URL_IMG = "http://222.75.227.14:30000/";
//        String BASE_JAVA = "";

    /**
     * ????????????
     */
//    String BASE_URL = "https://www.nxzwgyyqgwh.com.cn/";
//    String BASE_URL_IMG = "https://www.nxzwgyyqgwh.com.cn/img/";
//    String BASE_JAVA = "java";

    /*=======????????????======*/
    @POST(BASE_JAVA + "/loginPhone")
    LiveData<Resource<Response<LoginResultEntity>>> login(@Body HashMap<String, String> map);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/environment/")
    LiveData<Resource<Response<EPAlarmEvent>>> getEpAlarm(@Query("type") String type);

    /*=======???????????????======*/
    @GET(BASE_JAVA + "/phone/hazard/")
    LiveData<Resource<Response<HSAlarmEvent>>> getHSAlarm(@Query("type") String type);

    /*=======????????????type======*/
    @GET(BASE_JAVA + "/eventType/list/")
    LiveData<Resource<Response<List<ReportTypeEvent>>>> getReportType();

    /*=======?????????======*/
    @GET(BASE_JAVA + "/phone/person/")
    LiveData<Resource<Response<List<PhoneEvent>>>> getPhoneEvent();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/user/")
    LiveData<Resource<Response<UserInfoEvent>>> getUserInfoEvent();

    /*=======????????????or????????????======*/
    @GET(BASE_JAVA + "/phone/events/")
    LiveData<Resource<Response<SelfReportEvent>>> getSelfReportEvent();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/transmission/enterpriseList/")
    LiveData<Resource<Response<List<EntNameEvent>>>> getEnterpriseEvent();

    /*=======??????????????????======*/
    @GET(BASE_JAVA + "/perilousCraft/queryTechnologyIdByEnterpriseId/")
    LiveData<Resource<Response<List<EntTypeEvent>>>> getEntTypeEvent(@Query("enterpriseId") int id);

    /*=======???????????????======*/
    @GET(BASE_JAVA + "/transmission/querySourceDangerRealDate/")
    LiveData<Resource<Response<List<EntSHSEvent>>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid);

    /*=======?????????????????????======*/
    @GET(BASE_JAVA + "/phone/hazardDetail")
    LiveData<Resource<Response<EntSHSEvent>>> getEntSHSEvent(@Query("enterpriseId") String id, @Query("technologyId") String tlid, @Query("current") int pageNum, @Query("size") int pageSiz, @Query("dataType") int timeType, @Query("type") String Type);

    /*=======?????????????????????======*/
    @GET(BASE_JAVA + "/environmentpoint/list/")
    LiveData<Resource<Response<List<EntSEPTypeEvent>>>> getEntSEPTypeEvent(@Query("enterpriseId") int id);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/EnvironmentMonitor/")
    LiveData<Resource<Response<EntSEPEvent>>> getEntSEPEvent(@Query("psCode") String id, @Query("pointCode") String tlid, @Query("current") int page, @Query("size") int pageSize, @Query("type") int timeType, @Query("isException") String isp);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/push/pageList/")
    LiveData<Resource<Response<MyMsgEvent>>> getMyMsgEvent(@Query("current") int page, @Query("size") int pageSize);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/events/")
    LiveData<Resource<Response<MyPeportEvent>>> getMyPeportEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/approveEvents/")
    LiveData<Resource<Response<MyApprovalEvent>>> getMyApprovalEvent(@Query("current") int page, @Query("size") int pageSize, @Query("status") String state);

    /*=======????????????-ID-????????????======*/
    @GET(BASE_JAVA + "/event/findById/")
    LiveData<Resource<Response<MyPeportIDEvent>>> getMyPeportIDEvent(@Query("id") String id);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/push/")
    LiveData<Resource<Response<String>>> getVideoPush(@Query("content") String content, @Query("userId") String userId);

    /*=======????????????======*/
    @POST(BASE_JAVA + "/event/update/")
    LiveData<Resource<Response<String>>> getMyPeportIDUpdateEvent(@Body HashMap<String, String> mape);

    /*=======??????????????????======*/
    @POST(BASE_JAVA + "/phone/update/")
    LiveData<Resource<Response<String>>> getUserInfoUpdateEvent(@Body HashMap<String, String> map);

    /*=======????????????======*/
    @POST(BASE_JAVA + "/user/update")
    LiveData<Resource<Response<String>>> getPushMsgEvent(@Body HashMap<String, String> map);

    /*=======????????????======*/
    @POST(BASE_JAVA + "/event/insert/")
    LiveData<Resource<Response<String>>> getReportEvent(@Body HashMap<String, String> map);

    /**
     * ??????????????????
     */
    @Multipart
    @POST(BASE_JAVA + "/file/upload")
    LiveData<Resource<Response<String>>> getUploadImg(@Part MultipartBody.Part MultipartFile);

    /**
     * ??????????????????
     */
    @Multipart
    @POST(BASE_JAVA + "/file/multiUpload")
    LiveData<Resource<Response<String>>> getUploadImgS(@Part List<MultipartBody.Part> maps);

    /*=======????????????New======*/
    @GET(BASE_JAVA + "/environmentpoint/listTree")
    LiveData<Resource<Response<List<EntNewEp>>>> getEntNewEp();

    /*=======??????????????????======*/
    @POST(BASE_JAVA + "/push/update")
    LiveData<Resource<Response<String>>> getMspUpdate(@Body HashMap<String, String> map);

    /*=======??????????????????======*/
    @GET(BASE_JAVA + "/push/read/")
    LiveData<Resource<Response<String>>> getMspRead();

    /**
     * ????????????
     */
    @GET(BASE_JAVA + "/appmanagement/newest")
    LiveData<Resource<Response<UpdateVersion>>> getUpdateVersion(@Query("version") String version);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/sign/")
    LiveData<Resource<Response<String>>> getSignIn(@Query("longitude") String longitude, @Query("latitude") String latitude);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/integral/count/")
    LiveData<Resource<Response<ScoresDetailsEvent>>> getScoresDetails();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/product/pageList/")
    LiveData<Resource<Response<CommodityEvent>>> getCommodity(@Query("current") int page, @Query("size") int pageSize);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/integral/pageList/")
    LiveData<Resource<Response<MyScoresEvent>>> getMyScoresEvent(@Query("current") int page, @Query("size") int pageSize, @Query("type") String state);

    /*=======??????????????????======*/
    @GET(BASE_JAVA + "/creditexchange/pageList/")
    LiveData<Resource<Response<MyExchangeRecordEvent>>> getMyExchangeRecord(@Query("current") int page, @Query("size") int pageSize, @Query("state") String state);

    /*=======??????????????????======*/
    @GET(BASE_JAVA + "/creditexchange/exchange")
    LiveData<Resource<Response<String>>> getScoresExchangeCommodity(@Query("productId") int productId);

    /*=======????????????======*/
    @POST(BASE_JAVA + "/appChecking/insert")
    LiveData<Resource<Response<String>>> getCheckReportEvent(@Body HashMap<String, String> map);

    /*=======????????????=====*/
    @GET(BASE_JAVA + "/push/journal")
    LiveData<Resource<Response<MyMsgEvent>>> getLogManager(@Query("current") int page, @Query("size") int pageSize,@Query("enterpriseId") String enterpriseId,@Query("date") String date);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/navigationEnterprise")
    LiveData<Resource<Response<List<MapPointEvent>>>> getEpPoint();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/navigationDevice")
    LiveData<Resource<Response<List<MapPointEvent>>>> getCheckPoint();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/navigationEvent")
    LiveData<Resource<Response<List<MapPointEvent>>>> getEventPoint();

    /*=======   ????????????-??????????????????======*/
    @GET(BASE_JAVA + "/phone/lookout")
    LiveData<Resource<Response<List<CameraVideoEvent>>>> getLookout(@Query("current") int page, @Query("size") int pageSize);

    /*======= ????????????-???????????????======*/
    @GET(BASE_JAVA + "/phone/hazardCamera")
    LiveData<Resource<Response<List<CameraVideoEvent>>>> getHazardCamera(@Query("current") int page, @Query("size")int pageSize ,@Query("enterpriseId") String enterpriseId,@Query("name") String name);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/appChecking/pageList")
    LiveData<Resource<Response<SelfCheck>>> getSelfCheck(@Query("current") int page, @Query("size") int pageSize);

    /*=======????????????-ID-????????????======*/
    @GET(BASE_JAVA + "/appChecking/findById")
    LiveData<Resource<Response<SelfCheck.RecordsBean>>> getCheckIDEvent(@Query("id") String id);

    /*=======???????????????????????????======*/
    @GET(BASE_JAVA + "/transmission/querySourceDangerRealDate")
    LiveData<Resource<Response<List<WxyEvent>>>> getWxyEvent(@Query("enterpriseId") String id, @Query("queryType") int queryType);

    /*=======?????????????????????======*/
    @GET(BASE_JAVA + "/transmission/hazardNum")
    LiveData<Resource<Response<List<WxyTjEvent>>>> getWxyTjEvent();

    /*=======????????????????????????======*/
    @GET(BASE_JAVA + "/phone/alarmInfoStatistic")
    LiveData<Resource<Response<List<LineChartWxy>>>> getLineChartWxy(@Query("labelId") String labelId, @Query("type") int type);

    /*=======????????????????????????======*/
    @GET(BASE_JAVA + "/phone/realStatistics")
    LiveData<Resource<Response<List<LineChartWxy>>>> getLineChartWxyTj(@Query("labelId") String labelId, @Query("type") int type);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/environmentStatistics/analysisTrendChartEnvironment")
    LiveData<Resource<Response<List<LineChartsHb>>>> getLineChartHb(@Query("enterpriseId") String enterpriseId, @Query("pointId") String pointId, @Query("timeType") String timeType);

    /*=======????????????======*/
    @GET(BASE_JAVA + "/equipmentexaminedetails/enterpriseedetails")
    LiveData<Resource<Response<List<PhoneEvent>>>> getXjryCheck();

    /*=======??????????????????======*/
    @POST(BASE_JAVA + "/equipmentexaminedetails/insert")
    LiveData<Resource<Response<String>>> getMachineCheckReportEvent(@Body HashMap<String, String> map);

    /*=======????????????Id????????????======*/
    @POST(BASE_JAVA + "/equipmentexaminedetails/selectDetails")
    LiveData<Resource<Response<List<MachineCheckId>>>> getMachineCheckReportIdEvent(@Query("id") String id);

    /*=======????????????List????????????======*/
    @GET(BASE_JAVA + "/equipmentexaminedetails/select")
    LiveData<Resource<Response<List<MachineCheck>>>> getMachineCheckReportListEvent();

    /*=======????????????======*/
    @GET(BASE_JAVA + "/phone/giveAnAlarm")
    LiveData<Resource<Response<String>>> getCallPolice(@Query("address") String address,@Query("name") String name,@Query("phone") String phone);
}
