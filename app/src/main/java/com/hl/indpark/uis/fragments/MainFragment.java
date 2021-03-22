package com.hl.indpark.uis.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.MyMsgActivity;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yjl on 2021/3/8 10:51
 * Function：
 * Desc：主体
 */
public class MainFragment extends BaseFragment implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private IDrawerToggle drawerToggle;

    private TextView titleBar;
    private TextView tv_hot_god_msg;
    private RelativeLayout rl_msg;
    private BottomNavigationView bottomNavigationView;
    //    R.id.tab_monitor
    private List<Integer> tabIds = Arrays.asList(R.id.tab_home, R.id.tab_maillist, R.id.tab_self);
    private SparseArray<BaseFragment> fragments = new SparseArray<>();
    private SparseArray<Class<? extends BaseFragment>> fragmentClasses = new SparseArray<>();
    private SparseIntArray fragmentTitles = new SparseIntArray();
    private String enterpriseName;

    {
        fragmentClasses.put(R.id.tab_home, HomeFragment.class);
//        fragmentClasses.put(R.id.tab_monitor, MonitorFragment.class);
        fragmentClasses.put(R.id.tab_maillist, MailListFragment.class);
        fragmentClasses.put(R.id.tab_self, SelfFragment.class);

        fragmentTitles.put(R.id.tab_home, R.string.tab_home);
//        fragmentTitles.put(R.id.tab_monitor, R.string.tab_monitor);
        fragmentTitles.put(R.id.tab_maillist, R.string.tab_maillist);
        fragmentTitles.put(R.id.tab_self, R.string.tab_self1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDrawerToggle) {
            drawerToggle = (IDrawerToggle) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerToggle = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        enterpriseName = SharePreferenceUtil.getKeyValue("enterpriseName");
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleBar = root.findViewById(R.id.title);
        rl_msg = root.findViewById(R.id.rl_msg);
        tv_hot_god_msg = root.findViewById(R.id.tv_hot_god_msg);
        rl_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
            }
        });
        enterpriseName = SharePreferenceUtil.getKeyValue("enterpriseName");
        if (enterpriseName != null && !enterpriseName.equals("")) {
            titleBar.setText(enterpriseName);
        }
        bottomNavigationView = root.findViewById(R.id.tab_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(tabIds.get(0));
        getData();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
        hideAll(beginTransaction);
        getData();
        int itemId = item.getItemId();
        if (tabIds.contains(itemId)) {
            if (fragmentTitles.get(itemId) == R.string.tab_home) {
                Log.e("输出title", "onNavigationItemSelected: 一样");
                titleBar.setText(enterpriseName);
            } else {
                titleBar.setText(fragmentTitles.get(itemId));
            }

            if (titleBar.getText().equals("")) {
                titleBar.setVisibility(View.INVISIBLE);
                rl_msg.setVisibility(View.INVISIBLE);
            } else {
                titleBar.setVisibility(View.VISIBLE);
                rl_msg.setVisibility(View.VISIBLE);
            }
            BaseFragment fragment = fragments.get(itemId);
            if (fragment == null) {
                try {
                    fragment = fragmentClasses.get(itemId).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fragment != null) {
                    beginTransaction.add(R.id.layout_content, fragment);
                    fragments.put(itemId, fragment);
                }
            } else {
                beginTransaction.show(fragment);
            }
            beginTransaction.commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    private void hideAll(FragmentTransaction beginTransaction) {
        for (Integer tabId : tabIds) {
            BaseFragment fragment = fragments.get(tabId);
            if (fragment != null) {
                beginTransaction.hide(fragment);
            }
        }
    }

    public interface IDrawerToggle {
        void toggle();
    }

    public void getData() {
        ArticlesRepo.getMsgRead().observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.getData() != null && !response.getData().equals("")) {
                    if (response.getData().equals("0")) {
                        tv_hot_god_msg.setVisibility(View.GONE);
                    } else {
                        tv_hot_god_msg.setText(response.getData());
                        tv_hot_god_msg.setVisibility(View.VISIBLE);
                    }
                }
                Log.e("未读消息", "onSuccess: " + response.getData());
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                tv_hot_god_msg.setVisibility(View.GONE);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                tv_hot_god_msg.setVisibility(View.GONE);
                super.onError(throwable);

            }
        });
    }
}
