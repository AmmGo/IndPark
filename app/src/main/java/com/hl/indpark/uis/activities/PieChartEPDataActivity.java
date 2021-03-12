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
import com.hl.indpark.entities.events.EntSEPEvent;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EntSEPAdapter;
import com.hl.indpark.widgit.EntDialog;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PieChartEPDataActivity extends BaseActivity {
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
    private List<EntSEPEvent.RecordsBean> entSep = new ArrayList<>();
    private EntSEPAdapter adapter;
    private PopEvent popEvent;
    private TabLayout tabLayout;
    private int typeAdapter;

    @OnClick({R.id.ll_spin, R.id.ll_spin2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(PieChartEPDataActivity.this, popEvent, 100);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_spin2:
                try {
                    pop = new EntDialog(PieChartEPDataActivity.this, popEvent, 103);
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
                        adapter.setNewData(entSep);
                        break;
                    case 1:
                        //正常
                        adapter.setNewData(getNewData("0"));
                        break;
                    case 2:
                        //异常
                        adapter.setNewData(getNewData("1"));
                        break;
                    case 3:
                        //超标
                        adapter.setNewData(getNewData("2"));
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

    public List<EntSEPEvent.RecordsBean> getNewData(String px) {
        List<EntSEPEvent.RecordsBean> newData = new ArrayList<>();
        for (int i = 0; i < entSep.size(); i++) {
            if (px.equals(entSep.get(i).isException)) {
                newData.add(entSep.get(i));
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
        adapter = new EntSEPAdapter(entSep);
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
        ArticlesRepo.getEntSEPTypeEvent(id).observe(this, new ApiObserver<List<EntSEPTypeEvent>>() {
            @Override
            public void onSuccess(Response<List<EntSEPTypeEvent>> response) {
                popEvent.entSEPTypeEvents = response.getData();
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

    public void getEntSEP(String id, String tlid) {
        ArticlesRepo.getEntSEPEvent(id, tlid, "1", "20").observe(this, new ApiObserver<EntSEPEvent>() {
            @Override
            public void onSuccess(Response<EntSEPEvent> response) {
                entSep = response.getData().records;
                adapter.getType(typeAdapter);
                adapter.setNewData(entSep);

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

        chooseText2.setText("请选择排口");
        entSep.clear();
        adapter.notifyDataSetChanged();
        chooseText.setText(event.name);
        enterpriseId = String.valueOf(event.psCode);
        getEntType(event.id);
        pop.cancel();
    }

    @Subscribe
    public void getEntType(EntSEPTypeEvent event) {
        chooseText2.setText(event.name);
        if (event.name!=null&&event.name.equals("废气排放口")){
             typeAdapter = 1;
        }else{
            typeAdapter = 2;
        }

        getEntSEP(enterpriseId, event.iocode);
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
