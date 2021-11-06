package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.new2.EventFlow;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EventFlowAdapter;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EventFlowActivity extends BaseActivity {

    @BindView(R.id.recy_event_flow)
    RecyclerView recyclerView;
    private List<EventFlow> list;
    private EventFlowAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_event_flow;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("事件处理流程");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list = new ArrayList<>();
        Intent intent = getIntent();
        initAdapter();
        int idEvent = intent.getIntExtra("id", 0);
        getEventFlow(idEvent);
    }

    public void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new EventFlowAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    public void getEventFlow(int id) {
        ArticlesRepo.getEventFlow(String.valueOf(id)).observe(this, new ApiObserver<List<EventFlow>>() {
            @Override
            public void onSuccess(Response<List<EventFlow>> response) {
                list = new ArrayList<>();
                list.addAll(response.getData());
                adapter.setNewData(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventFlowActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
}