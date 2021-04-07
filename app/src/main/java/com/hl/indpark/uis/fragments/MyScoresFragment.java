package com.hl.indpark.uis.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyScoresEvent;
import com.hl.indpark.entities.events.ScoresDetailsEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.MyScoresAdapter;
import com.hl.indpark.utils.Util;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;


/**
 * Created by yjl on 2021/4/6 14:41
 * Function：我的积分
 * Desc：
 */
public class MyScoresFragment extends BaseFragment {
    @BindView(R.id.recy_tab_1)
    RecyclerView recyclerView;
    @BindView(R.id.ll_show_scores)
    LinearLayout showscores;
    @BindView(R.id.tv_add_scores)
    TextView tv_add_scores;
    @BindView(R.id.tv_used_scores)
    TextView tv_used_scores;
    @BindView(R.id.tv_no_used_scores)
    TextView tv_no_used_scores;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<MyScoresEvent.RecordsBean> list;
    private List<MyScoresEvent.RecordsBean> myList;
    private MyScoresAdapter adapter;
    private String state = null;

    @Override
    protected int getContentView() {
        return R.layout.fragment_my_scores;
    }

    @OnCheckedChanged({R.id.rb_all, R.id.rb_sr, R.id.rb_zc})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_all:
                if (ischanged) {
                    //全部
                    pageNum = 1;
                    pageSize = 10;
                    list.clear();
                    state = null;
                    getData(pageNum, pageSize, state);
                }
                break;
            case R.id.rb_sr:
                if (ischanged) {
                    //1：收入
                    pageNum = 1;
                    pageSize = 10;
                    list.clear();
                    state = "1";
                    getData(pageNum, pageSize, state);
                }
                break;
            case R.id.rb_zc:
                if (ischanged) {
                    //2：支出
                    pageNum = 1;
                    pageSize = 10;
                    list.clear();
                    state = "2";
                    getData(pageNum, pageSize, state);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
    @Override
    public void onResume() {
        super.onResume();
        getUserScores();
    }
    public void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MyScoresAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }
    public void getUserScores() {
        ArticlesRepo.getScoresDetails().observe(this, new ApiObserver<ScoresDetailsEvent>() {
            @Override
            public void onSuccess(Response<ScoresDetailsEvent> response) {
                tv_add_scores.setText(""+response.getData().accumulatedIntegral);
                tv_used_scores.setText(""+response.getData().usedIntegral);
                tv_no_used_scores.setText(""+response.getData().unusedIntegral);
                showscores.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                showscores.setVisibility(View.GONE);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                showscores.setVisibility(View.GONE);
                super.onError(throwable);

            }
        });
    }

    public void getData(int pageNum, int pageSize, String state) {
        ArticlesRepo.getMyScores(pageNum, pageSize, state).observe(this, new ApiObserver<MyScoresEvent>() {
            @Override
            public void onSuccess(Response<MyScoresEvent> response) {
                Log.e("积分列表", "onSuccess: ");
                MyScoresEvent event = response.getData();
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
                Util.login(String.valueOf(code), getActivity());
                View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
                list.clear();
                adapter.setNewData(list);
                adapter.setEmptyView(emptyView);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
                list.clear();
                adapter.setNewData(list);
                adapter.setEmptyView(emptyView);
                refreshLayout.finishRefresh(false);
            }
        });
    }
}
