package com.hl.indpark.uis.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.CustomCaptureActivity;
import com.hl.indpark.uis.activities.EventsReportActivity;
import com.hl.indpark.uis.activities.LogManagerActivity;
import com.hl.indpark.uis.activities.MachineCheckActivity;
import com.hl.indpark.uis.activities.MachineCheckListActivity;
import com.hl.indpark.uis.activities.NavigationManagerActivity;
import com.hl.indpark.uis.activities.SelfTestActivity;
import com.hl.indpark.uis.activities.SignInActivity;
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
    private UserInfoEvent userInfoEvent;
    private String locAddress = "";

    @OnClick({R.id.ll_one_alrarm, R.id.ll_events, R.id.ll_sign_in, R.id.ll_bjtj, R.id.ll_bjfx, R.id.ll_sys, R.id.ll_self_test, R.id.ll_nav_manager, R.id.ll_log_manager, R.id.ll_home_jc, R.id.ll_sbxj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_one_alrarm:
//                Uri uri = Uri.parse("tel:" + "119");
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(uri);
//                getActivity().startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage( R.string.tip_bluetooth_permission);
                builder.setTitle("提示");
                builder.setPositiveButton(R.string.lab_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallPolice();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.lab_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.ll_events:
                intent = new Intent(getActivity(), EventsReportActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sign_in:
//                showDialog("立即打卡");
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;
            case R.id.ll_bjtj:
                tabLayout.getTabAt(0).select();
                break;
            case R.id.ll_bjfx:
                tabLayout.getTabAt(1).select();
                break;
            case R.id.ll_sys:
                startActivity(new Intent(getActivity(), CustomCaptureActivity.class));
                break;
            case R.id.ll_self_test:
                startActivity(new Intent(getActivity(), SelfTestActivity.class));
                break;
            case R.id.ll_nav_manager:
                startActivity(new Intent(getActivity(), NavigationManagerActivity.class));
                break;
            case R.id.ll_log_manager:
                startActivity(new Intent(getActivity(), LogManagerActivity.class));
                break;
            case R.id.ll_sbxj:
                startActivity(new Intent(getActivity(), MachineCheckActivity.class));
//                startActivity(new Intent(getActivity(), MachineCheckIdActivity.class));
                break;
            case R.id.ll_home_jc:
//                startActivity(new Intent(getActivity(), LineChartWxyFxActivity.class));
//                startActivity(new Intent(getActivity(), LineChartHbActivity.class));
                startActivity(new Intent(getActivity(), MachineCheckListActivity.class));

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
//                getSignIn(String.valueOf(getLongitude),String.valueOf(getLatitude));
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

    public void getSignIn(String lon, String lat) {
        ArticlesRepo.getSignIn(lon, lat).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showToast(getContext(), "打卡成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                if (code == 10017 || code == 10018) {
                    ToastUtil.showToast(getContext(), msg);
                    return;
                } else {
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
    }

    private void initView() {
        final String[] titles = {"", ""};
        Fragment fragment1 = new TabAlarmCountFragment();
        Fragment fragment2 = new TabAlarmCheckFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        loc();
        initData();
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
    }

    public void getCallPolice() {
        ArticlesRepo.getCallPolice(locAddress,userInfoEvent.name,userInfoEvent.phone).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
            ToastUtil.showToast(getContext(),"一键报警成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(getContext(),msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
                userInfoEvent = response.getData();

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
                    Log.e("获取纬度", "onLocationChanged: "+aMapLocation.getLatitude() );
                    Log.e("获取经度", "onLocationChanged: "+aMapLocation.getLongitude() );
                    locAddress = aMapLocation.getAddress();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    public void loc(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }
}
