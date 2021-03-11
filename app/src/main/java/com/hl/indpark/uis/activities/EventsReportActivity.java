package com.hl.indpark.uis.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.SelfReportEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.permission.DefaultResourceProvider;
import com.hl.indpark.uis.fragments.MediaFragment;
import com.hl.indpark.utils.GlideEngine;
import com.hl.indpark.utils.SelectDialog;
import com.hl.indpark.widgit.EntDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.permissionhelper.PermissionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EventsReportActivity extends BaseActivity {
    @BindView(R.id.tv_count)
    TextView tvCount;
    boolean islMaxCount;
    private MediaFragment mediaFragment;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> mediaList = new ArrayList<>();
    private Location location;

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

    @OnClick({R.id.img_add, R.id.tv_report, R.id.ll_type})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                addImageDialog(PHOTO, PHOTOLIB);
                break;
            case R.id.tv_report:
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

    @Override
    protected int getContentView() {
        return R.layout.activity_one_events;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
        initData();
        setMediaFragment();
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
    private List<File> uploadFile;
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
                                            .maxSelectNum(6)
                                            .loadImageEngine(GlideEngine.createGlideEngine())
                                            .selectionMode(PictureConfig.SINGLE)
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
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.addAll(selectList);
                    uploadFile.clear();
                    mediaFragment.setActivity(this, mediaList);
//                    for (int i = 0; i < mediaList.size(); i++) {
//                        uploadFile.add(new File(mediaList.get(i).getCompressPath()));
//                    }
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
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });

        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
        ArticlesRepo.getSelfReportEvent().observe(this, new ApiObserver<SelfReportEvent>() {
            @Override
            public void onSuccess(Response<SelfReportEvent> response) {
                Log.e("我的上报", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
//    public static final int LOCATION_CODE = 301;
//    private LocationManager locationManager;
//    private String locationProvider = null;
//    private void getLocation () {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //获取所有可用的位置提供器
//        List<String> providers = locationManager.getProviders(true);
//        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            //如果是Network
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                //请求权限
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//            } else {
//                //监视地理位置变化
//                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
//                Location location = locationManager.getLastKnownLocation(locationProvider);
//                if (location != null) {
//                    //输入经纬度
//                    Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else {
//            //监视地理位置变化
//            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
//            Location location = locationManager.getLastKnownLocation(locationProvider);
//            if (location != null) {
//                //不为空,显示地理位置经纬度
//                Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public LocationListener locationListener = new LocationListener() {
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//                //不为空,显示地理位置经纬度
//                Toast.makeText(EventsReportActivity.this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case LOCATION_CODE:
//                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show();
//                    try {
//                        List<String> providers = locationManager.getProviders(true);
//                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//                            //如果是Network
//                            locationProvider = LocationManager.NETWORK_PROVIDER;
//                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
//                            //如果是GPS
//                            locationProvider = LocationManager.GPS_PROVIDER;
//                        }
//                        //监视地理位置变化
//                        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
//                        Location location = locationManager.getLastKnownLocation(locationProvider);
//                        if (location != null) {
//                            //不为空,显示地理位置经纬度
//                            Toast.makeText(EventsReportActivity.this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
//                        }
//                    }catch (SecurityException e){
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        locationManager.removeUpdates(locationListener);
//    }
}
