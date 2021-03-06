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
    @BindView(R.id.ed_event_titile)
    EditText ed_event_titile;
    @BindView(R.id.ed_event_decs)
    EditText ed_event_decs;
    boolean islMaxCount;
    private MediaFragment mediaFragment;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> mediaList = new ArrayList<>();
    private ReportEvent eventReport;
    private String imgS;
    private String uploadEventTitile;
    private String uploadEventDecs;
    private String uploadTvType;
    private LoadingDailog dialog;
    private double getLongitude;
    private double getLatitude;

    @OnTextChanged(value = R.id.ed_event_decs, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        tvCount.setText(detailLength + "/300");
        if (detailLength == 299) {
            islMaxCount = true;
        }
        if (detailLength == 300 && islMaxCount) {
            ToastUtil.showToast(EventsReportActivity.this, "????????????300??????");
            islMaxCount = false;
        }
    }

    @BindView(R.id.img_add)
    ImageView imageView;
    PopEvent popEvent = new PopEvent();
    EntDialog pop;

    @OnClick({R.id.img_add, R.id.tv_report, R.id.ll_type})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.img_add:
//                addImageDialog(PHOTO, PHOTOLIB);

                permissionUtil.request("????????????????????????", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                    @Override
                    public void callback(boolean granted, boolean isAlwaysDenied) {
                        if (granted) {
                            PictureSelector.create(EventsReportActivity.this)
                                    .openGallery(PictureMimeType.ofImage())//??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                                    .theme(R.style.picture_default_style)//????????????(????????????????????????) ????????????demo values/styles??? ?????????R.style.picture.white.style
                                    .maxSelectNum(6)// ???????????????????????? int
                                    .minSelectNum(1)// ?????????????????? int
                                    .imageEngine(GlideEngine.createGlideEngine())
                                    .selectionData(mediaList)
                                    .imageSpanCount(3)// ?????????????????? int
                                    .isCamera(true)// ???????????????????????? true or false
                                    .isZoomAnim(true)// ?????????????????? ???????????? ??????true
                                    .isEnableCrop(false)// ???????????? true or false
                                    .isCompress(true)// ???????????? true or false
                                    .minimumCompressSize(100)// ??????100kb??????????????????
                                    .forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
                        }
                    }
                });
                break;
            case R.id.tv_report:
                Util.hideInputManager(EventsReportActivity.this, v);
//                getUpdateUserImg(uploadFile.get(0));
//                getUploadEvent();
                uploadTvType = tvType.getText().toString().replaceAll(" ", "").replace("?????????????????????","");
                uploadEventTitile = ed_event_titile.getText().toString().replaceAll(" ", "");
                uploadEventDecs = ed_event_decs.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(uploadTvType)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }

                if (TextUtils.isEmpty(uploadEventTitile)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(uploadEventDecs)) {
                    ToastUtil.showToast(this, "?????????????????????");
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
            default:
        }
    }

    @Subscribe
    public void getEntType(ReportTypeEvent event) {
        tvType.setText(event.name);
        eventReport.id = event.id;
        pop.cancel();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_events;
    }

    //??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;
    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;
    //???????????????????????????
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    aMapLocation.getLatitude();//????????????
                    aMapLocation.getLongitude();//????????????
                    Log.e("????????????", "onLocationChanged: "+aMapLocation.getLatitude() );
                    Log.e("????????????", "onLocationChanged: "+aMapLocation.getLongitude() );
                    getLongitude = aMapLocation.getLongitude();
                    getLatitude = aMapLocation.getLatitude();
                }else {
                    //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        //???????????????
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //????????????????????????
        mLocationClient.setLocationListener(mLocationListener);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //???????????????????????????
        //??????????????????false???
        mLocationOption.setOnceLocation(true);
        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        //??????????????????,????????????,?????????2000ms?????????1000ms???
        mLocationOption.setInterval(1000);
        //????????????????????????30000???????????????????????????????????????8000?????????
        mLocationOption.setHttpTimeOut(20000);
        //????????????????????????30000???????????????????????????????????????8000?????????
        mLocationOption.setHttpTimeOut(20000);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
        //????????????
        mLocationClient.startLocation();
        initPermissionConfig();
        uploadFile = new ArrayList<>();
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("????????????");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
        setMediaFragment();
        eventReport = new ReportEvent();
    }

    private PermissionUtil permissionUtil;
    private static String PHOTO = "??????";
    private static String PHOTOLIB = "??????";

    /**
     * ??????fragment
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
                        permissionUtil.request("??????????????????", Manifest.permission.CAMERA, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
                                    if (mediaList != null && mediaList.size() == 6) {
                                        ToastUtil.showToast(EventsReportActivity.this, "?????????????????????6?????????");
                                        return;
                                    }
                                    PictureSelector.create(EventsReportActivity.this)
                                            .openCamera(PictureMimeType.ofImage())
                                            .loadImageEngine(GlideEngine.createGlideEngine()) // ?????????Demo GlideEngine.java
                                            .forResult(PictureConfig.REQUEST_CAMERA);
                                }
                            }
                        });
                        break;
                    case 1:
                        permissionUtil.request("????????????????????????", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void callback(boolean granted, boolean isAlwaysDenied) {
                                if (granted) {
                                    PictureSelector.create(EventsReportActivity.this)
                                            .openGallery(PictureMimeType.ofImage())
                                            .isCamera(false)
                                            .minimumCompressSize(100)// ????????????
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
                    // ????????????
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
                Log.e("????????????", "onSuccess: ");
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
                    .setMessage("?????????...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog = loadBuilder.create();
            dialog.getWindow().setDimAmount(0f);
            dialog.show();
        }

        Map<String, String> paramMap = new HashMap<>();
        /**
         * ??????????????????
         * */
        paramMap.put("title", uploadEventTitile);
        paramMap.put("type", String.valueOf(eventReport.id));
        paramMap.put("content", uploadEventDecs);
        if (imgS != null && !imgS.equals("")) {
            paramMap.put("image", imgS);
        }
        paramMap.put("longitude", String.valueOf(getLongitude));
        paramMap.put("latitude", String.valueOf(getLatitude));

        ArticlesRepo.getReportEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("????????????", "onSuccess: ");
                ToastUtil.showToast(EventsReportActivity.this, "????????????");
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
                Log.e("??????????????????", "onSuccess: " + response.getData());
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
//            //?????????????????????
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//            //file?????????????????????????????????,?????????file.getName()????????????????????????,??????????????????????????????
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
                .setMessage("?????????...")
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
                Log.e("??????????????????", "onSuccess: " + response.getData());
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

}
