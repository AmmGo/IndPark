package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyPeportEvent;
import com.hl.indpark.entities.new2.EventPageList;
import com.hl.indpark.entities.new2.SbEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EntSEPAdapter;
import com.hl.indpark.uis.adapters.MyReportAdapter;
import com.hl.indpark.utils.Util;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @description 我的上报
 * @date: 2021/3/14 21:46
 * @author: yjl
 */
public class MyReportActivity extends BaseActivity {
    @BindView(R.id.recy_my_approval)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<SbEvent.RecordsBean> list;
    private List<SbEvent.RecordsBean> myList;
    private MyReportAdapter adapter;
    private TabLayout tabLayout;
    private String state = "0";

    @Override
    protected int getContentView() {
        return R.layout.activity_my_report;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的上报");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout = findViewById(R.id.layout_tab_list);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //全部
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        state = "0";
                        getData(pageNum, pageSize, state);
                        break;
                    case 1:
                        //已处理
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        state = "1";
                        getData(pageNum, pageSize, state);
                        break;
                    case 2:
                        //未处理
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        state = "2";
                        getData(pageNum, pageSize, state);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        getData(pageNum, pageSize, state);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        getData(pageNum, pageSize, state);
                    }
                }, 50);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum += 1;
                        getData(pageNum, pageSize, state);
                        if (total == 1) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 50);
            }
        });
        initAdapter();
    }

    public void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MyReportAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    Intent intent = new Intent(MyReportActivity.this, ReportApprovalActivity.class);
                    intent.putExtra("id",list.get(position).id);
                    intent.putExtra("reorap",1);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getData(int pageNum, int pageSize, String state) {
        ArticlesRepo.getSbEvent(pageNum, pageSize, state).observe(this, new ApiObserver<SbEvent>() {
            @Override
            public void onSuccess(Response<SbEvent> response) {
                Log.e("上报列表", "onSuccess: ");
                SbEvent event = response.getData();
                myList = new ArrayList<>();
                myList = event.records;
                if (myList != null && event.records.size() > 0) {
                    list.addAll(myList);
                    adapter.setNewData(list);
                    total = 0;
                } else {
                    if (list.size() <= 0) {
                        View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
                        list.clear();
                        adapter.setNewData(list);
                        adapter.setEmptyView(emptyView);
                    }
                    total = 1;
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code),MyReportActivity.this);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshLayout.finishRefresh(false);
            }
        });
    }
}
