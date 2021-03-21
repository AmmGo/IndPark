package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartSHDataActivity;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.PieChartView;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.OnCheckedChanged;


/**
 * Created by yjl on 2021/3/9 13:19
 * Function：
 * Desc：报警分析---危险源
 */
public class TabAHSFragment extends BaseFragment {

    private PieChartView mPieChart;
    private ArrayList<PieEntry> data = new ArrayList<PieEntry>();
    private PieData pieData;
    private String type = "2";
    private int typeData = 2;
    private LinearLayout linearLayout;

    @OnCheckedChanged({R.id.rg_month, R.id.rg_quarter, R.id.rg_year})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rg_month:
                if (ischanged) {
                    type = "2";
                    typeData = Util.wxyMonthly;
                    initData(type);
                }
                break;
            case R.id.rg_quarter:
                if (ischanged) {
                    type = "3";
                    typeData = Util.wxyQuarter;
                    initData(type);
                }
                break;
            case R.id.rg_year:
                if (ischanged) {
                    type = "4";
                    typeData = Util.wxyYear;
                    initData(type);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_ahs;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPieChart = root.findViewById(R.id.chart_sep);
        linearLayout = root.findViewById(R.id.ll_gone);
        initData(type);
        mPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PieChartSHDataActivity.class);
                intent.putExtra("timeType",typeData);
                startActivity(intent);
            }
        });
    }

    private void initData(String ty) {
        ArticlesRepo.getHSAlarm(ty).observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
                try {
                    if (response != null && response.getData() != null) {
                        HSAlarmEvent alarmEvent = response.getData();
                        List<HSAlarmEvent.ValueBean> valueBeanList = new ArrayList<>();
                        valueBeanList.addAll(alarmEvent.value);
                        Comparator<HSAlarmEvent.ValueBean> comparator = new Comparator<HSAlarmEvent.ValueBean>() {
                            public int compare(HSAlarmEvent.ValueBean s1, HSAlarmEvent.ValueBean s2) {
                                // 先排年龄
                                if (s1.num != s2.num) {
                                    return s1.num - s2.num;
                                } else if (s1.type != s2.type) {
                                    // 年龄相同则按姓名排序
                                    return s1.type - (s2.type);
                                } else {
                                    return 0;
                                }
                            }
                            ;
                        };
                        Collections.sort(valueBeanList, comparator);
                        double[] datas = new double[valueBeanList.size()];
                        String[] texts = new String[valueBeanList.size()];
                        String[] strs = new String[valueBeanList.size()];
                        for (int i = 0; i < valueBeanList.size(); i++) {
                            datas[i] = valueBeanList.get(i).num;
                            texts[i] = valueBeanList.get(i).num+","+HSAlarmEvent.getType(valueBeanList.get(i).type);
                            strs[i] = HSAlarmEvent.getType(valueBeanList.get(i).type);
                        }
                        mPieChart.setStrList(strs);
                        mPieChart.setDatas(datas);
                        mPieChart.setTexts(texts);
                        mPieChart.setMaxNum(datas.length);
                        mPieChart.setCenterText(alarmEvent.key+",危险源报警");
                        mPieChart.invalidate();
                        Log.e("dafda", "onSuccess: ");
                        linearLayout.setVisibility(View.VISIBLE);
    //                    sepPieChart(alarmEvent);
                    }else{
                        linearLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                linearLayout.setVisibility(View.GONE);

            }
        });
    }

}
