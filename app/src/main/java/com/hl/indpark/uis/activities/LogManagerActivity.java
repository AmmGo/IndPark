package com.hl.indpark.uis.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntNewEp;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.LogManagerAdapter;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.EntDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yjl on 2021/5/10 13:27
 * Function：
 * Desc：日志管理
 */
public class LogManagerActivity extends BaseActivity {
    @BindView(R.id.recy_log_manager)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 20;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<MyMsgEvent.RecordsBean> list;
    private List<MyMsgEvent.RecordsBean> myList;
    private LogManagerAdapter adapter;
    private TabLayout tabLayout;

    @BindView(R.id.chooseText)
    TextView chooseText;
    @BindView(R.id.chooseText2)
    TextView chooseText2;
    private EntDialog pop;
    private PopEvent popEvent;
    String qyid = null;
    String queryDate = null;
    @BindView(R.id.ll_spin)
    LinearLayout ll_spin;
    @OnClick({R.id.ll_spin, R.id.ll_spin2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(LogManagerActivity.this, popEvent, 99);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_spin2:
                List<EntTypeEvent> dateTime = new ArrayList<>();
                dateTime.add(new EntTypeEvent("1", "按月选择"));
                dateTime.add(new EntTypeEvent("2", "按日选择"));
                popEvent.entTypeEvents = dateTime;
                try {
                    pop = new EntDialog(LogManagerActivity.this, popEvent, 101);
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

    @Subscribe
    public void getEntName(EntNameEvent event) {
        chooseText.setText(event.name);
        chooseText2.setText("请选择日期");
        queryDate = null;
        list.clear();
        adapter.notifyDataSetChanged();
        qyid = String.valueOf(event.id);
        getData(pageNum, pageSize);
        pop.cancel();
    }

    @Subscribe
    public void getEntType(EntTypeEvent event) {
        pickerTime(event.id);
        pop.cancel();
    }

    public void pickerTime(String type) {
        //弹出时间选择器选择生日
        if (type.equals("1")) {
            TimePickerView timePickerView = new TimePickerView.Builder(LogManagerActivity.this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(final Date date, View v) {
                    queryDate = getDataTime1(date);
                    chooseText2.setText(queryDate);
                    list.clear();
                    adapter.notifyDataSetChanged();
                    getData(pageNum, pageSize);
                }
            })
                    .setType(TimePickerView.Type.YEAR_MONTH)
                    .setCancelText("取消")
                    .setSubmitText("确定")
                    .setContentSize(20)//滚轮文字大小
                    .setTitleSize(20)//标题文字大小
                    .setOutSideCancelable(true)
                    .isCyclic(true)
                    .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                    .setSubmitColor(Color.GRAY)//确定按钮文字颜色
                    .setCancelColor(Color.GRAY)//取消按钮文字颜色
                    .isCenterLabel(false)
                    .build();
            timePickerView.show();
        } else {
            TimePickerView timePickerView = new TimePickerView.Builder(LogManagerActivity.this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(final Date date, View v) {
                    queryDate = getDataTime(date);
                    chooseText2.setText(queryDate);
                    list.clear();
                    adapter.notifyDataSetChanged();
                    getData(pageNum, pageSize);
                }
            })
                    .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                    .setCancelText("取消")
                    .setSubmitText("确定")
                    .setContentSize(20)//滚轮文字大小
                    .setTitleSize(20)//标题文字大小
                    .setOutSideCancelable(true)
                    .isCyclic(true)
                    .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                    .setSubmitColor(Color.GRAY)//确定按钮文字颜色
                    .setCancelColor(Color.GRAY)//取消按钮文字颜色
                    .isCenterLabel(false)
                    .build();
            timePickerView.show();
        }

    }

    public String getDataTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public String getDataTime1(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    private Calendar calendar = null;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    private void initDateTime() {
        // 获取日历的一个对象
        calendar = Calendar.getInstance();
        // 获取年月日时分秒的信息
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        //设置标题
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_log_manager;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private Map<String, List<EntNewEp.ValueBean>> map;

    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                try {
                    popEvent.entNameEvents = response.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), LogManagerActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("日志管理");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String category = SharePreferenceUtil.getKeyValue("category");
        if (category != null && !category.equals("") && category.equals("2")) {
            ll_spin.setVisibility(View.VISIBLE);
        } else {
            ll_spin.setVisibility(View.GONE);
        }
        initDateTime();
        popEvent = new PopEvent();
        getEntName();
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
        adapter = new LogManagerAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    public void getData(int pageNum, int pageSize) {
        ArticlesRepo.getLogManager(pageNum, pageSize,qyid,queryDate ).observe(this, new ApiObserver<MyMsgEvent>() {
            @Override
            public void onSuccess(Response<MyMsgEvent> response) {
                Log.e("日志管理", "onSuccess: ");
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
                Util.login(String.valueOf(code), LogManagerActivity.this);
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
