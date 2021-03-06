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
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.MyMsgAdapter;
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
 * @description 我的消息
 * @date: 2021/3/14 21:46
 * @author: yjl
 */
public class MyMsgActivity extends BaseActivity {
    @BindView(R.id.recy_my_approval)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 20;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<MyMsgEvent.RecordsBean> list;
    private List<MyMsgEvent.RecordsBean> myList;
    private MyMsgAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_msg;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        pageNum = 1;
//        pageSize = 20;
//        list.clear();
//        getData(pageNum, pageSize);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的消息");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        getData(pageNum, pageSize);
        refreshLayout.autoRefresh();//自动刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        pageSize = 20;
                        list.clear();
                        getData(pageNum, pageSize);
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
                        getData(pageNum, pageSize);
                        if (total == 1) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 0);
            }
        });
        initAdapter();
    }

    public void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MyMsgAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    MyMsgEvent.RecordsBean msgBean = (MyMsgEvent.RecordsBean) list.get(position);
                    Intent intent = new Intent(MyMsgActivity.this, MyMsgIdActivity.class);
                    intent.putExtra("Extra_data", msgBean);
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    list.get(i).read = 1;
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    public void getData(int pageNum, int pageSize) {
        ArticlesRepo.getMyMsgEvent(pageNum, pageSize).observe(this, new ApiObserver<MyMsgEvent>() {
            @Override
            public void onSuccess(Response<MyMsgEvent> response) {
                Log.e("我的消息", "onSuccess: ");
                MyMsgEvent event = response.getData();
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
                Util.login(String.valueOf(code), MyMsgActivity.this);
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
