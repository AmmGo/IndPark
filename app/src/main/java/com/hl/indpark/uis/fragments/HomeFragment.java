package com.hl.indpark.uis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.EventsReportActivity;
import com.hl.indpark.uis.adapters.ViewPagerAdapter;
import com.hl.indpark.utils.SharePreferenceUtil;

import net.arvin.baselib.base.BaseFragment;

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

    @OnClick({R.id.ll_one_alrarm, R.id.ll_events})
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
//                Uri uri = Uri.parse("tel:" + "119");
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(uri);
//                getActivity().startActivity(intent);
                break;
            case R.id.ll_events:
                intent = new Intent(getActivity(), EventsReportActivity.class);
                startActivity(intent);
                break;
        }
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
        initData();
    }
    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
               UserInfoEvent userInfoEvent = response.getData();
                SharePreferenceUtil.saveKeyValue("userId", String.valueOf(userInfoEvent.personnelId));
                Log.e("登录用户Id", "onSuccess: "+userInfoEvent.personnelId );
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
    private void initView() {
        final String[] titles = {"报警统计", "报警分析"};
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
}
