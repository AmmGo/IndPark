package com.hl.indpark.uis.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
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
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yjl on 2021/5/10 16:15
 * Function：
 * Desc：导航管理
 */
public class NavigationManagerActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mMapView;
    private AMap aMap;


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
        aMap = mMapView.getMap();

        aMap.setTrafficEnabled(true);// 显示实时交通状况
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 卫星地图模式
    // 绑定 Marker 被点击事件
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng lat) {
            }
        });
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
                list =response.getData();
                if (list.size()>0){
                    for (int i = 0; i < list.size();i++){
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude,list.get(i).longitude))
                                .title(list.get(i).name)
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),R.drawable.ep_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(0).latitude,list.get(0).longitude),8,0,30));
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
                list =response.getData();
                if (list.size()>0){
                    for (int i = 0; i < list.size();i++){
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude,list.get(i).longitude))
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),R.drawable.check_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(0).latitude,list.get(0).longitude),8,0,30));
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

    public void getEventPoint() {
        ArticlesRepo.getEventPoint().observe(this, new ApiObserver<List<MapPointEvent>>() {
            @Override
            public void onSuccess(Response<List<MapPointEvent>> response) {
                Log.e("成功", "onSuccess: ");
                List<MapPointEvent> list = new ArrayList<>();
                list =response.getData();
                if (list.size()>0){
                    for (int i = 0; i < list.size();i++){
                        Marker marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).latitude,list.get(i).longitude))
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),R.drawable.event_marker_point)))
                                .draggable(true));
                    }
                    //改变可视区域为指定位置
                    //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                    cameraUpdate= CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(list.get(0).latitude,list.get(0).longitude),8,0,30));
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
