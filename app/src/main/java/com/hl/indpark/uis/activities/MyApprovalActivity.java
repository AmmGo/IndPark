package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.MyApprovalAdapter;
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
 * @description 我的审批
 * @date: 2021/3/14 21:46
 * @author: yjl
 */
public class MyApprovalActivity extends BaseActivity {
    @BindView(R.id.recy_my_approval)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<MyApprovalEvent.RecordsBean> list;
    private List<MyApprovalEvent.RecordsBean> myApprovalList;
    private MyApprovalAdapter adapter;
    private TabLayout tabLayout;
    private String state ;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_approval;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的审批");
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
                        state = null;
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
        refreshLayout.autoRefresh();//自动刷新
//        getData(pageNum, pageSize, state);
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
        adapter = new MyApprovalAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyApprovalActivity.this, ReportApprovalActivity.class);
                intent.putExtra("id",list.get(position).id);
                intent.putExtra("reorap",2);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            int resultId = data.getIntExtra("T", 0);
            Log.e("返回成功", "onActivityResult: " + resultId);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == resultId) {
                    list.get(i).status = "1";
                }
            }
            adapter.notifyDataSetChanged();
        }

    }
    public void getData(int pageNum, int pageSize, String state) {
        ArticlesRepo.getMyApprovalEvent(pageNum, pageSize, state).observe(this, new ApiObserver<MyApprovalEvent>() {
            @Override
            public void onSuccess(Response<MyApprovalEvent> response) {
                Log.e("审批列表", "onSuccess: ");
                MyApprovalEvent myApprovalEvent = response.getData();
                myApprovalList = new ArrayList<>();
                myApprovalList = myApprovalEvent.records;
                if (myApprovalList != null && myApprovalEvent.records.size() > 0) {
                    list.addAll(myApprovalList);
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
                Util.login(String.valueOf(code),MyApprovalActivity.this);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshLayout.finishRefresh(false);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
