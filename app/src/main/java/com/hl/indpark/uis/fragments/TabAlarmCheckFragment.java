package com.hl.indpark.uis.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartEPDataActivity;
import com.hl.indpark.uis.activities.PieChartSH1DataActivity;
import com.hl.indpark.utils.Util;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class TabAlarmCheckFragment extends BaseFragment implements HorizontalProgressView.HorizontalProgressUpdateListener {

    @BindView(R.id.hp_ggb)
    HorizontalProgressView hp_ggb;
    @BindView(R.id.tv_ggb)
    TextView tv_ggb;
    @BindView(R.id.hp_gb)
    HorizontalProgressView hp_gb;
    @BindView(R.id.tv_gb)
    TextView tv_gb;
    @BindView(R.id.hp_ddb)
    HorizontalProgressView hp_ddb;
    @BindView(R.id.tv_ddb)
    TextView tv_ddb;
    @BindView(R.id.hp_db)
    HorizontalProgressView hp_db;
    @BindView(R.id.tv_db)
    TextView tv_db;
    @BindView(R.id.tv_sum)
    TextView tv_sum;
    public static final int[] PIECOLORS = {
            Color.rgb(54, 136, 255), Color.rgb(244, 177, 136)
    };
    private PieChart chart;
    private Typeface tf;
    private Map<Integer, Float> map;
    private Map<Integer, Integer> mapShow;
    private String type = "2";
    private Intent intent;
    @BindView(R.id.pieChart1)
    PieChart pieChart;
    @OnClick({R.id.rl_hbfx,R.id.pieChart1, R.id.ll_wxyfx})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.rl_hbfx:
            case R.id.pieChart1:
                intent = new Intent(getActivity(), PieChartEPDataActivity.class);
                intent.putExtra("type", Integer.valueOf(type));
                startActivity(intent);
                break;
            case R.id.ll_wxyfx:
                intent = new Intent(getActivity(), PieChartSH1DataActivity.class);
                intent.putExtra("timeType",Integer.valueOf(type));
                startActivity(intent);
                break;
        }
    }

    @OnCheckedChanged({R.id.rg_month, R.id.rg_quarter, R.id.rg_year})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rg_month:
                if (ischanged) {
                    type = "2";
                    getCountHb();
                    getWxyTj();
                }
                break;
            case R.id.rg_quarter:
                if (ischanged) {
                    type = "3";
                    getCountHb();
                    getWxyTj();
                }
                break;
            case R.id.rg_year:
                if (ischanged) {
                    type = "4";
                    getCountHb();
                    getWxyTj();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_alarm_check;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        chart = root.findViewById(R.id.pieChart1);
        hp_ggb.setProgressViewUpdateListener(this);
        hp_gb.setProgressViewUpdateListener(this);
        hp_ddb.setProgressViewUpdateListener(this);
        hp_db.setProgressViewUpdateListener(this);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                intent = new Intent(getActivity(), PieChartEPDataActivity.class);
                intent.putExtra("type", Integer.valueOf(type));
                startActivity(intent);
            }
            @Override
            public void onNothingSelected() {

            }
        });
        initWxyData();
        PieCountHb();
        getWxyTj();
    }

    public void initWxyData() {
        map = new HashMap<>();
        mapShow = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(i, 0F);
            mapShow.put(i, 0);
        }
    }

    public void PieCountHb() {
        chart.getDescription().setEnabled(false);
        tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");
        chart.setCenterTextTypeface(tf);
        chart.setCenterTextSize(10f);
        chart.setCenterTextTypeface(tf);
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(50f);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        getCountHb();
    }

    protected PieData generatePieData(EPAlarmEvent hb) {
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        entries1.add(new PieEntry((float) (60), hb.gasNumber + "" + hb.gasStr));
        entries1.add(new PieEntry((float) (40), hb.waterNumber + hb.waterStr));
        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(PIECOLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(2f);
        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);
        return d;
    }

    private SpannableString generateCenterText(int num) {
        String numCount = String.valueOf(num);
        SpannableString s = new SpannableString(numCount + "\n环保报警");
        s.length();
        s.setSpan(new RelativeSizeSpan(2f), 0, numCount.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), numCount.length(), s.length(), 0);
        return s;
    }

    private void getCountHb() {
        ArticlesRepo.getEpAlarm(type).observe(this, new ApiObserver<EPAlarmEvent>() {
            @Override
            public void onSuccess(Response<EPAlarmEvent> response) {
                try {
                    if (response != null && response.getData() != null) {
                        EPAlarmEvent hbCount = response.getData();
                        chart.setCenterText(generateCenterText(hbCount.totalNumber));
                        chart.setData(generatePieData(hbCount));
                        for (IDataSet<?> set : chart.getData().getDataSets())
                            set.setDrawValues(!set.isDrawValuesEnabled());
                        chart.animateXY(1400, 1400);
                        chart.invalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    @Override
    public void onHorizontalProgressStart(View view) {

    }

    @Override
    public void onHorizontalProgressUpdate(View view, float progress) {
        int progressInt = (int) progress;
        switch (view.getId()) {
            case R.id.hp_ggb:
                tv_ggb.setText(mapShow.get(1) + "");
                break;
            case R.id.hp_gb:
                tv_gb.setText(mapShow.get(2)+ "");
                break;
            case R.id.hp_ddb:
                tv_ddb.setText(mapShow.get(4)+ "");
                break;
            case R.id.hp_db:
                tv_db.setText(mapShow.get(3)+ "");
                break;
            default:
                break;
        }
    }

    @Override
    public void onHorizontalProgressFinished(View view) {

    }

    @Override
    public void onDestroyView() {
        hp_gb.stopProgressAnimation();
        hp_ggb.stopProgressAnimation();
        hp_ddb.stopProgressAnimation();
        hp_db.stopProgressAnimation();
        hp_ggb.setProgressViewUpdateListener(null);
        hp_gb.setProgressViewUpdateListener(null);
        hp_ddb.setProgressViewUpdateListener(null);
        hp_db.setProgressViewUpdateListener(null);
        super.onDestroyView();
    }

    private void getWxyTj() {
        initWxyData();
        ArticlesRepo.getHSAlarm(type).observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
                if (response.getData() != null && response.getData().value.size() > 0) {

                    int count = 0;
                    List<HSAlarmEvent.ValueBean> valueBeans = new ArrayList<>();
                    valueBeans.addAll(response.getData().value);
                    if (valueBeans.size()>4){
                        for (int i = 0; i < 4; i++) {
                            if (valueBeans.get(i).type != 0) {
                                count += valueBeans.get(i).num;
                                map.put(valueBeans.get(i).type, (float) (valueBeans.get(i).num/Float.valueOf(response.getData().key)*100));
                                mapShow.put(valueBeans.get(i).type, valueBeans.get(i).num);
                            }
                        }
                    }else{
                        for (int i = 0; i < valueBeans.size(); i++) {
                            if (valueBeans.get(i).type != 0) {
                                count += valueBeans.get(i).num;
                                map.put(valueBeans.get(i).type, (float) (valueBeans.get(i).num/Float.valueOf(response.getData().key)*100));
                                mapShow.put(valueBeans.get(i).type, valueBeans.get(i).num);
                            }
                        }
                    }

                    map.put(0, (float)count);
                    mapShow.put(0, count);
                    showWxy();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    public void showWxy() {
        tv_sum.setText("危险源报警：" + mapShow.get(0));
        hp_ggb.setEndProgress(map.get(1));
        hp_gb.setEndProgress(map.get(2));
        hp_ddb.setEndProgress(map.get(4));
        hp_db.setEndProgress(map.get(3));


        hp_ggb.startProgressAnimation();
        hp_gb.startProgressAnimation();
        hp_ddb.startProgressAnimation();
        hp_db.startProgressAnimation();
    }
}