package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EntSHSAdapter;
import com.hl.indpark.widgit.EntDialog;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PieChartSHDataActivity extends BaseActivity {
    @BindView(R.id.recy_pie_data)
    RecyclerView mRcyPieData;
    @BindView(R.id.arrow)
    ImageView arrowImageView;
    @BindView(R.id.chooseText)
    TextView chooseText;
    @BindView(R.id.chooseText2)
    TextView chooseText2;
    private EntDialog pop;
    private String enterpriseId;
    private List<EntSHSEvent> entSHSEvents = new ArrayList<>();
    private EntSHSAdapter adapter;
    private PopEvent popEvent;
    private TabLayout tabLayout;

    @OnClick({R.id.ll_spin, R.id.ll_spin2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(PieChartSHDataActivity.this, popEvent, 100);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_spin2:
                try {
                    pop = new EntDialog(PieChartSHDataActivity.this, popEvent, 101);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pie_chart_ep_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int typeEvent = intent.getIntExtra("type", 0);
        String titleText = "";
        popEvent = new PopEvent();
        switch (typeEvent) {
            case 0:
                titleText = "实时数据";
                break;
            case 1:
                titleText = "环保实时数据";
                break;
            case 2:
                titleText = "危险源实时数据";
                break;
            case 3:
                titleText = "环保实时数据";
                break;
            case 4:
                titleText = "危险源实时数据";
                break;
            default:
        }
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText(titleText);
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
                        adapter.setNewData(entSHSEvents);
                        break;
                    case 1:
                        //正常
                        adapter.setNewData(getNewData("0"));
                        break;
                    case 2:
                        //高高报
                        adapter.setNewData(getNewData("1"));
                        break;
                    case 3:
                        //高报
                        adapter.setNewData(getNewData("2"));
                        break;
                    case 4:
                        //低低报
                        adapter.setNewData(getNewData("4"));
                        break;
                    case 5:
                        //低报
                        adapter.setNewData(getNewData("3"));
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
        getEntName();
        initAdapter();
    }

    public List<EntSHSEvent> getNewData(String px) {
        List<EntSHSEvent> newData = new ArrayList<>();
        for (int i = 0; i < entSHSEvents.size(); i++) {
            if (px.equals(entSHSEvents.get(i).type)) {
                newData.add(entSHSEvents.get(i));
            }

        }
        return newData;
    }

    public void initAdapter() {
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcyPieData.setLayoutManager(layoutManager);
        //创建适配器
        adapter = new EntSHSAdapter(entSHSEvents);
        //给RecyclerView设置适配器
        mRcyPieData.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                popEvent.entNameEvents = response.getData();
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

    public void getEntType(int id) {
        ArticlesRepo.getEntTypeEvent(id).observe(this, new ApiObserver<List<EntTypeEvent>>() {
            @Override
            public void onSuccess(Response<List<EntTypeEvent>> response) {
                popEvent.entTypeEvents = response.getData();
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

    public void getEntSHS(String id, String tlid) {
        ArticlesRepo.getEntSHSEvent(id, tlid).observe(this, new ApiObserver<List<EntSHSEvent>>() {
            @Override
            public void onSuccess(Response<List<EntSHSEvent>> response) {
                entSHSEvents.clear();
                entSHSEvents = response.getData();
                adapter.setNewData(entSHSEvents);
                tabLayout.getTabAt(0).select();
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

    @Subscribe
    public void getEntName(EntNameEvent event) {

        chooseText2.setText("请选择工艺");
        entSHSEvents.clear();
        adapter.notifyDataSetChanged();
        chooseText.setText(event.name);
        enterpriseId = event.companyCode;
        getEntType(event.id);
        pop.cancel();
    }

    @Subscribe
    public void getEntType(EntTypeEvent event) {
        chooseText2.setText(event.name);
        getEntSHS(enterpriseId, event.hazardType);
        pop.cancel();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}