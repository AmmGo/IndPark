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
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntSHSEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.WxyEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.EntSHSAdapter;
import com.hl.indpark.utils.Util;
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

public class PieChartSHDataActivity extends BaseActivity {
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
    private String qyid;
    private String companyCode;
    private List<EntSHSEvent> list = new ArrayList<>();
    private List<EntSHSEvent> selectList = new ArrayList<>();
    private EntSHSAdapter adapter;
    private PopEvent popEvent;
    private TabLayout tabLayout;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    private int timeType;
    private String selectType = "0";
    private String gyid;
    private int queryType = 0;
    private Map<String, List<EntSHSEvent>> listMap;
    private List<EntSHSEvent> gblist;
    private List<EntSHSEvent> ggblist;
    private List<EntSHSEvent> dblist;
    private List<EntSHSEvent> ddblist;
    private String getEntName;

    @OnClick({R.id.ll_spin, R.id.ll_spin2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(PieChartSHDataActivity.this, popEvent, 99);
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
        return R.layout.activity_pie_chart_sh_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        listMap = new HashMap<>();
        gblist = new ArrayList<>();
        ggblist = new ArrayList<>();
        dblist = new ArrayList<>();
        ddblist = new ArrayList<>();
        try {
            Intent intent = getIntent();
            timeType = intent.getIntExtra("timeType", 0);
            String titleText = "";
            qyid = null;
            gyid = null;
            popEvent = new PopEvent();
            switch (timeType) {
                case 0:
                    titleText = "实时数据";
                    break;
                case 1:
                    titleText = "危险源实时数据";
                    break;
                case 2:
                    titleText = "危险源月度数据";
                    break;
                case 3:
                    titleText = "危险源季度数据";
                    break;
                case 4:
                    titleText = "危险源年度数据";
                    break;
                default:
            }
            TitleBar titleBar = findViewById(R.id.title_bar);
            titleBar.getCenterTextView().setText(titleText);
            titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            tabLayout = findViewById(R.id.layout_tab_list);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    getEntName = chooseText.getText().toString();
                    pageNum = 1;
                    pageSize = 10;
                    list.clear();
                    switch (tab.getPosition()) {
                        case 0:
                            //全部
//                            selectType = null;
                            selectType = "0";
                            queryType = 1;
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            getWxy(companyCode, selectType, queryType);
                            break;
                        case 1:
                            //高高报
                            selectType = "1";
                            queryType = 1;
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            if (getEntName.equals("全部")) {
                                if (ggblist.size() <= 0) {
                                    View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                                    ggblist.clear();
                                    adapter.setNewData(ggblist);
                                    adapter.setEmptyView(emptyView);
                                } else {
                                    adapter.setNewData(ggblist);
                                }

                            } else {
                                getWxy(companyCode, selectType, queryType);
                            }
                            break;
                        case 2:
                            //高报
                            selectType = "2";
                            queryType = 1;
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            if (getEntName.equals("全部")) {
                                if (gblist.size() <= 0) {
                                    View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                                    gblist.clear();
                                    adapter.setNewData(gblist);
                                    adapter.setEmptyView(emptyView);
                                } else {
                                    adapter.setNewData(gblist);
                                }

                            } else {
                                getWxy(companyCode, selectType, queryType);
                            }
                            break;
                        case 3:
                            //低低报
                            selectType = "4";
                            queryType = 1;
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            if (getEntName.equals("全部")) {
                                if (ddblist.size() <= 0) {
                                    View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                                    ddblist.clear();
                                    adapter.setNewData(ddblist);
                                    adapter.setEmptyView(emptyView);
                                } else {
                                    adapter.setNewData(ddblist);
                                }

                            } else {
                                getWxy(companyCode, selectType, queryType);
                            }
                            break;
                        case 4:
                            //低报
                            selectType = "3";
                            queryType = 1;
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            if (getEntName.equals("全部")) {
                                if (dblist.size() <= 0) {
                                    View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                                    dblist.clear();
                                    adapter.setNewData(dblist);
                                    adapter.setEmptyView(emptyView);
                                } else {
                                    adapter.setNewData(dblist);
                                }

                            } else {
                                getWxy(companyCode, selectType, queryType);
                            }
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
//            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNum = 1;
                            pageSize = 10;
                            list.clear();
//                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            getWxy(companyCode,selectType,queryType);
                            refreshLayout.finishRefresh();
                        }
                    }, 50);
                }
            });
            refreshLayout.setEnableLoadMore(false);
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNum += 1;
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initAdapter() {
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcyPieData.setLayoutManager(layoutManager);
        //创建适配器
        adapter = new EntSHSAdapter(list);
        //给RecyclerView设置适配器
        mRcyPieData.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EntSHSEvent jumpData = (EntSHSEvent) adapter.getItem(position);
                Intent intent = new Intent(PieChartSHDataActivity.this, LineChartWxyTjActivity.class);
                intent.putExtra("labelId", String.valueOf(jumpData.labelId));
                String dw_str = jumpData.value;
                if (dw_str!=null&&!dw_str.equals("")){
                    dw_str = dw_str.replace(".","").replaceAll("[\\d]+","").replace("-","");
                }else{
                    dw_str = "";
                }
                intent.putExtra("dw_str", dw_str);
                intent.putExtra("tv_pk_name", jumpData.pointName);
                intent.putExtra("dw_name", jumpData.dataType);
                startActivity(intent);
            }
        });
    }

    int tagOne = 0;

    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                List<EntNameEvent> list = new ArrayList<>();
                if (Util.getIsRoleld()){
                    EntNameEvent entNameEvent = new EntNameEvent();
                    entNameEvent.companyCode = "1";
                    entNameEvent.name = "全部";
                    entNameEvent.psCode = "1";
                    entNameEvent.id = 1;
                    list.add(entNameEvent);
                    if (response.getData().size()>0){
                        for (int i = 0 ;i<response.getData().size();i++){
                            list.add(response.getData().get(i));
                        }
                    }
                }else{
                    list = response.getData();
                }

                popEvent.entNameEvents = list;
//                try {
//                    if (response.getData() != null && response.getData().size() == 1) {
//                        chooseText.setText(response.getData().get(0).name);
//                        qyid = String.valueOf(response.getData().get(0).id);
////                        getEntType(Integer.parseInt(qyid));
//                        getEntSHS(qyid, null, pageNum, pageSize, timeType, selectType);
//                        tagOne = 1;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                /**修改之后内容*/
                try {
                    if (response.getData() != null) {

                        companyCode = String.valueOf(response.getData().get(0).id);
                        if (response.getData().size()==1){
                            queryType=1;
                            chooseText.setText(response.getData().get(0).name);
                        }else{
                            queryType=0;
                            chooseText.setText("全部");
                        }
                        getWxy(companyCode, selectType,queryType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), PieChartSHDataActivity.this);
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
                if (response.getData() != null && response.getData().size() > 0) {
                    chooseText2.setText(response.getData().get(0).name);
                    gyid = response.getData().get(0).id;

//                    getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), PieChartSHDataActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getEntSHS(String id, String tlid, int pageNum, int pageSize, int timeType, String type) {
        try {
            ArticlesRepo.getEntSHSEvent(id, tlid, pageNum, pageSize, timeType, type).observe(this, new ApiObserver<List<EntSHSEvent>>() {
                @Override
                public void onSuccess(Response<List<EntSHSEvent>> response) {
                    List<EntSHSEvent> shsEvent = response.getData();
                    selectList = new ArrayList<>();
                    selectList = shsEvent;
                    if (selectList != null && selectList.size() > 0) {
                        list.addAll(selectList);
                        adapter.setNewData(list);
                        total = 0;
                    } else {
                        if (list.size() <= 0) {
                            View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                            list.clear();
                            adapter.setNewData(list);
                            adapter.setEmptyView(emptyView);
                        }
                        total = 1;
                    }
                    Log.e("危险源数据", "onSuccess: \n" + "企业ID" + id + "\n工艺类型" + tlid + "\n第几页" + pageNum + "\n一页数量" + pageSize + "\n事件跨度" + timeType + "\n事件报警类型" + type);


                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), PieChartSHDataActivity.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getEntName(EntNameEvent event) {

        chooseText2.setText("请选择工艺");
        list.clear();
        adapter.notifyDataSetChanged();
        chooseText.setText(event.name);
        qyid = String.valueOf(event.id);
        pageNum = 1;
        pageSize =10;
//        getEntSHS(qyid, null, pageNum, pageSize, timeType, selectType);
        companyCode= String.valueOf(event.id);
        if (event.name.equals("全部")){
            queryType = 0;
        }else{
            queryType = 1;
        }
        getWxy(companyCode,selectType,queryType);
//        getEntType(event.id);
        pop.cancel();
    }

    @Subscribe
    public void getEntType(EntTypeEvent event) {
        chooseText2.setText(event.name);
        gyid = event.id;
        list.clear();
        getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
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

    public void getWxy(String id,String selectType,int queryType) {
        getEntName = chooseText.getText().toString();
        if (getEntName.equals("全部")) {
            queryType = 0;
        }
        try {
            ArticlesRepo.getWxyEvent(id, queryType,1).observe(this, new ApiObserver<List<WxyEvent>>() {
                @Override
                public void onSuccess(Response<List<WxyEvent>> response) {
                    List<WxyEvent> wxyEvents = new ArrayList<>();
                    wxyEvents = response.getData();
                    list = new ArrayList<>();
                    if (wxyEvents != null && wxyEvents.size() > 0) {
                        if (selectType.equals("0")) {
                            for (int i = 0; i < wxyEvents.size(); i++) {
                                EntSHSEvent data1 = new EntSHSEvent();
                                data1.type = wxyEvents.get(i).type;
                                data1.pointName = wxyEvents.get(i).pointName;
                                data1.enterpriseName = wxyEvents.get(i).enterpriseName;
                                data1.value = wxyEvents.get(i).value;
                                data1.time = wxyEvents.get(i).time;
                                data1.tagId = wxyEvents.get(i).labelId;
                                data1.labelId = wxyEvents.get(i).labelId;
                                list.add(data1);
                            }
                        }else{
                            for (int i = 0; i < wxyEvents.size(); i++) {
                                if (selectType.equals(wxyEvents.get(i).type)){
                                    EntSHSEvent data1 = new EntSHSEvent();
                                    data1.type = wxyEvents.get(i).type;
                                    data1.pointName = wxyEvents.get(i).pointName;
                                    data1.enterpriseName = wxyEvents.get(i).enterpriseName;
                                    data1.value = wxyEvents.get(i).value;
                                    data1.time = wxyEvents.get(i).time;
                                    data1.tagId = wxyEvents.get(i).labelId;
                                    data1.labelId = wxyEvents.get(i).labelId;
                                    list.add(data1);
                                }
                            }
                        }

                        if (list.size() > 0) {
                            gblist = new ArrayList<>();
                            ggblist = new ArrayList<>();
                            dblist = new ArrayList<>();
                            ddblist = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).type.equals("1")) {
                                    ggblist.add(list.get(i));
                                } else if (list.get(i).type.equals("2")) {
                                    gblist.add(list.get(i));
                                } else if (list.get(i).type.equals("3")) {
                                    dblist.add(list.get(i));
                                } else if (list.get(i).type.equals("4")) {
                                    ddblist.add(list.get(i));
                                }
                            }
                            listMap.put("1", ggblist);
                            listMap.put("2", gblist);
                            listMap.put("3", dblist);
                            listMap.put("4", ddblist);
                            adapter.setNewData(list);
                        } else {
                            View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                            list.clear();
                            adapter.setNewData(list);
                            adapter.setEmptyView(emptyView);
                        }
                    } else {
                        View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRcyPieData.getParent(), false);
                        list.clear();
                        adapter.setNewData(list);
                        adapter.setEmptyView(emptyView);
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), PieChartSHDataActivity.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
