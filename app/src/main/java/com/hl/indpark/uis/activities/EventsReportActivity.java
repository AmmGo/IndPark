package com.hl.indpark.uis.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.tu.loadingdialog.LoadingDailog;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.ReportEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.new2.Clr;
import com.hl.indpark.entities.new2.Level;
import com.hl.indpark.entities.new2.Ryqr;
import com.hl.indpark.entities.new2.Sbry;
import com.hl.indpark.entities.new2.Wpry;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.permission.DefaultResourceProvider;
import com.hl.indpark.uis.fragments.MediaFragment;
import com.hl.indpark.utils.GlideEngine;
import com.hl.indpark.utils.SelectDialog;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.EntDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.permissionhelper.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EventsReportActivity extends BaseActivity {
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_clr)
    TextView tv_clr;
    @BindView(R.id.tv_level)
    TextView tv_level;
    @BindView(R.id.ed_event_titile)
    EditText ed_event_titile;
    @BindView(R.id.ed_event_decs)
    EditText ed_event_decs;
    boolean islMaxCount;
    private MediaFragment mediaFragment;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> mediaList = new ArrayList<>();
    private ReportEvent eventReport;
    private Clr eventClr;
    private Level eventLevel;
    private String imgS;
    private String uploadEventTitile;
    private String uploadEventDecs;
    private String uploadTvType;
    private String uploadTvClr;
    private String uploadTvLevel;
    private LoadingDailog dialog;
    private double getLongitude;
    private double getLatitude;
    private String eventAddress;

    @OnTextChanged(value = R.id.ed_event_decs, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        tvCount.setText(detailLength + "/300");
        if (detailLength == 299) {
            islMaxCount = true;
        }
        if (detailLength == 300 && islMaxCount) {
            ToastUtil.showToast(EventsReportActivity.this, "最多输入300个字");
            islMaxCount = false;
        }
    }

    @BindView(R.id.img_add)
    ImageView imageView;
    PopEvent popEvent = new PopEvent();
    EntDialog pop;

    @OnClick({R.id.img_add, R.id.tv_report, R.id.ll_type, R.id.ll_level, R.id.ll_clr})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.img_add:
//                addImageDialog(PHOTO, PHOTOLIB);

                permissionUtil.request("需要文件读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                    @Override
                    public void callback(boolean granted, boolean isAlwaysDenied) {
                        if (granted) {
                            PictureSelector.create(EventsReportActivity.this)
                                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                    .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                                    .maxSelectNum(6)// 最大图片选择数量 int
                                    .minSelectNum(1)// 最小选择数量 int
                                    .imageEngine(GlideEngine.createGlideEngine())
                                    .selectionData(mediaList)
                                    .imageSpanCount(3)// 每行显示个数 int
                                    .isCamera(true)// 是否显示拍照按钮 true or false
                                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                    .isEnableCrop(false)// 是否裁剪 true or false
                                    .isCompress(true)// 是否压缩 true or false
                                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                        }
                    }
                });
                break;
            case R.id.tv_report:
                Util.hideInputManager(EventsReportActivity.this, v);
//                getUpdateUserImg(uploadFile.get(0));
//                getUploadEvent();
                uploadTvType = tvType.getText().toString().replaceAll(" ", "").replace("请选择事件类型", "");
                uploadTvLevel = tv_level.getText().toString().replaceAll(" ", "").replace("请选择级别", "");
                uploadTvClr = tv_clr.getText().toString().replaceAll(" ", "").replace("请选择处理人", "");
                uploadEventTitile = ed_event_titile.getText().toString().replaceAll(" ", "");
                uploadEventDecs = ed_event_decs.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(uploadTvType)) {
                    ToastUtil.showToast(this, "请选择事件类型");
                    return;
                }
                if (TextUtils.isEmpty(uploadTvLevel)) {
                    ToastUtil.showToast(this, "请选择级别");
                    return;
                }
                if (TextUtils.isEmpty(uploadTvClr)) {
                    ToastUtil.showToast(this, "请选择处理人");
                    return;
                }

                if (TextUtils.isEmpty(uploadEventTitile)) {
                    ToastUtil.showToast(this, "请输入事件标题");
                    return;
                }
                if (TextUtils.isEmpty(uploadEventDecs)) {
                    ToastUtil.showToast(this, "请输入事件描述");
                    return;
                }
                if (mediaList != null && mediaList.size() > 0) {
                    try {
                        for (int i = 0; i < mediaList.size(); i++) {
                            uploadFile.add(new File(mediaList.get(i).getCompressPath()));
                        }
                    } catch (Exception e) {
                        for (int i = 0; i < mediaList.size(); i++) {
                            uploadFile.add(new File(mediaList.get(i).getPath()));
                        }
                        e.printStackTrace();
                    }
                    getUpdateUserImgS(uploadFile);
                } else {
                    getUploadEvent();
                }

                break;
            case R.id.ll_type:
                try {
                    pop = new EntDialog(EventsReportActivity.this, popEvent, 102);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_level:
                try {
                    pop = new EntDialog(EventsReportActivity.this, popEvent, 106);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_clr:
                try {
                    pop = new EntDialog(EventsReportActivity.this, popEvent, 107);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    @Subscribe
    public void getEntType(ReportTypeEvent event) {
        tvType.setText(event.name);
        eventReport.id = event.id;
        pop.cancel();
    }

    @Subscribe
    public void getLevel(Level event) {
        tv_level.setText(event.name);
        eventLevel.id = event.id;
        pop.cancel();
    }

    @Subscribe
    public void getClr(Clr event) {
        tv_clr.setText(event.name);
        eventClr.id = event.id;
        pop.cancel();
    }

    public void initLevel() {
        List<Level> levels = new ArrayList<>();
        levels.add(new Level("一般", 1));
        levels.add(new Level("较大", 2));
        levels.add(new Level("重大", 3));
        levels.add(new Level("特别重大", 4));
        popEvent.LevelList = levels;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_events;
    }

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    Log.e("获取纬度", "onLocationChanged: " + aMapLocation.getLatitude());
                    Log.e("获取经度", "onLocationChanged: " + aMapLocation.getLongitude());
                    getLongitude = aMapLocation.getLongitude();
                    getLatitude = aMapLocation.getLatitude();
                    eventAddress = aMapLocation.getAoiName();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        initPermissionConfig();
        uploadFile = new ArrayList<>();
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("事件上报");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getRyqr();
        initLevel();
        initData();
        setMediaFragment();
        eventReport = new ReportEvent();
        eventClr = new Clr();
        eventLevel = new Level();
    }

    private PermissionUtil permissionUtil;
    private static String PHOTO = "拍照";
    private static String PHOTOLIB = "相册";

    /**
     * 设置fragment
     */
    private void setMediaFragment() {
        mediaFragment = new MediaFragment();
        mediaFragment.setActivity(this, selectList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }

    private void initPermissionConfig() {
        PermissionUtil.setPermissionTextProvider(new DefaultResourceProvider());
        permissionUtil = new PermissionUtil.Builder().with(this).build();
    }

    private List<File> uploadFile = new ArrayList<>();

    private void addImageDialog(String one, String two) {
        List<String> names = new ArrayList<>();
        names.add(one);
        names.add(two);
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        permissionUtil.request("需要拍照权限", Manifest.permission.CAMERA, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
                                    if (mediaList != null && mediaList.size() == 6) {
                                        ToastUtil.showToast(EventsReportActivity.this, "你最多只能选择6张照片");
                                        return;
                                    }
                                    PictureSelector.create(EventsReportActivity.this)
                                            .openCamera(PictureMimeType.ofImage())
                                            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                            .forResult(PictureConfig.REQUEST_CAMERA);
                                }
                            }
                        });
                        break;
                    case 1:
                        permissionUtil.request("需要文件读写权限", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
                                    PictureSelector.create(EventsReportActivity.this)
                                            .openGallery(PictureMimeType.ofImage())
                                            .isCamera(false)
                                            .minimumCompressSize(100)// 是否压缩
                                            .isCompress(true)
                                            .maxSelectNum(6)
                                            .selectionData(mediaList)
                                            .synOrAsy(false)
                                            .compressQuality(60)
                                            .loadImageEngine(GlideEngine.createGlideEngine())
                                            .selectionMode(PictureConfig.MULTIPLE)
                                            .forResult(PictureConfig.CHOOSE_REQUEST);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(EventsReportActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!EventsReportActivity.this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = new ArrayList<>();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.clear();
                    mediaList.addAll(selectList);
                    mediaFragment.setActivity(this, mediaList);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    // 结果回调
                    selectList = new ArrayList<>();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.addAll(selectList);
                    mediaFragment.setActivity(this, mediaList);
                    break;
                default:
            }
        }
    }

    public void initData() {
        ArticlesRepo.getReportType().observe(this, new ApiObserver<List<ReportTypeEvent>>() {
            @Override
            public void onSuccess(Response<List<ReportTypeEvent>> response) {
                popEvent.reportTypeEventList = response.getData();
                Log.e("事件类型", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });


    }

    public void getUploadEvent() {
        if (mediaList != null && mediaList.size() > 0) {

        } else {
            LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(EventsReportActivity.this)
                    .setMessage("提交中...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog = loadBuilder.create();
            dialog.getWindow().setDimAmount(0f);
            dialog.show();
        }

        Map<String, String> paramMap = new HashMap<>();
        /**
         * 通用参数配置
         * */
        paramMap.put("name", uploadEventTitile);
        paramMap.put("eventType", String.valueOf(eventReport.id));
        paramMap.put("level", String.valueOf(eventLevel.id));
        paramMap.put("createTime", Util.stampToDate(String.valueOf(System.currentTimeMillis())));
        paramMap.put("type", String.valueOf(1));
        paramMap.put("assignId", String.valueOf(eventClr.id));
        paramMap.put("handlePersonId", String.valueOf(eventClr.id));
//        paramMap.put("联系电话", uploadEventTitile);
        paramMap.put("address", eventAddress);
        paramMap.put("longitude", String.valueOf(getLongitude));
        paramMap.put("latitude", String.valueOf(getLatitude));
        paramMap.put("detail", uploadEventDecs);
        paramMap.put("content", uploadEventDecs);
        if (imgS != null && !imgS.equals("")) {
            paramMap.put("image", imgS);
        }
        ArticlesRepo.getReportEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上报事件", "onSuccess: ");
                ToastUtil.showToast(EventsReportActivity.this, "提交成功");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(EventsReportActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), EventsReportActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }

    public void getUpdateUserImg(File file) {
        Map<String, String> paramMap = new HashMap<>();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part MultipartFile =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        ArticlesRepo.getUploadImg(MultipartFile).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上传一张图片", "onSuccess: " + response.getData());
//                2021/03/12/d900ef89-a365-4172-9719-1406ae2f8287.jpg
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {

        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
//            //设置文件的类型
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//            //file就是上传文件的参数类型,后面的file.getName()就是你上传的文件,首先要拿到文件的地址
//            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            parts.add(part);
        }
        return parts;
    }

    public void getUpdateUserImgS(List<File> files) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(EventsReportActivity.this)
                .setMessage("提交中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        List<MultipartBody.Part> partList = filesToMultipartBodyParts(files);
        Log.e("fdas", "getUpdateUserImgS: ");
        ArticlesRepo.getUploadImgS(partList).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上传多张图片", "onSuccess: " + response.getData());
                imgS = response.getData();
                getUploadEvent();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void getRyqr() {
        ArticlesRepo.getRyqr().observe(this, new ApiObserver<Ryqr>() {
            @Override
            public void onSuccess(Response<Ryqr> response) {
                Ryqr ryqr = response.getData();
                if (ryqr.isDep) {
                    getWpry();
                } else {
                    getSbry();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);
                if (false) {
                    getWpry();
                } else {
                    getSbry();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (false) {
                    getWpry();
                } else {
                    getSbry();
                }
            }
        });
    }

    public void getWpry() {
        ArticlesRepo.getWpry().observe(this, new ApiObserver<List<Wpry>>() {
            @Override
            public void onSuccess(Response<List<Wpry>> response) {
                List<Wpry> wpry = wpryTest();
                List<Clr> clrs = new ArrayList<>();
                for (int i = 0; i < wpry.size(); i++) {
                    clrs.add(new Clr(wpry.get(i).name, wpry.get(i).id));
                }
                popEvent.clrList = clrs;

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    public void getSbry() {
        ArticlesRepo.getSbry().observe(this, new ApiObserver<List<Sbry>>() {
            @Override
            public void onSuccess(Response<List<Sbry>> response) {
                List<Sbry> sbry = sbryTest();
                List<Clr> clrs = new ArrayList<>();
                for (int i = 0; i < sbry.size(); i++) {
                    clrs.add(new Clr(sbry.get(i).name, sbry.get(i).id));
                }
                popEvent.clrList = clrs;
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventsReportActivity.this);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    public List<Sbry> sbryTest() {
        List<Sbry> sbries = new ArrayList<>();
        sbries.add(new Sbry("昭仪", 1));
        sbries.add(new Sbry("挽茨", 2));
        sbries.add(new Sbry("刘骅骝", 3));
        sbries.add(new Sbry("晋王及", 4));
        return sbries;
    }

    public List<Wpry> wpryTest() {
        List<Wpry> sbries = new ArrayList<>();
        sbries.add(new Wpry("昭仪", 1));
        sbries.add(new Wpry("挽茨", 2));
        sbries.add(new Wpry("刘骅骝", 3));
        sbries.add(new Wpry("晋王及", 4));
        return sbries;
    }
}
