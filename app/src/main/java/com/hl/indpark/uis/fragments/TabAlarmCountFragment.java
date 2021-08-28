package com.hl.indpark.uis.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.entities.events.WxyTjEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.Util;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TabAlarmCountFragment extends BaseFragment implements HorizontalProgressView.HorizontalProgressUpdateListener {
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
    TextView tv_db;    @BindView(R.id.tv_sum)
    TextView tv_sum;
    public static final int[] PIECOLORS = {
            Color.rgb(54, 136, 255), Color.rgb(244, 177, 136)
    };
    private PieChart chart;
    private Typeface tf;
    private Map<Integer, Float> map;
    private Map<Integer, Integer> mapShow;
    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_alarm_count;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        chart = root.findViewById(R.id.pieChart1);
        hp_ggb.setProgressViewUpdateListener(this);
        hp_gb.setProgressViewUpdateListener(this);
        hp_ddb.setProgressViewUpdateListener(this);
        hp_db.setProgressViewUpdateListener(this);
        initWxyData();
        PieCountHb();
        getWxyTj();
    }
    public void initWxyData() {
        map = new HashMap<>();
        mapShow = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(i, 0f);
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
        ds1.setValueTextSize(4f);
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
        ArticlesRepo.getEpAlarm("1").observe(this, new ApiObserver<EPAlarmEvent>() {
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
                tv_gb.setText(mapShow.get(2)  + "");
                break;
            case R.id.hp_ddb:
                tv_ddb.setText(mapShow.get(4)  + "");
                break;
            case R.id.hp_db:
                tv_db.setText(mapShow.get(3)  + "");
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
        ArticlesRepo.getWxyTjEvent().observe(this, new ApiObserver<List<WxyTjEvent>>() {
            @Override
            public void onSuccess(Response<List<WxyTjEvent>> response) {
                if (response.getData() != null && response.getData().size() > 0) {
                    int count = 0;
                    List<WxyTjEvent> valueBeans = new ArrayList<>();
                    valueBeans.addAll(response.getData());
                    for (int i = 0; i < valueBeans.size(); i++) {
                        if (valueBeans.get(i).key != 0) {
                            count += valueBeans.get(i).value;
                        }
                    }
                    for (int i = 0; i < valueBeans.size(); i++) {
                        if (valueBeans.get(i).key != 0) {
                            map.put(valueBeans.get(i).key, (float) valueBeans.get(i).value/count*100);
                            mapShow.put(valueBeans.get(i).key, valueBeans.get(i).value);
                        }
                    }
                    map.put(0, (float) count);
                    mapShow.put(0, count);
                    showWxy();
                    Log.e("TAG", "onSuccess: ");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }
        });
    }

    public void showWxy() {
        tv_sum.setText("危险源报警："+mapShow.get(0));
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