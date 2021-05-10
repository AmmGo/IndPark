package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MapPointEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.map.WalkRouteActivity;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by yjl on 2021/5/10 16:15
 * Function：
 * Desc：导航管理
 */
public class NavigationManagerActivity extends BaseActivity implements LocationSource, AMapLocationListener {
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.ep_point)
    LinearLayout ll_ep_point;
    @BindView(R.id.check_point)
    LinearLayout ll_check_point;
    @BindView(R.id.event_point)
    LinearLayout ll_event_point;
    private AMap aMap;
    private LatLng locLatLng;

    @OnCheckedChanged({R.id.cb_1, R.id.cb_2, R.id.cb_3})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.cb_1:
                if (ischanged) {
                    getEpPoint();
                    ll_ep_point.setBackgroundResource(R.drawable.switch_map_on);
                } else {
                    ll_ep_point.setBackgroundResource(R.drawable.switch_map_off);
                }
                break;
            case R.id.cb_2:
                if (ischanged) {
                    getCheckPoint();
                    ll_check_point.setBackgroundResource(R.drawable.switch_map_on);
                } else {
                    ll_check_point.setBackgroundResource(R.drawable.switch_map_off);
                }
                break;
            case R.id.cb_3:
                if (ischanged) {
                    getEventPoint();
                    ll_event_point.setBackgroundResource(R.drawable.switch_map_on);
                } else {
                    ll_event_point.setBackgroundResource(R.drawable.switch_map_off);
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.ep_point, R.id.check_point, R.id.event_point})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ep_point:
                getEpPoint();
                break;
            case R.id.check_point:
                getCheckPoint();
                break;
            case R.id.event_point:
                getEventPoint();
                break;
            default:
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_map_check_point;
    }

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("导航管理");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        try {
            mMapView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            ViewGroup child = (ViewGroup) mMapView.getChildAt(0);//地图框架
                            child.getChildAt(2).setVisibility(View.GONE);//logo
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        aMap = mMapView.getMap();
        UiSettings settings = aMap.getUiSettings();
        ;
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(false);
        // 是否显示地图方向盘
        settings.setCompassEnabled(false);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 卫星地图模式
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(NavigationManagerActivity.this, WalkRouteActivity.class);
                intent.putExtra("locLat", locLatLng.latitude);
                intent.putExtra("locLng", locLatLng.longitude);
                intent.putExtra("endLat", marker.getPosition().latitude);
                intent.putExtra("endLng", marker.getPosition().longitude);
                startActivity(intent);
                return true;
            }
        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
        initLoc();
    }

    // 定位
    private void initLoc() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(3000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    // 定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
//                    aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getAddress());
//                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    Log.e("定位地址：", buffer.toString());
                    // 记录当前定位的坐标
                    locLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    CameraUpdate cameraUpdate;

    public void getEpPoint() {
        ArticlesRepo.getEpPoint().observe(this, new ApiObserver<List<MapPointEvent>>() {
            @Override
            public void onSuccess(Response<List<MapPointEvent>> response) {
                Log.e("成功", "onSuccess: ");
                List<MapPointEvent> list = new ArrayList<>();
                list = response.getData();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude, list.get(i).longitude))
                                .title(list.get(i).name)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(), R.drawable.ep_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(0).latitude, list.get(0).longitude), 15, 0, 30));
                    aMap.moveCamera(cameraUpdate);//地图移向指定区域

                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), NavigationManagerActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getCheckPoint() {
        ArticlesRepo.getCheckPoint().observe(this, new ApiObserver<List<MapPointEvent>>() {
            @Override
            public void onSuccess(Response<List<MapPointEvent>> response) {
                Log.e("成功", "onSuccess: ");
                List<MapPointEvent> list = new ArrayList<>();
                list = response.getData();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude, list.get(i).longitude))
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(), R.drawable.check_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    try {
                        if (list.size() > 1)
                            cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(1).latitude, list.get(1).longitude), 13, 0, 30));
                        aMap.moveCamera(cameraUpdate);//地图移向指定区域
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), NavigationManagerActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getEventPoint() {
        ArticlesRepo.getEventPoint().observe(this, new ApiObserver<List<MapPointEvent>>() {
            @Override
            public void onSuccess(Response<List<MapPointEvent>> response) {
                Log.e("成功", "onSuccess: ");
                List<MapPointEvent> list = new ArrayList<>();
                list = response.getData();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude, list.get(i).longitude))
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(), R.drawable.event_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(0).latitude, list.get(0).longitude), 12, 0, 30));
                    aMap.moveCamera(cameraUpdate);//地图移向指定区域
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), NavigationManagerActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
}
