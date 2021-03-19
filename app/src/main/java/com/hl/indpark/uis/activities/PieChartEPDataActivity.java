package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EntNewEp;
import com.hl.indpark.entities.events.EntSEPEvent;
import com.hl.indpark.entities.events.NameEp;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.TypeEp;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EntSEPAdapter;
import com.hl.indpark.widgit.EntDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PieChartEPDataActivity extends BaseActivity {
    @BindView(R.id.recy_pie_data)
    RecyclerView mRcyPieData;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.arrow)
    ImageView arrowImageView;
    @BindView(R.id.chooseText)
    TextView chooseText;
    @BindView(R.id.chooseText2)
    TextView chooseText2;
    private EntDialog pop;
    private List<EntSEPEvent.RecordsBean> list = new ArrayList<>();
    private List<EntSEPEvent.RecordsBean> selectList = new ArrayList<>();
    private EntSEPAdapter adapter;
    private PopEvent popEvent;
    private TabLayout tabLayout;
    private int typeAdapter;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    private int timeType;
    private String selectType;
    private String pkid;
    private String qyid;
    private List<EntNewEp.ValueBean> newepsValue;
    private List<String> newEpName;
    private Map<String, List<EntNewEp.ValueBean>> map;
    private String commName;

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
        timeType = intent.getIntExtra("type", 0);
        String titleText = "";
        popEvent = new PopEvent();
        switch (timeType) {
            case 0:
                titleText = "实时数据";
                break;
            case 1:
                titleText = "环保实时数据";
                break;
            case 2:
                titleText = "环保月度实时数据";
                break;
            case 3:
                titleText = "环保季度实时数据";
                break;
            case 4:
                titleText = "环保年度实时数据";
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
                pageNum = 1;
                pageSize = 10;
                list.clear();
                switch (tab.getPosition()) {
                    case 0:
                        //全部
                        selectType = null;
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
                        break;
                    case 1:
                        //高高报
                        selectType = "0";
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
                        break;
                    case 2:
                        //高报
                        selectType = "1";
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
                        break;
                    case 3:
                        //低低报
                        selectType = "2";
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
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
        getEntEp();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
                        refreshLayout.finishRefresh();
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
                        getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
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
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcyPieData.setLayoutManager(layoutManager);
        //创建适配器
        adapter = new EntSEPAdapter(list);
        //给RecyclerView设置适配器
        mRcyPieData.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    public void getEntEp() {
        ArticlesRepo.getEntNewEp().observe(this, new ApiObserver<List<EntNewEp>>() {
            @Override
            public void onSuccess(Response<List<EntNewEp>> response) {
                try {
                    List<EntNewEp> newEpList = response.getData();
                    List<NameEp> nameEp = new ArrayList<>();
                    map = new HashMap<String, List<EntNewEp.ValueBean>>();
                    for (int i = 0; i < newEpList.size(); i++) {
                        nameEp.add(new NameEp(newEpList.get(i).name));
                        map.put(newEpList.get(i).name, newEpList.get(i).value);
                    }
                    EntNewEp.ValueBean value= newEpList.get(0).value.get(0);
                    commName = value.enterpriseName;
                    chooseText.setText( value.enterpriseName);
                    chooseText2.setText(value.name);
                    typeAdapter = value.type;
                    adapter.getType(typeAdapter);
                    popEvent.nameEp=nameEp;
                    popEvent.typeEp=typeEps(commName);
                    qyid = map.get(commName).get(0).psCode;
                    pkid = map.get(commName).get(0).iocode;
                    getEntSEP(value.psCode,value.iocode,pageNum,pageSize,timeType,selectType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

public List<TypeEp> typeEps(String key){
        List<TypeEp> type = new ArrayList<>();
        List<EntNewEp.ValueBean> list = new ArrayList<>();
        list.addAll(map.get(key));
        for (int i =0 ;i<list.size();i++){
            type.add(new TypeEp(list.get(i).name));
        }
        return type;
}
    public void getEntSEP(String qyid, String pkid, int pageNum, int pageSize, int timeType, String selectType) {
        ArticlesRepo.getEntSEPEvent(qyid, pkid, pageNum, pageSize, timeType, selectType).observe(this, new ApiObserver<EntSEPEvent>() {
            @Override
            public void onSuccess(Response<EntSEPEvent> response) {
                EntSEPEvent shsEvent = response.getData();
                selectList = new ArrayList<>();
                selectList = shsEvent.records;
                if (selectList != null && selectList.size() > 0) {
                    list.addAll(selectList);
                    adapter.setNewData(list);
                    total = 0;
                } else {
                    if (list.size() <= 0) {
                        View emptyView = getLayoutInflater().inflate(R.layout.layout_data_empty, (ViewGroup) mRcyPieData.getParent(), false);
                        list.clear();
                        adapter.setNewData(list);
                        adapter.setEmptyView(emptyView);
                    }
                    total = 1;
                }
                Log.e("环保数据", "onSuccess: \n" + "企业ID" + qyid + "\n排口类型" + pkid + "\n第几页" + pageNum + "\n一页数量" + pageSize + "\n事件跨度" + timeType + "\n事件报警类型" + selectType);

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
    public void getEntName(NameEp event) {
        try {
            tabLayout.getTabAt(0).select();
            list.clear();
            adapter.notifyDataSetChanged();
            commName = event.nameEp;
            chooseText.setText(commName);
            typeAdapter = map.get(commName).get(0).type;
            adapter.getType(typeAdapter);
            chooseText2.setText(map.get(commName).get(0).name);
            qyid = map.get(commName).get(0).psCode;
            pkid = map.get(commName).get(0).iocode;
            popEvent.typeEp=typeEps(commName);
            getEntSEP(qyid,pkid,pageNum,pageSize,timeType,selectType);
            pop.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getEntType(TypeEp event) {
        try {
            chooseText2.setText(event.typeEp);
            for (int i = 0 ;i<map.get(commName).size();i++){
                if (event.typeEp.equals(map.get(commName).get(i).name)){
                    typeAdapter = map.get(commName).get(i).type;
                    pkid = map.get(commName).get(i).iocode;
                }
            }
            adapter.getType(typeAdapter);
            list.clear();
            getEntSEP(qyid, pkid, pageNum, pageSize, timeType, selectType);
            pop.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
