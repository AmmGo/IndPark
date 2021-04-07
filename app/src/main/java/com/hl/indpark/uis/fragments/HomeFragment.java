package com.hl.indpark.uis.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.EventsReportActivity;
import com.hl.indpark.uis.adapters.ViewPagerAdapter;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.baselib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yjl on 2021/3/8 11:05
 * Function：
 * Desc：首页
 */
public class HomeFragment extends BaseFragment {
    private Unbinder unbinder;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private Intent intent;

    @OnClick({R.id.ll_one_alrarm, R.id.ll_events, R.id.ll_sign_in, R.id.ll_bjtj, R.id.ll_bjfx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_one_alrarm:
//                Intent intent = null;
//                Uri uri = Uri.parse("tel:" + "119");
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ToastUtil.showToast(getContext(),"请到设置中打开电话权限");
//                    intent = new Intent(Settings.ACTION_SETTINGS);
//                    getActivity().startActivity(intent);
//                    return;
//                }
//                intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(uri);
//                getContext().startActivity(intent);
                Uri uri = Uri.parse("tel:" + "119");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(uri);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_events:
                intent = new Intent(getActivity(), EventsReportActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sign_in:
                showDialog("立即打卡");
                break;
            case R.id.ll_bjtj:
                tabLayout.getTabAt(0).select();
                break;
            case R.id.ll_bjfx:
                tabLayout.getTabAt(1).select();
                break;
        }
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_signin, null);
        final TextView logMsg = (TextView) layout.findViewById(R.id.tv_log_msg);
        final TextView logZDl = (TextView) layout.findViewById(R.id.tv_log_zdl);
        final TextView logqx = (TextView) layout.findViewById(R.id.tv_log_qx);
        logMsg.setText(msg);
        builder.setView(layout);
        AlertDialog ad = builder.create();
        ad.setCancelable(false);
        logZDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSignIn(String.valueOf(getLongitude),String.valueOf(getLatitude));
//                getSignIn("105.200795","37.650807");
                ad.dismiss();
            }
        });
        logqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.show();
    }
    public void getSignIn(String lon,String lat) {
        ArticlesRepo.getSignIn(lon,lat).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showToast(getContext(), "打卡成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                if (code==10017||code==10018){
                    ToastUtil.showToast(getContext(), msg);
                    return;
                }else{
                    ToastUtil.showToast(getContext(), "签到失败");
                }
                Util.login(String.valueOf(code), getActivity());
            }
            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(getContext(), "签到失败");
            }
        });
    }
    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tabLayout = root.findViewById(R.id.layout_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager = root.findViewById(R.id.viewpager);
        unbinder = ButterKnife.bind(this, root);
        initView();
        loadData();
        location();
    }

    private void initView() {
        final String[] titles = {"", ""};
        Fragment fragment1 = new TabStatisticsFragment();
        Fragment fragment2 = new TabAnalysisFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void loadData() {
//        ArticlesRepo.getEpAlarm("1").observe(this, new ApiObserver<EPAlarmEvent>() {
//            @Override
////            public void onSuccess(Response<EPAlarmEvent> response) {
////                ToastUtil.showToast(getActivity(), "登录成功");
////            }
////        });
        ArticlesRepo.getHSAlarm("1").observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stopLocation();
        //停止定位  销毁定位客户端。
        mLocationClient.onDestroy();
    }
    private double getLongitude;
    private double getLatitude;
    private AMapLocationClient mLocationClient = null;      //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null; //声明mListener对象，定位监听器
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());

        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
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
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    Log.e("获取纬度", "onLocationChanged: "+aMapLocation.getLatitude() );
                    Log.e("获取经度", "onLocationChanged: "+aMapLocation.getLongitude() );
                    getLongitude = aMapLocation.getLongitude();
                    getLatitude = aMapLocation.getLatitude();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

}
