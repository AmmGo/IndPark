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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PieChartSH1DataActivity extends BaseActivity {
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
    private List<EntSHSEvent.RecordsBean> list = new ArrayList<>();
    private List<EntSHSEvent.RecordsBean> selectList = new ArrayList<>();
    private EntSHSAdapter adapter;
    private PopEvent popEvent;
    private TabLayout tabLayout;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    private int timeType;
    private String selectType;
    private String gyid;

    @OnClick({R.id.ll_spin, R.id.ll_spin2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(PieChartSH1DataActivity.this, popEvent, 99);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_spin2:
                try {
                    pop = new EntDialog(PieChartSH1DataActivity.this, popEvent, 101);
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
        try {
            Intent intent = getIntent();
            timeType = intent.getIntExtra("timeType", 0);
            String titleText = "";
            qyid = null;
            gyid = null;
            popEvent = new PopEvent();
            switch (timeType) {
                case 0:
                    titleText = "????????????";
                    break;
                case 1:
                    titleText = "?????????????????????";
                    break;
                case 2:
                    titleText = "?????????????????????";
                    break;
                case 3:
                    titleText = "?????????????????????";
                    break;
                case 4:
                    titleText = "?????????????????????";
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
                    pageNum = 1;
                    pageSize = 10;
                    list.clear();
                    switch (tab.getPosition()) {
                        case 0:
                            //??????
                            selectType = null;
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            break;
                        case 1:
                            //?????????
                            selectType = "1";
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            break;
                        case 2:
                            //??????
                            selectType = "2";
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            break;
                        case 3:
                            //?????????
                            selectType = "4";
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
                            break;
                        case 4:
                            //??????
                            selectType = "3";
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
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
            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNum = 1;
                            pageSize = 10;
                            list.clear();
                            getEntSHS(qyid, gyid, pageNum, pageSize, timeType, selectType);
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
        //??????????????????
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcyPieData.setLayoutManager(layoutManager);
        //???????????????
        adapter = new EntSHSAdapter(list);
        //???RecyclerView???????????????
        mRcyPieData.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    int tagOne = 0;

    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                popEvent.entNameEvents = response.getData();
                try {
                    if (response.getData() != null && response.getData().size() == 1) {
                        chooseText.setText(response.getData().get(0).name);
                        qyid = String.valueOf(response.getData().get(0).id);
//                        getEntType(Integer.parseInt(qyid));
                        getEntSHS(qyid, null, pageNum, pageSize, timeType, selectType);
                        tagOne = 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), PieChartSH1DataActivity.this);
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
                Util.login(String.valueOf(code), PieChartSH1DataActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getEntSHS(String id, String tlid, int pageNum, int pageSize, int timeType, String type) {
        try {
            ArticlesRepo.getEntSHSEvent(id, tlid, pageNum, pageSize, timeType, type).observe(this, new ApiObserver<EntSHSEvent>() {
                @Override
                public void onSuccess(Response<EntSHSEvent> response) {
                    EntSHSEvent shsEvent = response.getData();
                    selectList = new ArrayList<>();
                    selectList = shsEvent.records;
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
                    Log.e("???????????????", "onSuccess: \n" + "??????ID" + id + "\n????????????" + tlid + "\n?????????" + pageNum + "\n????????????" + pageSize + "\n????????????" + timeType + "\n??????????????????" + type);


                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), PieChartSH1DataActivity.this);
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

        chooseText2.setText("???????????????");
        list.clear();
        adapter.notifyDataSetChanged();
        chooseText.setText(event.name);
        qyid = String.valueOf(event.id);
        pageNum = 1;
        pageSize =10;
        getEntSHS(qyid, null, pageNum, pageSize, timeType, selectType);
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
}
